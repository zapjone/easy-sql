package com.easy.sql.parser.table.calcite;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;

/**
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public interface ExtendedRelTypeFactory extends RelDataTypeFactory {

    RelDataType createRawType(String className, String serializerString);

}
