package com.easy.sql.core.planner.plan.optimize;

import org.apache.calcite.rel.RelNode;

import java.util.List;

/**
 * @author zhangap
 * @version 1.0, 2022/4/22
 */
public interface Optimizer {

    /**
     * 优化
     */
    List<RelNode> optimize(List<RelNode> roots);

}
