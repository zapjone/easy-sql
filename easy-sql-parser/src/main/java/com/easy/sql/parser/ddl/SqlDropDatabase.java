package com.easy.sql.parser.ddl;

import org.apache.calcite.sql.SqlDrop;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.SqlSpecialOperator;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.util.ImmutableNullableList;

import java.util.List;

/**
 * DROP DATABASE DDL sql call.
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlDropDatabase extends SqlDrop {

    private static final SqlOperator OPERATOR =
            new SqlSpecialOperator("DROP DATABASE", SqlKind.OTHER_DDL);

    private final SqlIdentifier databaseName;
    private final boolean ifExists;
    private final boolean isCascade;

    public SqlDropDatabase(
            SqlParserPos pos, SqlIdentifier databaseName, boolean ifExists, boolean isCascade) {
        super(OPERATOR, pos, ifExists);
        this.databaseName = databaseName;
        this.ifExists = ifExists;
        this.isCascade = isCascade;
    }

    @Override
    public List<SqlNode> getOperandList() {
        return ImmutableNullableList.of(databaseName);
    }

    public SqlIdentifier getDatabaseName() {
        return databaseName;
    }

    public boolean getIfExists() {
        return this.ifExists;
    }

    public boolean isCascade() {
        return isCascade;
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword("DROP");
        writer.keyword("DATABASE");
        if (ifExists) {
            writer.keyword("IF EXISTS");
        }
        databaseName.unparse(writer, leftPrec, rightPrec);
        if (isCascade) {
            writer.keyword("CASCADE");
        } else {
            writer.keyword("RESTRICT");
        }
    }

    public String[] fullDatabaseName() {
        return databaseName.names.toArray(new String[0]);
    }

}
