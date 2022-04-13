package com.easy.sql.parser.ddl;

import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.util.ImmutableNullableList;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlAlterTableOptions extends SqlAlterTable {


    private final SqlNodeList propertyList;

    public SqlAlterTableOptions(
            SqlParserPos pos, SqlIdentifier tableName, SqlNodeList propertyList) {
        this(pos, tableName, null, propertyList);
    }

    public SqlAlterTableOptions(
            SqlParserPos pos,
            SqlIdentifier tableName,
            SqlNodeList partitionSpec,
            SqlNodeList propertyList) {
        super(pos, tableName, partitionSpec);
        this.propertyList = requireNonNull(propertyList, "propertyList should not be null");
    }

    @Override
    public List<SqlNode> getOperandList() {
        return ImmutableNullableList.of(tableIdentifier, propertyList);
    }

    public SqlNodeList getPropertyList() {
        return propertyList;
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        super.unparse(writer, leftPrec, rightPrec);
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

    @Override
    public String[] fullTableName() {
        return tableIdentifier.names.toArray(new String[0]);
    }

}
