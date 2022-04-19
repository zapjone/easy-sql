package com.easy.sql.core.exceptions;

import com.easy.sql.core.planner.catalog.CatalogPartitionSpec;
import com.easy.sql.core.planner.catalog.ObjectPath;

import java.util.List;

/**
 */
public class PartitionSpecInvalidException extends Exception {
    private static final String MSG =
            "PartitionSpec %s does not match partition keys %s of table %s in catalog %s.";

    public PartitionSpecInvalidException(
            String catalogName,
            List<String> partitionKeys,
            ObjectPath tablePath,
            CatalogPartitionSpec partitionSpec) {

        super(
                String.format(
                        MSG, partitionSpec, partitionKeys, tablePath.getFullName(), catalogName),
                null);
    }

    public PartitionSpecInvalidException(
            String catalogName,
            List<String> partitionKeys,
            ObjectPath tablePath,
            CatalogPartitionSpec partitionSpec,
            Throwable cause) {

        super(
                String.format(
                        MSG, partitionSpec, partitionKeys, tablePath.getFullName(), catalogName),
                cause);
    }
}
