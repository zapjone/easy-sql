package com.easy.sql.core.planner.service;

import com.easy.sql.core.configuration.EasySqlConfig;

/**
 * @author zhangap
 * @version 1.0, 2022/4/20
 */
public class Settings {

    private final EasySqlConfig config;
    private final String defaultCatalog;
    private final String defaultDatabase;

    private Settings(EasySqlConfig config,
                     String defaultCatalog, String defaultDatabase) {
        this.config = config;
        this.defaultCatalog = defaultCatalog;
        this.defaultDatabase = defaultDatabase;
    }

    public String getCatalogName() {
        return this.defaultCatalog;
    }

    public String getDatabaseName() {
        return this.defaultDatabase;
    }

    public EasySqlConfig getConfig() {
        return this.config;
    }

    public static class Builder {
        private String catalogName = "default_catalog";
        private String databaseName = "default_database";
        private EasySqlConfig config;

        public Builder withConfig(EasySqlConfig config) {
            this.config = config;
            return this;
        }

        public Builder withCatalogName(String name) {
            this.catalogName = name;
            return this;
        }

        public Builder withDatabaseName(String databaseName) {
            this.databaseName = databaseName;
            return this;
        }

        public Settings build() {
            return new Settings(config, catalogName, databaseName);
        }

    }

}
