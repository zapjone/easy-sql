package com.easy.sql.core.exceptions;

import com.easy.sql.core.planner.catalog.ObjectPath;

/** Exception for trying to operate on a function that doesn't exist. */
public class FunctionNotExistException extends Exception {

    private static final String MSG = "Function %s does not exist in Catalog %s.";

    public FunctionNotExistException(String catalogName, ObjectPath functionPath) {
        this(catalogName, functionPath, null);
    }

    public FunctionNotExistException(String catalogName, ObjectPath functionPath, Throwable cause) {
        super(String.format(MSG, functionPath.getFullName(), catalogName), cause);
    }
}
