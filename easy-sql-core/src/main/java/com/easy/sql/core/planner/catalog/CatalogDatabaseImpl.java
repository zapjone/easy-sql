package com.easy.sql.core.planner.catalog;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 数据库实现
 *
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class CatalogDatabaseImpl implements CatalogDatabase {

    // Property of the database
    private final Map<String, String> properties;
    // Comment of the database
    private final String comment;

    public CatalogDatabaseImpl(Map<String, String> properties, @Nullable String comment) {
        this.properties = checkNotNull(properties, "properties cannot be null");
        this.comment = comment;
    }

    /**
     * Get a map of properties associated with the database.
     */
    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    /**
     * Get comment of the database.
     *
     * @return comment of the database
     */
    @Override
    public String getComment() {
        return comment;
    }

    /**
     * Get a deep copy of the CatalogDatabase instance.
     *
     * @return a copy of CatalogDatabase instance
     */
    @Override
    public CatalogDatabase copy() {
        return copy(getProperties());
    }

    @Override
    public CatalogDatabase copy(Map<String, String> properties) {
        return new CatalogDatabaseImpl(new HashMap<>(properties), comment);
    }

    /**
     * Get a brief description of the database.
     *
     * @return an optional short description of the database
     */
    @Override
    public Optional<String> getDescription() {
        return Optional.ofNullable(comment);
    }

    /**
     * Get a detailed description of the database.
     *
     * @return an optional long description of the database
     */
    @Override
    public Optional<String> getDetailedDescription() {
        return Optional.ofNullable(comment);
    }
}
