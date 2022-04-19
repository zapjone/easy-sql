package com.easy.sql.core.planner.catalog;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.TemporalTable;
import org.apache.calcite.schema.impl.AbstractTable;

import javax.annotation.Nonnull;

/**
 * easy-sql中表的实现，在catalog中会使用Table来获取表信息
 *
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class CatalogSchemaTable extends AbstractTable implements TemporalTable {

    private final ObjectIdentifier tableIdentifier;

    public CatalogSchemaTable(
            ObjectIdentifier tableIdentifier) {
        this.tableIdentifier = tableIdentifier;
    }

    public ObjectIdentifier getTableIdentifier() {
        return tableIdentifier;
    }

    @Nonnull
    @Override
    public String getSysStartFieldName() {
        return "sys_start";
    }

    @Nonnull
    @Override
    public String getSysEndFieldName() {
        return "sys_end";
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
        return null;
    }
}
