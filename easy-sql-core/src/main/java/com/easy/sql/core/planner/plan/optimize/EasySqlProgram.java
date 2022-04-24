package com.easy.sql.core.planner.plan.optimize;

import com.easy.sql.core.configuration.EasySqlConfig;

/**
 * 优化规则列表
 *
 * @author zhangap
 * @version 1.0, 2022/4/24
 */
public class EasySqlProgram {

    private static final String SUBQUERY_REWRITE = "subquery_rewrite";
    private static final String TEMPORAL_JOIN_REWRITE = "temporal_join_rewrite";
    private static final String DECORRELATE = "decorrelate";
    private static final String DEFAULT_REWRITE = "default_rewrite";
    private static final String PREDICATE_PUSHDOWN = "predicate_pushdown";
    private static final String JOIN_REORDER = "join_reorder";
    private static final String PROJECT_REWRITE = "project_rewrite";
    private static final String LOGICAL = "logical";
    private static final String LOGICAL_REWRITE = "logical_rewrite";
    private static final String PHYSICAL = "physical";
    private static final String PHYSICAL_REWRITE = "physical_rewrite";

    /**
     * 根据优化规则进行构建，添加规则列表
     */
    public static EasySqlChainedProgram<EasySqlOptimizerContext> buildProgram(EasySqlConfig config) {
        EasySqlChainedProgram<EasySqlOptimizerContext> chainedProgram = new EasySqlChainedProgram<>();
        chainedProgram.addList(SUBQUERY_REWRITE, null);

        return chainedProgram;
    }

}
