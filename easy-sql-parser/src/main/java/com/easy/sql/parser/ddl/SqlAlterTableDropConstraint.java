package com.easy.sql.parser.ddl;

import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.util.ImmutableNullableList;

import java.util.List;

/**
 * ALTER TABLE [catalog_name.][db_name.]table_name DROP CONSTRAINT constraint_name.
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlAlterTableDropConstraint extends SqlAlterTable {
    private final SqlIdentifier constraintName;

    /**
     * Creates an alter table drop constraint node.
     *
     * @param tableID        Table ID
     * @param constraintName Constraint name
     * @param pos            Parser position
     */
    public SqlAlterTableDropConstraint(
            SqlIdentifier tableID, SqlIdentifier constraintName, SqlParserPos pos) {
        super(pos, tableID);
        this.constraintName = constraintName;
    }

    public SqlIdentifier getConstraintName() {
        return constraintName;
    }

    @Override
    public List<SqlNode> getOperandList() {
        return ImmutableNullableList.of(getTableName(), this.constraintName);
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        super.unparse(writer, leftPrec, rightPrec);
        writer.keyword("DROP CONSTRAINT");
        this.constraintName.unparse(writer, leftPrec, rightPrec);
    }
}
