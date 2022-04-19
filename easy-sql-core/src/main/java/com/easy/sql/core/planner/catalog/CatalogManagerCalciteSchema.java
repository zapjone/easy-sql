package com.easy.sql.core.planner.catalog;

import org.apache.calcite.linq4j.tree.Expression;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.Table;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class CatalogManagerCalciteSchema extends EasySqlSchema {

    private final CatalogManager catalogManager;

    public CatalogManagerCalciteSchema(CatalogManager catalogManager) {
        this.catalogManager = catalogManager;
    }


    @Override
    public Table getTable(String name) {
        return null;
    }

    @Override
    public Set<String> getTableNames() {
        return Collections.emptySet();
    }

    @Override
    public Schema getSubSchema(String name) {
        if (catalogManager.schemaExists(name)) {
            return new CatalogCalciteSchema(name, catalogManager);
        } else {
            return null;
        }
    }

    @Override
    public Set<String> getSubSchemaNames() {
        return new HashSet<>(catalogManager.listCatalogs());
    }

    @Override
    public Expression getExpression(SchemaPlus parentSchema, String name) {
        return null;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

}
