package com.easy.sql.core.exceptions;

/** Exception for trying to operate on a database that doesn't exist. */
public class DatabaseNotExistException extends Exception {
    private static final String MSG = "Database %s does not exist in Catalog %s.";

    public DatabaseNotExistException(String catalogName, String databaseName, Throwable cause) {
        super(String.format(MSG, databaseName, catalogName), cause);
    }

    public DatabaseNotExistException(String catalogName, String databaseName) {
        this(catalogName, databaseName, null);
    }
}
