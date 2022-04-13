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
public class SqlDescribeDatabase extends SqlCall {

    public static final SqlSpecialOperator OPERATOR =
            new SqlSpecialOperator("DESCRIBE DATABASE", SqlKind.OTHER);
    private final SqlIdentifier databaseName;
    private boolean isExtended = false;

    public SqlDescribeDatabase(SqlParserPos pos, SqlIdentifier databaseName, boolean isExtended) {
        super(pos);
        this.databaseName = databaseName;
        this.isExtended = isExtended;
    }

    @Override
    public SqlOperator getOperator() {
        return OPERATOR;
    }

    @Override
    public List<SqlNode> getOperandList() {
        return Collections.singletonList(databaseName);
    }

    public String getDatabaseName() {
        return databaseName.getSimple();
    }

    public boolean isExtended() {
        return isExtended;
    }

    public String[] fullDatabaseName() {
        return databaseName.names.toArray(new String[0]);
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword("DESCRIBE DATABASE");
        if (isExtended) {
            writer.keyword("EXTENDED");
        }
        databaseName.unparse(writer, leftPrec, rightPrec);
    }

}
