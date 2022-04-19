package com.easy.sql.core.exceptions;

import com.easy.sql.core.planner.catalog.ObjectPath;

/** Exception for trying to create a function that already exists. */
public class FunctionAlreadyExistException extends Exception {

    private static final String MSG = "Function %s already exists in Catalog %s.";

    public FunctionAlreadyExistException(String catalogName, ObjectPath functionPath) {
        this(catalogName, functionPath, null);
    }

    public FunctionAlreadyExistException(
            String catalogName, ObjectPath functionPath, Throwable cause) {
        super(String.format(MSG, functionPath.getFullName(), catalogName), cause);
    }
}
