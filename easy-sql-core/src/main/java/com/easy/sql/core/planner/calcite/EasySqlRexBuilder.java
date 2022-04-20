package com.easy.sql.core.planner.calcite;

import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rex.RexBuilder;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class EasySqlRexBuilder extends RexBuilder {

    public EasySqlRexBuilder(RelDataTypeFactory typeFactory) {
        super(typeFactory);
    }

}
