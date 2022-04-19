package com.easy.sql.core.exceptions;


import com.easy.sql.core.planner.catalog.CatalogPartitionSpec;
import com.easy.sql.core.planner.catalog.ObjectPath;

/**
 * Exception for operation on a partition that doesn't exist. The cause includes non-existent table,
 * non-partitioned table, invalid partition spec, etc.
 */
public class PartitionNotExistException extends Exception {
    private static final String MSG = "Partition %s of table %s in catalog %s does not exist.";

    public PartitionNotExistException(
            String catalogName, ObjectPath tablePath, CatalogPartitionSpec partitionSpec) {

        super(String.format(MSG, partitionSpec, tablePath.getFullName(), catalogName), null);
    }

    public PartitionNotExistException(
            String catalogName,
            ObjectPath tablePath,
            CatalogPartitionSpec partitionSpec,
            Throwable cause) {

        super(String.format(MSG, partitionSpec, tablePath.getFullName(), catalogName), cause);
    }
}
