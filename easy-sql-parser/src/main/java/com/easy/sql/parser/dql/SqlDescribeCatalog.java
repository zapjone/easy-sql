package com.easy.sql.parser.dql;

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
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlDescribeCatalog extends SqlCall {

    public static final SqlSpecialOperator OPERATOR =
            new SqlSpecialOperator("DESCRIBE CATALOG", SqlKind.OTHER);
    private final SqlIdentifier catalogName;

    public SqlDescribeCatalog(SqlParserPos pos, SqlIdentifier catalogName) {
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

    public String getCatalogName() {
        return catalogName.getSimple();
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword("DESCRIBE CATALOG");
        catalogName.unparse(writer, leftPrec, rightPrec);
    }

}
