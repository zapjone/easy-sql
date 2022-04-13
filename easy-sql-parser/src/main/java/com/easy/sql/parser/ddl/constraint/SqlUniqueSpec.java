package com.easy.sql.parser.ddl.constraint;

import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.parser.SqlParserPos;

/**
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public enum SqlUniqueSpec {

    PRIMARY_KEY("PRIMARY KEY"),
    UNIQUE("UNIQUE");

    private final String digest;

    SqlUniqueSpec(String digest) {
        this.digest = digest;
    }

    @Override
    public String toString() {
        return this.digest;
    }

    public SqlLiteral symbol(SqlParserPos pos) {
        return SqlLiteral.createSymbol(this, pos);
    }

}
