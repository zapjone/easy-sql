package com.easy.sql.core.planner.delegation;

import com.easy.sql.core.common.SqlDialect;
import com.easy.sql.core.configuration.ConfigOption;

import java.util.Collections;
import java.util.Set;

/**
 * 默认的Sql解析Factory
 *
 * @author zhangap
 * @version 1.0, 2022/4/20
 */
public class DefaultParserFactory implements ParserFactory {

    @Override
    public String factoryIdentifier() {
        return SqlDialect.DEFAULT.name().toLowerCase();
    }

    @Override
    public Set<ConfigOption<?>> requiredOptions() {
        return Collections.emptySet();
    }

    @Override
    public Set<ConfigOption<?>> optionalOptions() {
        return Collections.emptySet();
    }

    @Override
    public Parser create(Context context) {
        return new ParserImpl(context.getCatalogManager(),
                () -> context.getPlannerContext().createEasySqlPlanner(
                        context.getCatalogManager().getCurrentCatalog(),
                        context.getCatalogManager().getCurrentDatabase()),
                context.getPlannerContext()::createCalciteParser);
    }
}
