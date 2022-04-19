package com.easy.sql.core.exceptions;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class EasySqlException extends RuntimeException{

    private static final long serialVersionUID = 450688772469004724L;

    public EasySqlException(String message) {
        super(message);
    }

    public EasySqlException(Throwable cause) {
        super(cause);
    }

    public EasySqlException(String message, Throwable cause) {
        super(message, cause);
    }
}
