package com.easy.sql.core.planner.calcite;

import com.easy.sql.core.planner.metadata.EasySqlDefaultRelMetadataProvider;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.rex.RexBuilder;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class EasySqlRelOptClusterFactory {

    public static RelOptCluster create(RelOptPlanner planner, RexBuilder rexBuilder) {
        RelOptCluster cluster = RelOptCluster.create(planner, rexBuilder);
        cluster.setMetadataProvider(EasySqlDefaultRelMetadataProvider.provider());
        /*cluster.setMetadataQuerySupplier(() -> {
        });*/
        return cluster;
    }

}
