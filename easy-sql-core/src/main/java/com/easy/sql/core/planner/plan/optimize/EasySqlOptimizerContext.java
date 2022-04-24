package com.easy.sql.core.planner.plan.optimize;

import org.apache.calcite.plan.Context;

/**
 * @author zhangap
 * @version 1.0, 2022/4/24
 */
public interface EasySqlOptimizerContext extends Context {

    boolean isUpdateBeforeRequired();

    @Override
    default <C> C unwrap(Class<C> clazz) {
        return clazz.isInstance(this) ? clazz.cast(this) : null;
    }
}
