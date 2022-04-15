package com.easy.sql.core.planner.catalog;

import org.apache.calcite.linq4j.tree.Expression;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.Schemas;
import org.apache.calcite.schema.Table;

import java.util.HashSet;
import java.util.Set;

/**
 * CatalogCalciteSchema
 *
 * @author zap
 * @version 1.0, 2022/04/15
 */
public class CatalogCalciteSchema extends EasySqlSchema {

    private final String catalogName;
    private final CatalogManager catalogManager;

    public CatalogCalciteSchema(
            String catalogName, CatalogManager catalog) {
        this.catalogName = catalogName;
        this.catalogManager = catalog;
    }

    @Override
    public Schema getSubSchema(String schemaName) {
        if (catalogManager.schemaExists(catalogName, schemaName)) {
            return new DatabaseCalciteSchema(
                    schemaName, catalogName, catalogManager, isStreamingMode);
        } else {
            return null;
        }
    }

    @Override
    public Set<String> getSubSchemaNames() {
        return catalogManager.listSchemas(catalogName);
    }

    @Override
    public Table getTable(String name) {
        return null;
    }

    @Override
    public Set<String> getTableNames() {
        return new HashSet<>();
    }

    @Override
    public Expression getExpression(SchemaPlus parentSchema, String name) {
        return Schemas.subSchemaExpression(parentSchema, name, getClass());
    }

    @Override
    public boolean isMutable() {
        return true;
    }
}
