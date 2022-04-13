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
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlDropCatalog extends SqlDrop {


    private static final SqlOperator OPERATOR =
            new SqlSpecialOperator("DROP CATALOG", SqlKind.OTHER_DDL);

    private final SqlIdentifier catalogName;
    private final boolean ifExists;

    public SqlDropCatalog(SqlParserPos pos, SqlIdentifier catalogName, boolean ifExists) {
        super(OPERATOR, pos, false);
        this.catalogName = catalogName;
        this.ifExists = ifExists;
    }

    @Override
    public List<SqlNode> getOperandList() {
        return ImmutableNullableList.of(catalogName);
    }

    public SqlIdentifier getCatalogName() {
        return catalogName;
    }

    public boolean getIfExists() {
        return this.ifExists;
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword("DROP");
        writer.keyword("CATALOG");
        if (ifExists) {
            writer.keyword("IF EXISTS");
        }
        catalogName.unparse(writer, leftPrec, rightPrec);
    }

    public String catalogName() {
        return catalogName.getSimple();
    }

}
