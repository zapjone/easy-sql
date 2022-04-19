package com.easy.sql.core.exceptions;

import com.easy.sql.core.planner.catalog.ObjectPath;

/** Exception for trying to operate non-partitioned on a partitioned table. */
public class TablePartitionedException extends Exception {

    private static final String MSG = "Table %s in catalog %s is partitioned.";

    public TablePartitionedException(String catalogName, ObjectPath tablePath) {
        this(catalogName, tablePath, null);
    }

    public TablePartitionedException(String catalogName, ObjectPath tablePath, Throwable cause) {
        super(String.format(MSG, tablePath.getFullName(), catalogName), cause);
    }
}
