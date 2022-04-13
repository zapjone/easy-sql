package com.easy.sql.parser.dql;

import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.SqlSpecialOperator;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;

import java.util.Collections;
import java.util.List;

/**
 * SHOW [USER] FUNCTIONS Sql Call.
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlShowFunctions extends SqlCall {

    public static final SqlSpecialOperator OPERATOR =
            new SqlSpecialOperator("SHOW FUNCTIONS", SqlKind.OTHER);

    private final boolean requireUser;

    public SqlShowFunctions(SqlParserPos pos, boolean requireUser) {
        super(pos);
        this.requireUser = requireUser;
    }

    @Override
    public SqlOperator getOperator() {
        return OPERATOR;
    }

    @Override
    public List<SqlNode> getOperandList() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        if (requireUser) {
            writer.keyword("SHOW USER FUNCTIONS");
        } else {
            writer.keyword("SHOW FUNCTIONS");
        }
    }

    public boolean requireUser() {
        return requireUser;
    }

}
