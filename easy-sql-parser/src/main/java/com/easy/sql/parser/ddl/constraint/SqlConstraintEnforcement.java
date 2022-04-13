package com.easy.sql.parser.ddl.constraint;

import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.parser.SqlParserPos;

/**
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public enum SqlConstraintEnforcement {
    ENFORCED("ENFORCED"),
    NOT_ENFORCED("NOT ENFORCED");

    private final String digest;

    SqlConstraintEnforcement(String digest) {
        this.digest = digest;
    }

    @Override
    public String toString() {
        return digest;
    }

    public SqlLiteral symbol(SqlParserPos pos) {
        return SqlLiteral.createSymbol(this, pos);
    }
}
