package com.easy.sql.core.planner.calcite;

import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.sql.SqlOperatorTable;
import org.apache.calcite.sql.validate.SqlValidatorCatalogReader;
import org.apache.calcite.sql.validate.SqlValidatorImpl;

/**
 * @author zhangap
 * @version 1.0, 2022/4/20
 */
public class EasyCalciteSqlValidator extends SqlValidatorImpl {

    /**
     * Creates a validator.
     *
     * @param opTab         Operator table
     * @param catalogReader Catalog reader
     * @param typeFactory   Type factory
     * @param config        Config
     */
    protected EasyCalciteSqlValidator(SqlOperatorTable opTab,
                                      SqlValidatorCatalogReader catalogReader,
                                      RelDataTypeFactory typeFactory,
                                      Config config) {
        super(opTab, catalogReader, typeFactory, config);
    }
}
