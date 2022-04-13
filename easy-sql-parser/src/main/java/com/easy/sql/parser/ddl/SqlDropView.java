package com.easy.sql.parser.ddl;

import org.apache.calcite.sql.SqlDrop;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.SqlSpecialOperator;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.util.ImmutableNullableList;

import java.util.List;

/**
 * DROP VIEW DDL sql call.
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlDropView extends SqlDrop {

    private static final SqlOperator OPERATOR =
            new SqlSpecialOperator("DROP VIEW", SqlKind.DROP_VIEW);

    private final SqlIdentifier viewName;
    private final boolean isTemporary;

    public SqlDropView(
            SqlParserPos pos, SqlIdentifier viewName, boolean ifExists, boolean isTemporary) {
        super(OPERATOR, pos, ifExists);
        this.viewName = viewName;
        this.isTemporary = isTemporary;
    }

    @Override
    public List<SqlNode> getOperandList() {
        return ImmutableNullableList.of(viewName);
    }

    public SqlIdentifier getViewName() {
        return viewName;
    }

    public boolean getIfExists() {
        return this.ifExists;
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword("DROP");
        if (isTemporary) {
            writer.keyword("TEMPORARY");
        }
        writer.keyword("VIEW");
        if (ifExists) {
            writer.keyword("IF EXISTS");
        }
        viewName.unparse(writer, leftPrec, rightPrec);
    }

    public boolean isTemporary() {
        return isTemporary;
    }

    public String[] fullViewName() {
        return viewName.names.toArray(new String[0]);
    }

}
