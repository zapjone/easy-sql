package com.easy.sql.core.planner.delegation;

import com.easy.sql.core.common.SqlDialect;
import com.easy.sql.core.configuration.EasySqlConfig;
import com.easy.sql.core.planner.calcite.EasyContext;
import com.easy.sql.core.planner.calcite.EasySqlCostFactory;
import com.easy.sql.core.planner.calcite.EasySqlRelOptClusterFactory;
import com.easy.sql.core.planner.calcite.EasySqlRexBuilder;
import com.easy.sql.core.planner.calcite.EasySqlTypeFactory;
import com.easy.sql.core.planner.calcite.EasySqlTypeSystem;
import com.easy.sql.core.planner.catalog.CatalogManager;
import com.easy.sql.parser.validate.EasySqlConformance;
import org.apache.calcite.config.Lex;
import org.apache.calcite.jdbc.CalciteSchema;
import org.apache.calcite.plan.Context;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelTraitDef;
import org.apache.calcite.plan.volcano.VolcanoPlanner;
import org.apache.calcite.rel.hint.HintPredicates;
import org.apache.calcite.rel.hint.HintStrategy;
import org.apache.calcite.rel.hint.HintStrategyTable;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelDataTypeSystem;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.validate.SqlConformance;
import org.apache.calcite.sql2rel.SqlToRelConverter;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.util.Litmus;

import java.util.List;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class PlannerContext {

    private final RelDataTypeSystem typeSystem = new EasySqlTypeSystem();
    private final RelDataTypeFactory typeFactory = new EasySqlTypeFactory(typeSystem);
    private final EasySqlConfig config;
    private final RelOptCluster cluster;
    private final Context context;
    private final CalciteSchema rootSchema;
    private final List<RelTraitDef> traitDefs;
    private final FrameworkConfig frameworkConfig;

    public PlannerContext(EasySqlConfig config,
                          CatalogManager catalogManager,
                          CalciteSchema rootSchema,
                          List<RelTraitDef> traitDefs) {
        this.config = config;

        this.context = new EasyContext(config, catalogManager);

        this.rootSchema = rootSchema;
        this.traitDefs = traitDefs;

        this.frameworkConfig = createFrameworkConfig();

        // CBO优化器
        RelOptPlanner planner = new VolcanoPlanner(frameworkConfig.getCostFactory(), frameworkConfig.getContext());
        for (RelTraitDef traitDef : frameworkConfig.getTraitDefs()) {
            planner.addRelTraitDef(traitDef);
        }
        this.cluster = EasySqlRelOptClusterFactory.create(planner, new EasySqlRexBuilder(typeFactory));
    }

    /**
     * 创建Calcite的FrameworkConfig
     */
    public FrameworkConfig createFrameworkConfig() {
        return Frameworks.newConfigBuilder()
                .defaultSchema(rootSchema.plus())
                .parserConfig(getSqlParserConfig())
                .costFactory(new EasySqlCostFactory())
                .typeSystem(typeSystem)
                .sqlToRelConverterConfig(getSqlToRelConverterConfig())
                .context(context)
                .traitDefs(traitDefs)
                .build();
    }

    private SqlToRelConverter.Config getSqlToRelConverterConfig() {
        return SqlToRelConverter.config()
                .withTrimUnusedFields(false)
                .withHintStrategyTable(createHintStrategyTable())
                .withInSubQueryThreshold(Integer.MAX_VALUE)
                .withExpand(false);
        //.withRelBuilderFactory()
    }

    private HintStrategyTable createHintStrategyTable() {
        return HintStrategyTable.builder()
                .errorHandler(Litmus.THROW)
                .hintStrategy("OPTIONS",
                        HintStrategy.builder(HintPredicates.TABLE_SCAN)
                                .optionChecker((hint, errorHandler) ->
                                        errorHandler.check(hint.kvOptions.size() > 0,
                                                "Hint [{}] only support non empty key value options",
                                                hint.hintName))
                                .build())
                .build();
    }

    private SqlParser.Config getSqlParserConfig() {
        SqlConformance conformance = getSqlConformance();
        return SqlParser.config()
                .withParserFactory(EasySqlParserFactories.create(conformance))
                .withConformance(conformance)
                .withLex(Lex.JAVA)
                .withIdentifierMaxLength(256);
    }

    private SqlConformance getSqlConformance() {
        SqlDialect sqlDialect = config.getSqlDialect();
        if (sqlDialect == SqlDialect.DEFAULT) {
            return EasySqlConformance.DEFAULT;
        }
        throw new UnsupportedOperationException("Unsupported SQL dialect: " + sqlDialect);
    }

}
