package com.easy.sql.parser.ddl;

import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.SqlSpecialOperator;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.util.ImmutableNullableList;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * ALTER Database DDL sql call
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlAlterDatabase extends SqlCall {


    public static final SqlSpecialOperator OPERATOR =
            new SqlSpecialOperator("ALTER DATABASE", SqlKind.OTHER_DDL);

    private final SqlIdentifier databaseName;

    private final SqlNodeList propertyList;

    public SqlAlterDatabase(
            SqlParserPos pos, SqlIdentifier databaseName, SqlNodeList propertyList) {
        super(pos);
        this.databaseName = requireNonNull(databaseName, "tableName should not be null");
        this.propertyList = requireNonNull(propertyList, "propertyList should not be null");
    }

    @Override
    public SqlOperator getOperator() {
        return OPERATOR;
    }

    @Override
    public List<SqlNode> getOperandList() {
        return ImmutableNullableList.of(databaseName, propertyList);
    }

    public SqlIdentifier getDatabaseName() {
        return databaseName;
    }

    public SqlNodeList getPropertyList() {
        return propertyList;
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword("ALTER DATABASE");
        databaseName.unparse(writer, leftPrec, rightPrec);
        writer.keyword("SET");
        SqlWriter.Frame withFrame = writer.startList("(", ")");
        for (SqlNode property : propertyList) {
            printIndent(writer);
            property.unparse(writer, leftPrec, rightPrec);
        }
        writer.newlineAndIndent();
        writer.endList(withFrame);
    }

    protected void printIndent(SqlWriter writer) {
        writer.sep(",", false);
        writer.newlineAndIndent();
        writer.print("  ");
    }

    public String[] fullDatabaseName() {
        return databaseName.names.toArray(new String[0]);
    }

}
