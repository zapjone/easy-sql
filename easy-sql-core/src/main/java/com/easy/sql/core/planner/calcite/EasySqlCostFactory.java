package com.easy.sql.core.planner.calcite;

import org.apache.calcite.plan.RelOptCost;
import org.apache.calcite.plan.RelOptCostFactory;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class EasySqlCostFactory implements RelOptCostFactory {

    public RelOptCost makeCost(double rowCount, double cpu, double io, double network, double memory) {
        return new EasySqlCost(rowCount, cpu, io, network, memory, null);
    }

    @Override
    public RelOptCost makeCost(double rowCount, double cpu, double io) {
        return new EasySqlCost(rowCount, cpu, io, 0.0, 0.0, null);
    }

    @Override
    public RelOptCost makeHugeCost() {
        return EasySqlCost.HUGE;
    }

    @Override
    public RelOptCost makeInfiniteCost() {
        return EasySqlCost.INFINITY;
    }

    @Override
    public RelOptCost makeTinyCost() {
        return EasySqlCost.TINY;
    }

    @Override
    public RelOptCost makeZeroCost() {
        return EasySqlCost.ZERO;
    }
}
