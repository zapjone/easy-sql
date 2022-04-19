package com.easy.sql.core.planner.catalog;

import java.util.Map;
import java.util.Optional;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public interface CatalogDatabase {

    /**
     * 数据库的属性
     */
    Map<String, String> getProperties();

    /**
     * 描述
     */
    String getComment();

    CatalogDatabase copy();

    CatalogDatabase copy(Map<String, String> properties);

    Optional<String> getDescription();

    Optional<String> getDetailedDescription();
}
