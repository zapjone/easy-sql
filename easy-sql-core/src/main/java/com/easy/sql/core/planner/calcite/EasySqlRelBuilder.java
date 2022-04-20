package com.easy.sql.core.planner.calcite;

import org.apache.calcite.plan.Context;
import org.apache.calcite.plan.Contexts;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptSchema;
import org.apache.calcite.tools.RelBuilder;
import org.apache.calcite.tools.RelBuilderFactory;

/**
 * @author zhangap
 * @version 1.0, 2022/4/20
 */
public class EasySqlRelBuilder extends RelBuilder {

    public EasySqlRelBuilder(Context context, RelOptCluster cluster, RelOptSchema relOptSchema) {
        super(context, cluster, relOptSchema);
    }

    public static RelBuilderFactory proto(Context context) {
        return (cluster, schema) -> {
            EasySqlContext clusterContext = cluster.getPlanner().getContext().unwrap(EasySqlContext.class);
            Context mergedContext = Contexts.chain(context, clusterContext);
            return new EasySqlRelBuilder(mergedContext, cluster, schema);
        };
    }

}
