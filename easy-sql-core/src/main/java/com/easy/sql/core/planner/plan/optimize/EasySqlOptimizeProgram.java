package com.easy.sql.core.planner.plan.optimize;

import org.apache.calcite.rel.RelNode;

/**
 * @author zhangap
 * @version 1.0, 2022/4/24
 */
public interface EasySqlOptimizeProgram<OC extends EasySqlOptimizerContext> {

    /**
     * 逻辑计划优化
     */
    RelNode optimize(RelNode root, OC context);

}
