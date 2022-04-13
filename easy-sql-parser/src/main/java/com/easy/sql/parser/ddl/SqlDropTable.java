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
 * DROP TABLE DDL sql call.
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlDropTable extends SqlDrop {

    private static final SqlOperator OPERATOR =
            new SqlSpecialOperator("DROP TABLE", SqlKind.DROP_TABLE);

    private SqlIdentifier tableName;
    private boolean ifExists;
    private final boolean isTemporary;

    public SqlDropTable(
            SqlParserPos pos, SqlIdentifier tableName, boolean ifExists, boolean isTemporary) {
        super(OPERATOR, pos, ifExists);
        this.tableName = tableName;
        this.ifExists = ifExists;
        this.isTemporary = isTemporary;
    }

    @Override
    public List<SqlNode> getOperandList() {
        return ImmutableNullableList.of(tableName);
    }

    public SqlIdentifier getTableName() {
        return tableName;
    }

    public void setTableName(SqlIdentifier viewName) {
        this.tableName = viewName;
    }

    public boolean getIfExists() {
        return this.ifExists;
    }

    public void setIfExists(boolean ifExists) {
        this.ifExists = ifExists;
    }

    public boolean isTemporary() {
        return this.isTemporary;
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword("DROP");
        if (isTemporary) {
            writer.keyword("TEMPORARY");
        }
        writer.keyword("TABLE");
        if (ifExists) {
            writer.keyword("IF EXISTS");
        }
        tableName.unparse(writer, leftPrec, rightPrec);
    }

    public String[] fullTableName() {
        return tableName.names.toArray(new String[0]);
    }

}
