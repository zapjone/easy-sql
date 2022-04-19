package com.easy.sql.core.exceptions;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(String message) {
        super(message);
    }
}
