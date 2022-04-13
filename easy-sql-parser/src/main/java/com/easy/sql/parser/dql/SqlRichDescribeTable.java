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
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlRichDescribeTable extends SqlCall {

    public static final SqlSpecialOperator OPERATOR =
            new SqlSpecialOperator("DESCRIBE TABLE", SqlKind.DESCRIBE_TABLE);
    protected final SqlIdentifier tableNameIdentifier;
    private boolean isExtended = false;

    public SqlRichDescribeTable(
            SqlParserPos pos, SqlIdentifier tableNameIdentifier, boolean isExtended) {
        super(pos);
        this.tableNameIdentifier = tableNameIdentifier;
        this.isExtended = isExtended;
    }

    @Override
    public SqlOperator getOperator() {
        return OPERATOR;
    }

    @Override
    public List<SqlNode> getOperandList() {
        return Collections.singletonList(tableNameIdentifier);
    }

    public boolean isExtended() {
        return isExtended;
    }

    public String[] fullTableName() {
        return tableNameIdentifier.names.toArray(new String[0]);
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword("DESCRIBE");
        if (isExtended) {
            writer.keyword("EXTENDED");
        }
        tableNameIdentifier.unparse(writer, leftPrec, rightPrec);
    }

}
