package com.easy.sql.core.planner.plan.optimize;

import com.easy.sql.core.planner.delegation.Planner;
import org.apache.calcite.rel.RelNode;

import java.util.List;
import java.util.stream.Collectors;

/**
 * EasySql优化器
 *
 * @author zhangap
 * @version 1.0, 2022/4/22
 */
public class EasyOptimizer implements Optimizer {

    private final Planner planner;

    public EasyOptimizer(Planner planner) {
        this.planner = planner;
    }


    @Override
    public List<RelNode> optimize(List<RelNode> roots) {
        EasySqlChainedProgram<EasySqlOptimizerContext> programs = EasySqlProgram.buildProgram(planner.getConfig());
        return roots.stream()
                .map(relNode -> programs.optimize(relNode, () -> false))
                .collect(Collectors.toList());
    }

}
