package com.easy.sql.core.planner.catalog;

import org.apache.calcite.linq4j.tree.Expression;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.Schemas;
import org.apache.calcite.schema.Table;

import java.util.HashSet;
import java.util.Set;

/**
 * DatabaseCalciteSchema
 *
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class DatabaseCalciteSchema extends EasySqlSchema {
    private final String catalogName;
    private final String databaseName;
    private final CatalogManager catalogManager;

    public DatabaseCalciteSchema(
            String catalogName,
            String databaseName,
            CatalogManager catalog) {
        this.databaseName = databaseName;
        this.catalogName = catalogName;
        this.catalogManager = catalog;
    }


    @Override
    public Table getTable(String tableName) {
        final ObjectIdentifier identifier = ObjectIdentifier.of(catalogName, databaseName, tableName);
        return catalogManager.getTable(identifier);
    }


    @Override
    public Set<String> getTableNames() {
        return catalogManager.listTables(catalogName, databaseName);
    }

    @Override
    public Schema getSubSchema(String s) {
        return null;
    }

    @Override
    public Set<String> getSubSchemaNames() {
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
