package com.easy.sql.core.exceptions;

import com.easy.sql.core.planner.catalog.ObjectPath;

/** Exception for trying to operate on a table (or view) that doesn't exist. */
public class TableNotExistException extends Exception {

    private static final String MSG = "Table (or view) %s does not exist in Catalog %s.";

    public TableNotExistException(String catalogName, ObjectPath tablePath) {
        this(catalogName, tablePath, null);
    }

    public TableNotExistException(String catalogName, ObjectPath tablePath, Throwable cause) {
        super(String.format(MSG, tablePath.getFullName(), catalogName), cause);
    }
}
