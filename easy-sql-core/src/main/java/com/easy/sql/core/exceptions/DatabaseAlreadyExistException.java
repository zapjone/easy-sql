package com.easy.sql.core.exceptions;

/** Exception for trying to create a database that already exists. */
public class DatabaseAlreadyExistException extends Exception {
    private static final String MSG = "Database %s already exists in Catalog %s.";

    public DatabaseAlreadyExistException(String catalog, String database, Throwable cause) {
        super(String.format(MSG, database, catalog), cause);
    }

    public DatabaseAlreadyExistException(String catalog, String database) {
        this(catalog, database, null);
    }
}
