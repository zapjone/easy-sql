package com.easy.sql.core.planner.calcite;

import com.easy.sql.core.configuration.EasySqlConfig;
import com.easy.sql.core.planner.catalog.CatalogManager;
import org.apache.calcite.plan.Context;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class EasyContext implements Context {

    private final EasySqlConfig config;
    private final CatalogManager catalogManager;

    public EasyContext(EasySqlConfig config,
                       CatalogManager catalogManager) {
        this.config = config;
        this.catalogManager = catalogManager;
    }

    @Override
    public <C> C unwrap(Class<C> clazz) {
        return clazz.isInstance(this) ? clazz.cast(this) : null;
    }
}
