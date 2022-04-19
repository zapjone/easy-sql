package com.easy.sql.core.exceptions;

import com.easy.sql.core.planner.catalog.CatalogPartitionSpec;
import com.easy.sql.core.planner.catalog.ObjectPath;

/** Exception for trying to create a partition that already exists. */
public class PartitionAlreadyExistsException extends Exception {
    private static final String MSG = "Partition %s of table %s in catalog %s already exists.";

    public PartitionAlreadyExistsException(
            String catalogName, ObjectPath tablePath, CatalogPartitionSpec partitionSpec) {

        super(String.format(MSG, partitionSpec, tablePath.getFullName(), catalogName));
    }

    public PartitionAlreadyExistsException(
            String catalogName,
            ObjectPath tablePath,
            CatalogPartitionSpec partitionSpec,
            Throwable cause) {

        super(String.format(MSG, partitionSpec, tablePath.getFullName(), catalogName), cause);
    }
}
