package com.easy.sql.core.planner.parser;

import com.easy.sql.core.exceptions.SqlParserException;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;

/**
 * @author zhangap
 * @version 1.0, 2022/4/20
 */
public class CalciteParser {
    private final SqlParser.Config config;

    public CalciteParser(SqlParser.Config config) {
        this.config = config;
    }

    /**
     * Parses a SQL statement into a {@link SqlNode}. The {@link SqlNode} is not yet validated.
     */
    public SqlNode parse(String sql) {
        try {
            SqlParser parser = SqlParser.create(sql, config);
            return parser.parseStmt();
        } catch (SqlParseException e) {
            throw new SqlParserException("SQL parse failed. " + e.getMessage(), e);
        }
    }

    /**
     * Parses a SQL expression into a {@link SqlNode}. The {@link SqlNode} is not yet validated.
     */
    public SqlNode parseExpression(String sqlExpression) {
        try {
            final SqlParser parser = SqlParser.create(sqlExpression, config);
            return parser.parseExpression();
        } catch (SqlParseException e) {
            throw new SqlParserException("SQL parse failed. " + e.getMessage(), e);
        }
    }

}
