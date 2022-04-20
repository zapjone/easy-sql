package com.easy.sql.core.planner.functions;

import org.apache.calcite.sql.SqlFunctionCategory;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.SqlSyntax;
import org.apache.calcite.sql.util.ReflectiveSqlOperatorTable;
import org.apache.calcite.sql.validate.SqlNameMatcher;
import org.apache.calcite.sql.validate.SqlNameMatchers;

import java.util.List;

/**
 * @author zhangap
 * @version 1.0, 2022/4/20
 */
public class EasySqlOperatorTable extends ReflectiveSqlOperatorTable {

    private static volatile EasySqlOperatorTable instance;

    public static EasySqlOperatorTable getInstance() {
        if (null == instance) {
            synchronized (EasySqlOperatorTable.class) {
                if (null == instance) {
                    instance = new EasySqlOperatorTable();
                    instance.init();
                }
            }
        }
        return instance;
    }


    @Override
    public void lookupOperatorOverloads(
            SqlIdentifier opName,
            SqlFunctionCategory category,
            SqlSyntax syntax,
            List<SqlOperator> operatorList,
            SqlNameMatcher nameMatcher) {
        // set caseSensitive=false to make sure the behavior is same with before.
        super.lookupOperatorOverloads(
                opName, category, syntax, operatorList,
                SqlNameMatchers.withCaseSensitive(false));
    }

    // -----------------------------------------------------------------------------
    // Easy sql可以在以下添加内置函数
    // -----------------------------------------------------------------------------

    // FUNCTIONS

}
