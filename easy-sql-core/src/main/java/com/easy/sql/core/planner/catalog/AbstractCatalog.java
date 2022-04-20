package com.easy.sql.core.planner.catalog;

import com.easy.sql.core.util.StringUtils;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author zhangap
 * @version 1.0, 2022/4/20
 */
public abstract class AbstractCatalog implements Catalog {
    private final String catalogName;
    private final String defaultDatabase;

    public AbstractCatalog(String name, String defaultDatabase) {
        checkArgument(!StringUtils.isNullOrWhitespaceOnly(name), "name cannot be null or empty");
        checkArgument(
                !StringUtils.isNullOrWhitespaceOnly(defaultDatabase),
                "defaultDatabase cannot be null or empty");

        this.catalogName = name;
        this.defaultDatabase = defaultDatabase;
    }

    public String getName() {
        return catalogName;
    }

    @Override
    public String getDefaultDatabase() {
        return defaultDatabase;
    }
}
