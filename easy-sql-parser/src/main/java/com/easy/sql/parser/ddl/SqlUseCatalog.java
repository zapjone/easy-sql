package com.easy.sql.parser.ddl;

import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.SqlSpecialOperator;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;

import java.util.Collections;
import java.util.List;

/**
 * USE CATALOG sql call.
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlUseCatalog extends SqlCall {
    public static final SqlSpecialOperator OPERATOR =
            new SqlSpecialOperator("USE CATALOG", SqlKind.OTHER_DDL);
    private final SqlIdentifier catalogName;

    public SqlUseCatalog(SqlParserPos pos, SqlIdentifier catalogName) {
        super(pos);
        this.catalogName = catalogName;
    }

    @Override
    public SqlOperator getOperator() {
        return OPERATOR;
    }

    @Override
    public List<SqlNode> getOperandList() {
        return Collections.singletonList(catalogName);
    }

    public SqlIdentifier getCatalogName() {
        return catalogName;
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword("USE CATALOG");
        catalogName.unparse(writer, leftPrec, rightPrec);
    }

    public String catalogName() {
        return catalogName.getSimple();
    }
}