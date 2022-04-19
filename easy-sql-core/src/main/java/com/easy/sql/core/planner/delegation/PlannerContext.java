package com.easy.sql.core.planner.delegation;

import com.easy.sql.core.common.SqlDialect;
import com.easy.sql.core.configuration.EasySqlConfig;
import com.easy.sql.parser.validate.EasySqlConformance;
import org.apache.calcite.config.Lex;
import org.apache.calcite.jdbc.CalciteSchema;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.validate.SqlConformance;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class PlannerContext {

    private final EasySqlConfig config;
    private final CalciteSchema rootSchema;
    private final FrameworkConfig frameworkConfig;

    public PlannerContext(EasySqlConfig config,
                          CalciteSchema rootSchema) {
        this.config = config;
        this.rootSchema = rootSchema;
        this.frameworkConfig = createFrameworkConfig();
    }

    /**
     * 创建Calcite的FrameworkConfig
     */
    public FrameworkConfig createFrameworkConfig() {
        return Frameworks.newConfigBuilder()
                .defaultSchema(rootSchema.plus())
                .parserConfig(getSqlParserConfig())
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
