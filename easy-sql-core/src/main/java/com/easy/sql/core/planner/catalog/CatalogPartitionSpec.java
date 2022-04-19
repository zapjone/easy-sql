package com.easy.sql.core.planner.catalog;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class CatalogPartitionSpec {
    private final Map<String, String> partitionSpec;

    public CatalogPartitionSpec(Map<String, String> partitionSpec) {
        checkNotNull(partitionSpec, "partitionSpec cannot be null");

        this.partitionSpec = Collections.unmodifiableMap(partitionSpec);
    }

    public Map<String, String> getPartitionSpec() {
        return partitionSpec;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CatalogPartitionSpec that = (CatalogPartitionSpec) o;
        return partitionSpec.equals(that.partitionSpec);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partitionSpec);
    }

    @Override
    public String toString() {
        return "CatalogPartitionSpec{" + partitionSpec + '}';
    }
}
