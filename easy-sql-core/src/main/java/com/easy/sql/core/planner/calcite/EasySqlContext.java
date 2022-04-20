package com.easy.sql.core.planner.calcite;

import com.easy.sql.core.configuration.EasySqlConfig;
import com.easy.sql.core.planner.catalog.CatalogManager;
import com.easy.sql.core.planner.catalog.FunctionCatalog;
import org.apache.calcite.plan.Context;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class EasySqlContext implements Context {

    private final EasySqlConfig config;
    private final FunctionCatalog functionCatalog;
    private final CatalogManager catalogManager;

    public EasySqlContext(EasySqlConfig config,
                          FunctionCatalog functionCatalog,
                          CatalogManager catalogManager) {
        this.config = config;
        this.functionCatalog = functionCatalog;
        this.catalogManager = catalogManager;
    }

    public FunctionCatalog getFunctionCatalog() {
        return this.functionCatalog;
    }

    public CatalogManager getCatalogManager() {
        return this.catalogManager;
    }

    @Override
    public <C> C unwrap(Class<C> clazz) {
        return clazz.isInstance(this) ? clazz.cast(this) : null;
    }
}
