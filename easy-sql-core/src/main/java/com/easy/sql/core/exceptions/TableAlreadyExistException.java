package com.easy.sql.core.exceptions;

import com.easy.sql.core.planner.catalog.ObjectPath;

/** Exception for trying to create a table (or view) that already exists. */
public class TableAlreadyExistException extends Exception {

    private static final String MSG = "Table (or view) %s already exists in Catalog %s.";

    public TableAlreadyExistException(String catalogName, ObjectPath tablePath) {
        this(catalogName, tablePath, null);
    }

    public TableAlreadyExistException(String catalogName, ObjectPath tablePath, Throwable cause) {
        super(String.format(MSG, tablePath.getFullName(), catalogName), cause);
    }
}
