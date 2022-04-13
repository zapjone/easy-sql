package com.easy.sql.parser.ddl;

import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.util.ImmutableNullableList;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * ALTER DDL to change properties of a view.
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlAlterViewProperties extends SqlAlterView {

    private final SqlNodeList propertyList;

    public SqlAlterViewProperties(
            SqlParserPos pos, SqlIdentifier viewName, SqlNodeList propertyList) {
        super(pos, viewName);
        this.propertyList = requireNonNull(propertyList, "propertyList should not be null");
    }

    @Override
    public List<SqlNode> getOperandList() {
        return ImmutableNullableList.of(viewIdentifier, propertyList);
    }

    public SqlNodeList getPropertyList() {
        return propertyList;
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        super.unparse(writer, leftPrec, rightPrec);
        writer.keyword("SET");
        SqlWriter.Frame withFrame = writer.startList("(", ")");
        for (SqlNode property : propertyList) {
            printIndent(writer);
            property.unparse(writer, leftPrec, rightPrec);
        }
        writer.newlineAndIndent();
        writer.endList(withFrame);
    }

    protected void printIndent(SqlWriter writer) {
        writer.sep(",", false);
        writer.newlineAndIndent();
        writer.print("  ");
    }

}
