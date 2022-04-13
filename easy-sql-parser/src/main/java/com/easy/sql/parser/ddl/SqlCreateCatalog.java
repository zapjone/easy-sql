package com.easy.sql.parser.ddl;

import org.apache.calcite.sql.SqlCreate;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.SqlSpecialOperator;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.util.ImmutableNullableList;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * CREATE CATALOG DDL sql call.
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlCreateCatalog extends SqlCreate {

    public static final SqlSpecialOperator OPERATOR =
            new SqlSpecialOperator("CREATE CATALOG", SqlKind.OTHER_DDL);

    private final SqlIdentifier catalogName;

    private final SqlNodeList propertyList;

    public SqlCreateCatalog(
            SqlParserPos position, SqlIdentifier catalogName, SqlNodeList propertyList) {
        super(OPERATOR, position, false, false);
        this.catalogName = requireNonNull(catalogName, "catalogName cannot be null");
        this.propertyList = requireNonNull(propertyList, "propertyList cannot be null");
    }

    @Override
    public SqlOperator getOperator() {
        return OPERATOR;
    }

    @Override
    public List<SqlNode> getOperandList() {
        return ImmutableNullableList.of(catalogName, propertyList);
    }

    public SqlIdentifier getCatalogName() {
        return catalogName;
    }

    public SqlNodeList getPropertyList() {
        return propertyList;
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword("CREATE CATALOG");
        catalogName.unparse(writer, leftPrec, rightPrec);

        if (this.propertyList.size() > 0) {
            writer.keyword("WITH");
            SqlWriter.Frame withFrame = writer.startList("(", ")");
            for (SqlNode property : propertyList) {
                printIndent(writer);
                property.unparse(writer, leftPrec, rightPrec);
            }
            writer.newlineAndIndent();
            writer.endList(withFrame);
        }
    }

    private void printIndent(SqlWriter writer) {
        writer.sep(",", false);
        writer.newlineAndIndent();
        writer.print("  ");
    }

    public String catalogName() {
        return catalogName.getSimple();
    }

}
