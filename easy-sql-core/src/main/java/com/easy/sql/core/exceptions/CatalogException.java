package com.easy.sql.core.exceptions;

/** A catalog-related, runtime exception. */
public class CatalogException extends RuntimeException {
    /** @param message the detail message. */
    public CatalogException(String message) {
        super(message);
    }

    /** @param cause the cause. */
    public CatalogException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message the detail message.
     * @param cause the cause.
     */
    public CatalogException(String message, Throwable cause) {
        super(message, cause);
    }
}
