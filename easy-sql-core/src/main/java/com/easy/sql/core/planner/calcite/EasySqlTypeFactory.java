package com.easy.sql.core.planner.calcite;

import org.apache.calcite.jdbc.JavaTypeFactoryImpl;
import org.apache.calcite.rel.type.RelDataTypeSystem;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class EasySqlTypeFactory extends JavaTypeFactoryImpl {

    public EasySqlTypeFactory(RelDataTypeSystem typeSystem) {
        super(typeSystem);
    }

}
