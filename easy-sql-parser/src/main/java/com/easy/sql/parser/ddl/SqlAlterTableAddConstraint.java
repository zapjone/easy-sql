package com.easy.sql.parser.ddl;

import com.easy.sql.parser.ddl.constraint.SqlTableConstraint;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.util.ImmutableNullableList;

import java.util.List;

/**
 * ALTER TABLE [catalog_name.][db_name.]table_name ADD [CONSTRAINT constraint_name] (PRIMARY KEY |
 * UNIQUE) (column, ...) [[NOT] ENFORCED].
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlAlterTableAddConstraint extends SqlAlterTable {
    private final SqlTableConstraint constraint;

    /**
     * Creates a add table constraint node.
     *
     * @param tableID    Table ID
     * @param constraint Table constraint
     * @param pos        Parser position
     */
    public SqlAlterTableAddConstraint(
            SqlIdentifier tableID, SqlTableConstraint constraint, SqlParserPos pos) {
        super(pos, tableID);
        this.constraint = constraint;
    }

    public SqlTableConstraint getConstraint() {
        return constraint;
    }

    @Override
    public List<SqlNode> getOperandList() {
        return ImmutableNullableList.of(getTableName(), this.constraint);
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        super.unparse(writer, leftPrec, rightPrec);
        writer.keyword("ADD");
        this.constraint.unparse(writer, leftPrec, rightPrec);
    }

}
