package com.easy.sql.parser.dql;

import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.SqlSpecialOperator;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;

import java.util.Collections;
import java.util.List;

/**
 * SHOW CREATE TABLE sql call.
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlShowCreateTable extends SqlCall {

    public static final SqlSpecialOperator OPERATOR =
            new SqlSpecialOperator("SHOW CREATE TABLE", SqlKind.OTHER_DDL);
    private final SqlIdentifier tableName;

    public SqlShowCreateTable(SqlParserPos pos, SqlIdentifier tableName) {
        super(pos);
        this.tableName = tableName;
    }

    public SqlIdentifier getTableName() {
        return tableName;
    }

    public String[] getFullTableName() {
        return tableName.names.toArray(new String[0]);
    }

    @Override
    public SqlOperator getOperator() {
        return OPERATOR;
    }

    @Override
    public List<SqlNode> getOperandList() {
        return Collections.singletonList(tableName);
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword("SHOW CREATE TABLE");
        tableName.unparse(writer, leftPrec, rightPrec);
    }
}
