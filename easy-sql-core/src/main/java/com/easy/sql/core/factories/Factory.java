package com.easy.sql.core.factories;

import com.easy.sql.core.configuration.ConfigOption;

import java.util.Set;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public interface Factory {

    /**
     * factory名称
     */
    String factoryIdentifier();

    /**
     * 必须的配置
     */
    Set<ConfigOption<?>> requiredOptions();

    /**
     * 可选配置
     */
    Set<ConfigOption<?>> optionalOptions();

}
