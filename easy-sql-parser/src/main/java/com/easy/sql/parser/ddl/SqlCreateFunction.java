package com.easy.sql.parser.ddl;

import org.apache.calcite.sql.SqlCharStringLiteral;
import org.apache.calcite.sql.SqlCreate;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.SqlSpecialOperator;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.util.ImmutableNullableList;

import javax.annotation.Nonnull;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * CREATE FUNCTION DDL sql call.
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlCreateFunction extends SqlCreate {


    public static final SqlSpecialOperator OPERATOR =
            new SqlSpecialOperator("CREATE FUNCTION", SqlKind.CREATE_FUNCTION);

    private final SqlIdentifier functionIdentifier;

    private final SqlCharStringLiteral functionClassName;

    private final String functionLanguage;

    private final boolean isTemporary;

    private final boolean isSystemFunction;

    public SqlCreateFunction(
            SqlParserPos pos,
            SqlIdentifier functionIdentifier,
            SqlCharStringLiteral functionClassName,
            String functionLanguage,
            boolean ifNotExists,
            boolean isTemporary,
            boolean isSystemFunction) {
        super(OPERATOR, pos, false, ifNotExists);
        this.functionIdentifier = requireNonNull(functionIdentifier);
        this.functionClassName = requireNonNull(functionClassName);
        this.isSystemFunction = isSystemFunction;
        this.isTemporary = isTemporary;
        this.functionLanguage = functionLanguage;
    }

    @Override
    public SqlOperator getOperator() {
        return OPERATOR;
    }

    @Nonnull
    @Override
    public List<SqlNode> getOperandList() {
        return ImmutableNullableList.of(functionIdentifier, functionClassName);
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword("CREATE");
        if (isTemporary) {
            writer.keyword("TEMPORARY");
        }
        if (isSystemFunction) {
            writer.keyword("SYSTEM");
        }
        writer.keyword("FUNCTION");
        if (ifNotExists) {
            writer.keyword("IF NOT EXISTS");
        }
        functionIdentifier.unparse(writer, leftPrec, rightPrec);
        writer.keyword("AS");
        functionClassName.unparse(writer, leftPrec, rightPrec);
        if (functionLanguage != null) {
            writer.keyword("LANGUAGE");
            writer.keyword(functionLanguage);
        }
    }

    public boolean isIfNotExists() {
        return ifNotExists;
    }

    public boolean isSystemFunction() {
        return isSystemFunction;
    }

    public boolean isTemporary() {
        return isTemporary;
    }

    public SqlCharStringLiteral getFunctionClassName() {
        return this.functionClassName;
    }

    public String getFunctionLanguage() {
        return this.functionLanguage;
    }

    public String[] getFunctionIdentifier() {
        return functionIdentifier.names.toArray(new String[0]);
    }

}
