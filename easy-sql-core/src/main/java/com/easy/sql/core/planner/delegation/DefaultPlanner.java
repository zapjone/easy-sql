package com.easy.sql.core.planner.delegation;

import com.easy.sql.core.common.SqlDialect;
import com.easy.sql.core.configuration.EasySqlConfig;
import com.easy.sql.core.dag.Information;
import com.easy.sql.core.factories.FactoryUtil;
import com.easy.sql.core.planner.catalog.CatalogManager;
import com.easy.sql.core.planner.catalog.CatalogManagerCalciteSchema;
import com.easy.sql.core.planner.catalog.FunctionCatalog;
import org.apache.calcite.sql.SqlNode;

import java.util.ArrayList;
import java.util.List;

import static org.apache.calcite.jdbc.CalciteSchemaBuilder.asRootSchema;

/**
 * 默认实现的Planner
 *
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class DefaultPlanner implements Planner {

    private final EasySqlConfig config;
    private final FunctionCatalog functionCatalog;
    private final CatalogManager catalogManager;
    private Parser parser;
    private PlannerContext plannerContext;
    private SqlDialect currentDialect;

    public DefaultPlanner(EasySqlConfig config,
                          FunctionCatalog functionCatalog,
                          CatalogManager catalogManager) {
        this.config = config;
        this.functionCatalog = functionCatalog;
        this.catalogManager = catalogManager;
        this.currentDialect = config.getSqlDialect();
    }

    @Override
    public Parser getParser() {
        SqlDialect configDialect = config.getSqlDialect();
        if (null == parser || configDialect != currentDialect) {
            // 创建解析器
            this.parser = this.createNewParser(configDialect);
            this.currentDialect = configDialect;
        }
        return parser;
    }

    @Override
    public List<Information> translate(List<SqlNode> sqlNode) {
        if (sqlNode.isEmpty()) {
            return new ArrayList<>();
        }

        return null;
    }

    private Parser createNewParser(SqlDialect dialect) {
        String factoryIdentifier = dialect.name().toLowerCase();
        // 利用spi进行加载工厂，并使用工厂进行创建
        ParserFactory factory = FactoryUtil.discoverFactory(Thread.currentThread().getContextClassLoader(),
                ParserFactory.class, factoryIdentifier);
        ParserFactory.DefaultParserContext context = new ParserFactory.DefaultParserContext(catalogManager,
                this.getPlannerContext());

        // 创建解析器，用于解析Sql语句
        return factory.create(context);
    }

    /**
     * 获取PlannerContext，如果未初始化，则进行创建
     */
    private PlannerContext getPlannerContext() {
        if (null == plannerContext) {
            this.plannerContext = new PlannerContext(config, functionCatalog, catalogManager,
                    asRootSchema(new CatalogManagerCalciteSchema(catalogManager)),
                    getTraitDefs()
            );
        }
        return this.plannerContext;
    }
}
