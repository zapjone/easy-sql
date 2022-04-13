package com.easy.sql.parser.ddl.constraint;

import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.SqlSpecialOperator;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.util.ImmutableNullableList;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlTableConstraint extends SqlCall {

    private static final SqlOperator OPERATOR =
            new SqlSpecialOperator("SqlTableConstraint", SqlKind.OTHER);

    private final SqlIdentifier constraintName;
    private final SqlLiteral uniqueSpec;
    private final SqlNodeList columns;
    private final SqlLiteral enforcement;
    private final boolean isTableConstraint;

    public SqlTableConstraint(
            @Nullable SqlIdentifier constraintName,
            SqlLiteral uniqueSpec,
            SqlNodeList columns,
            @Nullable SqlLiteral enforcement,
            boolean isTableConstraint,
            SqlParserPos pos) {
        super(pos);
        this.constraintName = constraintName;
        this.uniqueSpec = uniqueSpec;
        this.columns = columns;
        this.enforcement = enforcement;
        this.isTableConstraint = isTableConstraint;
    }

    @Override
    public SqlOperator getOperator() {
        return OPERATOR;
    }

    /**
     * Returns whether the constraint is UNIQUE.
     */
    public boolean isUnique() {
        return this.uniqueSpec.getValueAs(SqlUniqueSpec.class) == SqlUniqueSpec.UNIQUE;
    }

    /**
     * Returns whether the constraint is PRIMARY KEY.
     */
    public boolean isPrimaryKey() {
        return this.uniqueSpec.getValueAs(SqlUniqueSpec.class) == SqlUniqueSpec.PRIMARY_KEY;
    }

    /**
     * Returns whether the constraint is enforced.
     */
    public boolean isEnforced() {
        // Default is enforced.
        return this.enforcement == null
                || this.enforcement.getValueAs(SqlConstraintEnforcement.class)
                == SqlConstraintEnforcement.ENFORCED;
    }

    public Optional<String> getConstraintName() {
        String ret = constraintName != null ? constraintName.getSimple() : null;
        return Optional.ofNullable(ret);
    }

    public Optional<SqlIdentifier> getConstraintNameIdentifier() {
        return Optional.ofNullable(constraintName);
    }

    public SqlNodeList getColumns() {
        return columns;
    }

    public boolean isTableConstraint() {
        return isTableConstraint;
    }

    /**
     * Returns the columns as a string array.
     */
    public String[] getColumnNames() {
        return columns.getList().stream()
                .map(col -> ((SqlIdentifier) col).getSimple())
                .toArray(String[]::new);
    }

    @Override
    public List<SqlNode> getOperandList() {
        return ImmutableNullableList.of(constraintName, uniqueSpec, columns, enforcement);
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        if (this.constraintName != null) {
            writer.keyword("CONSTRAINT");
            this.constraintName.unparse(writer, leftPrec, rightPrec);
        }
        this.uniqueSpec.unparse(writer, leftPrec, rightPrec);
        if (isTableConstraint) {
            SqlWriter.Frame frame = writer.startList("(", ")");
            for (SqlNode column : this.columns) {
                writer.sep(",", false);
                column.unparse(writer, leftPrec, rightPrec);
            }
            writer.endList(frame);
        }
        if (this.enforcement != null) {
            this.enforcement.unparse(writer, leftPrec, rightPrec);
        }
    }

}
