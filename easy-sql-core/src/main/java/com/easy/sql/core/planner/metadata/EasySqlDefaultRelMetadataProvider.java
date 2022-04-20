package com.easy.sql.core.planner.metadata;

import com.google.common.collect.ImmutableList;
import org.apache.calcite.rel.metadata.ChainedRelMetadataProvider;
import org.apache.calcite.rel.metadata.RelMdColumnOrigins;
import org.apache.calcite.rel.metadata.RelMdExplainVisibility;
import org.apache.calcite.rel.metadata.RelMdMaxRowCount;
import org.apache.calcite.rel.metadata.RelMdMinRowCount;
import org.apache.calcite.rel.metadata.RelMdPredicates;
import org.apache.calcite.rel.metadata.RelMetadataProvider;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class EasySqlDefaultRelMetadataProvider {

    private EasySqlDefaultRelMetadataProvider(){}

    public static RelMetadataProvider provider(){
        return ChainedRelMetadataProvider.of(ImmutableList.of(
                RelMdColumnOrigins.SOURCE,
                RelMdMaxRowCount.SOURCE,
                RelMdMinRowCount.SOURCE,
                RelMdPredicates.SOURCE,
                RelMdExplainVisibility.SOURCE
        ));
    }


}
