package com.easy.sql.parser.ddl;

import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.util.ImmutableNullableList;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * ALTER DDL to change a view's query.
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlAlterViewAs extends SqlAlterView {


    private final SqlNode newQuery;

    public SqlAlterViewAs(SqlParserPos pos, SqlIdentifier viewIdentifier, SqlNode newQuery) {
        super(pos, viewIdentifier);
        this.newQuery = newQuery;
    }

    @Nonnull
    @Override
    public List<SqlNode> getOperandList() {
        return ImmutableNullableList.of(viewIdentifier, newQuery);
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        super.unparse(writer, leftPrec, rightPrec);
        writer.newlineAndIndent();
        writer.keyword("AS");
        writer.newlineAndIndent();
        newQuery.unparse(writer, leftPrec, rightPrec);
    }

    public SqlNode getNewQuery() {
        return newQuery;
    }

}
