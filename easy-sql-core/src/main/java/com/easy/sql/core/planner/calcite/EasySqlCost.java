package com.easy.sql.core.planner.calcite;

import org.apache.calcite.plan.RelOptCost;
import org.apache.calcite.plan.RelOptUtil;

/**
 * 自定义CBO代价计算
 *
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class EasySqlCost implements RelOptCost {

    private final double rowCount;
    private final double cpu;
    private final double io;
    private final double network;
    private final double memory;
    private final String output;

    protected static final EasySqlCost INFINITY = new EasySqlCost(Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY, "{inf}");

    protected static final EasySqlCost HUGE = new EasySqlCost(Double.MAX_VALUE,
            Double.MAX_VALUE,
            Double.MAX_VALUE,
            Double.MAX_VALUE,
            Double.MAX_VALUE, "{huge}");

    protected static final EasySqlCost ZERO = new EasySqlCost(0.0, 0.0, 0.0,
            0.0, 0.0, "{0}");

    protected static final EasySqlCost TINY = new EasySqlCost(1.0, 1.0, 0.0,
            0.0, 0.0, "{tiny}");

    private static final double MEMORY_TO_CPU_RATIO = 1.0;
    private static final double IO_TO_CPU_RATIO = 2.0;
    private static final double NETWORK_TO_CPU_RATIO = 4.0;
    private static final int BASE_CPU_COST = 1;

    private static final int HASH_CPU_COST = 8 * BASE_CPU_COST;
    private static final int SERIALIZE_DESERIALIZE_CPU_COST = 160 * BASE_CPU_COST;
    private static final int RANDOM_CPU_COST = BASE_CPU_COST;
    private static final int SINGLETON_CPU_COST = BASE_CPU_COST;

    private static final int COMPARE_CPU_COST = 4 * BASE_CPU_COST;
    private static final int FUNC_CPU_COST = 12 * BASE_CPU_COST;
    private static final int RANGE_PARTITION_CPU_COST = 12 * BASE_CPU_COST;
    private static final int SQL_DEFAULT_PARALLELISM_WORKER_PROCESS_SIZE = 1024 * 1024 * 1024;

    private static final int HASH_COLLISION_WEIGHT = 2;

    public EasySqlCost(double rowCount,
                       double cpu,
                       double io,
                       double network,
                       double memory,
                       String output) {
        this.rowCount = rowCount;
        this.cpu = cpu;
        this.io = io;
        this.network = network;
        this.memory = memory;
        if (null != output) {
            this.output = output;
        } else {
            this.output = String.format("%s rows, %s cpu, %s io, %s network, %s memory",
                    rowCount, cpu, io, network, memory);
        }
    }


    @Override
    public double getRows() {
        return rowCount;
    }

    @Override
    public double getCpu() {
        return cpu;
    }

    @Override
    public double getIo() {
        return io;
    }

    @Override
    public boolean isInfinite() {
        return this.equals(EasySqlCost.INFINITY) ||
                this.rowCount == Double.POSITIVE_INFINITY ||
                this.cpu == Double.POSITIVE_INFINITY ||
                this.io == Double.POSITIVE_INFINITY ||
                this.network == Double.POSITIVE_INFINITY ||
                this.memory == Double.POSITIVE_INFINITY;
    }

    @Override
    public boolean equals(RelOptCost other) {
        boolean otherEq = false;
        if (other instanceof EasySqlCost) {
            EasySqlCost otherCost = (EasySqlCost) other;
            otherEq = this.rowCount == otherCost.rowCount &&
                    this.cpu == otherCost.cpu &&
                    this.io == otherCost.io &&
                    this.network == otherCost.network &&
                    this.memory == otherCost.memory;
        }
        return otherEq;
    }

    @Override
    public boolean isEqWithEpsilon(RelOptCost cost) {
        if (!(cost instanceof EasySqlCost)) {
            return false;
        }
        EasySqlCost that = (EasySqlCost) cost;
        return this.equals(that) || ((Math.abs(this.rowCount - that.rowCount) < RelOptUtil.EPSILON) &&
                (Math.abs(this.cpu - that.cpu) < RelOptUtil.EPSILON) &&
                (Math.abs(this.io - that.io) < RelOptUtil.EPSILON) &&
                (Math.abs(this.network - that.network) < RelOptUtil.EPSILON) &&
                (Math.abs(this.memory - that.memory) < RelOptUtil.EPSILON));
    }

    @Override
    public boolean isLe(RelOptCost cost) {
        if (cost instanceof EasySqlCost) {
            if (cost.equals(this)) {
                return true;
            }
            EasySqlCost that = (EasySqlCost) cost;
            double cost1 = normalizeCost(this.memory, this.network, this.io);
            double cost2 = normalizeCost(that.memory, that.network, that.io);
            return (this.cpu < that.cpu) ||
                    (this.cpu == that.cpu && cost1 < cost2) ||
                    (this.cpu == that.cpu && cost1 == cost2 && this.rowCount <= that.rowCount);
        }
        return false;
    }

    private double normalizeCost(double memory, double network, double io) {
        return memory * EasySqlCost.MEMORY_TO_CPU_RATIO +
                network * EasySqlCost.NETWORK_TO_CPU_RATIO +
                io * EasySqlCost.IO_TO_CPU_RATIO;
    }

    @Override
    public boolean isLt(RelOptCost cost) {
        return isLe(cost) && !this.equals(cost);
    }

    @Override
    public RelOptCost plus(RelOptCost other) {
        EasySqlCost that = (EasySqlCost) other;
        if (this.equals(EasySqlCost.INFINITY) || that.equals(EasySqlCost.INFINITY)) {
            return EasySqlCost.INFINITY;
        }

        return new EasySqlCost(this.rowCount + that.rowCount,
                this.cpu + that.cpu,
                this.io + that.io,
                this.network + that.network,
                this.memory + that.memory, null);
    }

    @Override
    public RelOptCost minus(RelOptCost cost) {
        if (this.equals(EasySqlCost.INFINITY)) {
            return this;
        }
        EasySqlCost that = (EasySqlCost) cost;
        return new EasySqlCost(
                this.rowCount - that.rowCount,
                this.cpu - that.cpu,
                this.io - that.io,
                this.network - that.network,
                this.memory - that.memory, null);
    }

    @Override
    public RelOptCost multiplyBy(double factor) {
        if (this.equals(EasySqlCost.INFINITY)) {
            return this;
        }
        return new EasySqlCost(
                this.rowCount * factor,
                this.cpu * factor,
                this.io * factor,
                this.network * factor,
                this.memory * factor, null);
    }

    @Override
    public double divideBy(RelOptCost cost) {
        EasySqlCost that = (EasySqlCost) cost;
        double d = 1.0;
        double n = 0.0;

        if ((this.rowCount != 0) && !new Double(this.rowCount).isInfinite() &&
                (that.rowCount != 0) && !new Double(that.rowCount).isInfinite()) {
            d *= this.rowCount / that.rowCount;
            n += 1;
        }
        if ((this.cpu != 0) && !new Double(this.cpu).isInfinite() && (that.cpu != 0) && !new Double(that.cpu).isInfinite()) {
            d *= this.cpu / that.cpu;
            n += 1;
        }
        if ((this.io != 0) && !new Double(this.io).isInfinite() && (that.io != 0) && !new Double(that.io).isInfinite()) {
            d *= this.io / that.io;
            n += 1;
        }
        if ((this.network != 0) && !new Double(this.network).isInfinite() &&
                (that.network != 0) && !new Double(that.network).isInfinite()) {
            d *= this.network / that.network;
            n += 1;
        }
        if ((this.memory != 0) && !new Double(this.memory).isInfinite() &&
                (that.memory != 0) && !new Double(that.memory).isInfinite()) {
            d *= this.memory / that.memory;
            n += 1;
        }
        if (n == 0) {
            return 1.0;
        }

        return Math.pow(d, 1 / n);
    }

    @Override
    public int hashCode() {
        return Double.hashCode(rowCount) +
                Double.hashCode(cpu) +
                Double.hashCode(io) +
                Double.hashCode(network) +
                Double.hashCode(memory);
    }

    @Override
    public String toString() {
        return this.output;
    }
}
