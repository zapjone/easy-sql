package org.apache.calcite.jdbc;

import org.apache.calcite.schema.Schema;

/**
 * 因SimpleCalciteSchema类在calcite中属于包级别，所以这里使用包名和calcite包名
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class CalciteSchemaBuilder {

    private CalciteSchemaBuilder() {
    }

    public static CalciteSchema asRootSchema(Schema root) {
        return new SimpleCalciteSchema(null, root, "");
    }

}
