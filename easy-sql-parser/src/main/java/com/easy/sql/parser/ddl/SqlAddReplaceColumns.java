package com.easy.sql.parser.ddl;

import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.util.ImmutableNullableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * ALTER DDL to ADD or REPLACE columns for a table.
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlAddReplaceColumns extends SqlAlterTable {

    private final SqlNodeList newColumns;
    private final boolean replace;
    private final SqlNodeList properties;

    public SqlAddReplaceColumns(
            SqlParserPos pos,
            SqlIdentifier tableName,
            SqlNodeList newColumns,
            boolean replace,
            @Nullable SqlNodeList properties) {
        super(pos, tableName);
        this.newColumns = newColumns;
        this.replace = replace;
        this.properties = properties;
    }

    public SqlNodeList getNewColumns() {
        return newColumns;
    }

    public boolean isReplace() {
        return replace;
    }

    public SqlNodeList getProperties() {
        return properties;
    }

    @Nonnull
    @Override
    public List<SqlNode> getOperandList() {
        return ImmutableNullableList.of(tableIdentifier, partitionSpec, newColumns, properties);
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        super.unparse(writer, leftPrec, rightPrec);
        if (replace) {
            writer.keyword("REPLACE");
        } else {
            writer.keyword("ADD");
        }
        writer.keyword("COLUMNS");
        SqlWriter.Frame frame = writer.startList(SqlWriter.FrameTypeEnum.create("sds"), "(", ")");
        for (SqlNode column : newColumns) {
            printIndent(writer);
            column.unparse(writer, leftPrec, rightPrec);
        }
        writer.newlineAndIndent();
        writer.endList(frame);
    }

    protected void printIndent(SqlWriter writer) {
        writer.sep(",", false);
        writer.newlineAndIndent();
        writer.print("  ");
    }
}
