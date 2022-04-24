package com.easy.sql.core.planner.delegation;

import com.easy.sql.core.channel.Operator;
import com.easy.sql.core.common.SqlDialect;
import com.easy.sql.core.configuration.EasySqlConfig;
import com.easy.sql.core.factories.FactoryUtil;
import com.easy.sql.core.planner.catalog.CatalogManager;
import com.easy.sql.core.planner.catalog.CatalogManagerCalciteSchema;
import com.easy.sql.core.planner.catalog.FunctionCatalog;
import com.easy.sql.core.planner.plan.optimize.EasyOptimizer;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public EasySqlConfig getConfig() {
        return this.config;
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
    public List<Operator> translate(List<SqlNode> sqlNodes) {
        if (sqlNodes.isEmpty()) {
            return new ArrayList<>();
        }

        List<RelNode> relNodes = translateToRel(sqlNodes);
        // 对sqlNod进行优化
        List<RelNode> optimizedRelNodes = optimize(relNodes);

        return null;
    }

    /**
     * 将SqlNode列表转换为RelNode列表
     *
     * @param sqlNodes SqlNode列表
     * @return RelNode列表
     */
    public List<RelNode> translateToRel(List<SqlNode> sqlNodes) {
        return sqlNodes.stream().map(this::toRel).collect(Collectors.toList());
    }

    /**
     * 对SqlNode进行优化
     *
     * @param sqlNodes sqlNode列表
     * @return 优化后的RelNode列表
     */
    private List<RelNode> optimize(List<RelNode> sqlNodes) {
        return new EasyOptimizer(this).optimize(sqlNodes);
    }

    /**
     * 转换SqlNode
     */
    private RelNode toRel(SqlNode sqlNode) {
        if (sqlNode.getKind().belongsTo(SqlKind.QUERY)) {
            return parser.rel(sqlNode);
        }

        throw new UnsupportedOperationException("不支持的SqlNode类型:" + sqlNode.getKind().lowerName);
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
