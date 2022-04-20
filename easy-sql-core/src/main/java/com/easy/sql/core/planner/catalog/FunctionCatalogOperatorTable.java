package com.easy.sql.core.planner.catalog;

import com.easy.sql.core.planner.calcite.EasySqlTypeFactory;
import org.apache.calcite.sql.SqlFunctionCategory;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.SqlOperatorTable;
import org.apache.calcite.sql.SqlSyntax;
import org.apache.calcite.sql.validate.SqlNameMatcher;

import java.util.List;

/**
 * @author zhangap
 * @version 1.0, 2022/4/20
 */
public class FunctionCatalogOperatorTable implements SqlOperatorTable {

    private final FunctionCatalog functionCatalog;

    private final DataTypeFactory dataTypeFactory;

    private final EasySqlTypeFactory typeFactory;

    public FunctionCatalogOperatorTable(
            FunctionCatalog functionCatalog,
            DataTypeFactory dataTypeFactory,
            EasySqlTypeFactory typeFactory) {
        this.functionCatalog = functionCatalog;
        this.dataTypeFactory = dataTypeFactory;
        this.typeFactory = typeFactory;
    }


    @Override
    public void lookupOperatorOverloads(SqlIdentifier opName,
                                        SqlFunctionCategory category,
                                        SqlSyntax syntax,
                                        List<SqlOperator> operatorList,
                                        SqlNameMatcher nameMatcher) {

    }

    @Override
    public List<SqlOperator> getOperatorList() {
        return null;
    }
}
