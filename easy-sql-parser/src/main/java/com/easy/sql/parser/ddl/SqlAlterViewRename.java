package com.easy.sql.parser.ddl;

import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.util.ImmutableNullableList;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * ALTER DDL to rename a view.
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlAlterViewRename extends SqlAlterView {


    private final SqlIdentifier newViewIdentifier;

    public SqlAlterViewRename(
            SqlParserPos pos, SqlIdentifier viewIdentifier, SqlIdentifier newViewIdentifier) {
        super(pos, viewIdentifier);
        this.newViewIdentifier = newViewIdentifier;
    }

    @Nonnull
    @Override
    public List<SqlNode> getOperandList() {
        return ImmutableNullableList.of(viewIdentifier, newViewIdentifier);
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        super.unparse(writer, leftPrec, rightPrec);
        writer.keyword("RENAME TO");
        newViewIdentifier.unparse(writer, leftPrec, rightPrec);
    }

    public String[] fullNewViewName() {
        return newViewIdentifier.names.toArray(new String[0]);
    }

}
