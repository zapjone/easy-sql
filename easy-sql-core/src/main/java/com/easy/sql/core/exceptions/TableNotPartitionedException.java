package com.easy.sql.core.exceptions;

import com.easy.sql.core.planner.catalog.ObjectPath;

/** Exception for trying to operate partition on a non-partitioned table. */
public class TableNotPartitionedException extends Exception {

    private static final String MSG = "Table %s in catalog %s is not partitioned.";

    public TableNotPartitionedException(String catalogName, ObjectPath tablePath) {
        this(catalogName, tablePath, null);
    }

    public TableNotPartitionedException(String catalogName, ObjectPath tablePath, Throwable cause) {
        super(String.format(MSG, tablePath.getFullName(), catalogName), cause);
    }
}
