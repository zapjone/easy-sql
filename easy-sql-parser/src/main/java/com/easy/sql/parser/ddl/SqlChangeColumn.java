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
 * ALTER DDL to CHANGE a column for a table.
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlChangeColumn extends SqlAlterTable {

    private final SqlIdentifier oldName;
    private final SqlTableColumn newColumn;

    private final SqlIdentifier after;
    private final boolean first;

    private final SqlNodeList properties;

    public SqlChangeColumn(
            SqlParserPos pos,
            SqlIdentifier tableName,
            SqlIdentifier oldName,
            SqlTableColumn newColumn,
            @Nullable SqlIdentifier after,
            boolean first,
            @Nullable SqlNodeList properties) {
        super(pos, tableName);
        if (after != null && first) {
            throw new IllegalArgumentException("FIRST and AFTER cannot be set at the same time");
        }
        this.oldName = oldName;
        this.newColumn = newColumn;
        this.after = after;
        this.first = first;
        this.properties = properties;
    }

    public SqlIdentifier getOldName() {
        return oldName;
    }

    public SqlTableColumn getNewColumn() {
        return newColumn;
    }

    public SqlIdentifier getAfter() {
        return after;
    }

    public boolean isFirst() {
        return first;
    }

    public SqlNodeList getProperties() {
        return properties;
    }

    @Nonnull
    @Override
    public List<SqlNode> getOperandList() {
        return ImmutableNullableList.of(tableIdentifier, partitionSpec, oldName, newColumn, after);
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        super.unparse(writer, leftPrec, rightPrec);
        writer.keyword("CHANGE COLUMN");
        oldName.unparse(writer, leftPrec, rightPrec);
        newColumn.unparse(writer, leftPrec, rightPrec);
        if (first) {
            writer.keyword("FIST");
        }
        if (after != null) {
            writer.keyword("AFTER");
            after.unparse(writer, leftPrec, rightPrec);
        }
    }

}
