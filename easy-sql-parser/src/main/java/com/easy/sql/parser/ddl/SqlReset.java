package com.easy.sql.parser.ddl;

import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.SqlSpecialOperator;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.util.ImmutableNullableList;
import org.apache.calcite.util.NlsString;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * SQL call for "RESET" and "RESET 'key'".
 *
 * @author zhangap
 * @version 1.0, 2022/4/14
 */
public class SqlReset extends SqlCall {
    public static final SqlSpecialOperator OPERATOR =
            new SqlSpecialOperator("RESET", SqlKind.OTHER);

    @Nullable
    private final SqlNode key;

    public SqlReset(SqlParserPos pos, @Nullable SqlNode key) {
        super(pos);
        this.key = key;
    }

    @Override
    @Nonnull
    public SqlOperator getOperator() {
        return OPERATOR;
    }

    @Override
    @Nonnull
    public List<SqlNode> getOperandList() {
        return ImmutableNullableList.of(key);
    }

    public @Nullable SqlNode getKey() {
        return key;
    }

    public @Nullable String getKeyString() {
        if (key == null) {
            return null;
        }

        return ((NlsString) SqlLiteral.value(key)).getValue();
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword("RESET");
        if (key != null) {
            key.unparse(writer, leftPrec, rightPrec);
        }
    }
}
