package com.easy.sql.parser.dml;

import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.parser.SqlParserPos;

/**
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public enum RichSqlInsertKeyword {

    /**
     * 覆盖
     */
    OVERWRITE;

    public SqlLiteral symbol(SqlParserPos pos) {
        return SqlLiteral.createSymbol(this, pos);
    }

}
