package com.easy.sql.parser.ddl;

import org.apache.calcite.sql.SqlDrop;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlSpecialOperator;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.util.ImmutableNullableList;

import javax.annotation.Nonnull;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * DROP FUNCTION DDL sql call.
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlDropFunction extends SqlDrop {

    public static final SqlSpecialOperator OPERATOR =
            new SqlSpecialOperator("DROP FUNCTION", SqlKind.DROP_FUNCTION);

    private final SqlIdentifier functionIdentifier;

    private final boolean isTemporary;

    private final boolean isSystemFunction;

    public SqlDropFunction(
            SqlParserPos pos,
            SqlIdentifier functionIdentifier,
            boolean ifExists,
            boolean isTemporary,
            boolean isSystemFunction) {
        super(OPERATOR, pos, ifExists);
        this.functionIdentifier = requireNonNull(functionIdentifier);
        this.isSystemFunction = isSystemFunction;
        this.isTemporary = isTemporary;
    }

    @Nonnull
    @Override
    public List<SqlNode> getOperandList() {
        return ImmutableNullableList.of(functionIdentifier);
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword("DROP");
        if (isTemporary) {
            writer.keyword("TEMPORARY");
        }
        if (isSystemFunction) {
            writer.keyword("SYSTEM");
        }
        writer.keyword("FUNCTION");
        if (ifExists) {
            writer.keyword("IF EXISTS");
        }
        functionIdentifier.unparse(writer, leftPrec, rightPrec);
    }

    public String[] getFunctionIdentifier() {
        return functionIdentifier.names.toArray(new String[0]);
    }

    public boolean isTemporary() {
        return isTemporary;
    }

    public boolean isSystemFunction() {
        return isSystemFunction;
    }

    public boolean getIfExists() {
        return this.ifExists;
    }
}
