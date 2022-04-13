package com.easy.sql.parser.ddl;

import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.SqlSpecialOperator;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;

/**
 * Abstract class to describe ALTER VIEW statements.
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public abstract class SqlAlterView extends SqlCall {

    public static final SqlSpecialOperator OPERATOR =
            new SqlSpecialOperator("ALTER VIEW", SqlKind.ALTER_VIEW);

    protected final SqlIdentifier viewIdentifier;

    public SqlAlterView(SqlParserPos pos, SqlIdentifier viewIdentifier) {
        super(pos);
        this.viewIdentifier = viewIdentifier;
    }

    public SqlIdentifier getViewIdentifier() {
        return viewIdentifier;
    }

    @Override
    public SqlOperator getOperator() {
        return OPERATOR;
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword("ALTER VIEW");
        viewIdentifier.unparse(writer, leftPrec, rightPrec);
    }

    public String[] fullViewName() {
        return viewIdentifier.names.toArray(new String[0]);
    }

}
