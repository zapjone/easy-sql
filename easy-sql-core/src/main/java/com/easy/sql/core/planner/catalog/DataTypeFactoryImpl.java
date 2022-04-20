package com.easy.sql.core.planner.catalog;

import com.easy.sql.core.configuration.EasySqlConfig;

/**
 * @author zhangap
 * @version 1.0, 2022/4/20
 */
public class DataTypeFactoryImpl implements DataTypeFactory {

    private final ClassLoader classLoader;

    private EasySqlConfig config;

    public DataTypeFactoryImpl(ClassLoader classLoader,
                               EasySqlConfig config) {
        this.classLoader = classLoader;
        this.config = config;
    }

}
