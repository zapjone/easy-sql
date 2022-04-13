package com.easy.sql.parser.dml;

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
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlBeginStatementSet extends SqlCall {

    public static final SqlSpecialOperator OPERATOR =
            new SqlSpecialOperator("BEGIN STATEMENT SET", SqlKind.OTHER);

    public SqlBeginStatementSet(SqlParserPos pos) {
        super(pos);
    }

    @Override
    public SqlOperator getOperator() {
        return OPERATOR;
    }

    @Override
    public List<SqlNode> getOperandList() {
        return Collections.emptyList();
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword("BEGIN STATEMENT SET");
    }

}
