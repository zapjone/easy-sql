package com.easy.sql.core.exceptions;

/**
 * @author zhangap
 * @version 1.0, 2022/4/20
 */
public class SqlParserException extends RuntimeException {

    public SqlParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlParserException(String message) {
        super(message);
    }
}
