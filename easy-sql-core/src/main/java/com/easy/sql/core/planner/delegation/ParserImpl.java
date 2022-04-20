package com.easy.sql.core.planner.delegation;

import com.easy.sql.core.planner.calcite.EasySqlPlannerImpl;
import com.easy.sql.core.planner.catalog.CatalogManager;
import com.easy.sql.core.planner.parser.CalciteParser;
import org.apache.calcite.sql.SqlNode;

import java.util.function.Supplier;

/**
 * @author zhangap
 * @version 1.0, 2022/4/20
 */
public class ParserImpl implements Parser {

    private final CatalogManager catalogManager;
    private final Supplier<EasySqlPlannerImpl> validatorSupplier;
    private final Supplier<CalciteParser> calciteParserSupplier;

    public ParserImpl(CatalogManager catalogManager,
                      Supplier<EasySqlPlannerImpl> validatorSupplier,
                      Supplier<CalciteParser> calciteParserSupplier) {
        this.catalogManager = catalogManager;
        this.validatorSupplier = validatorSupplier;
        this.calciteParserSupplier = calciteParserSupplier;
    }

    /**
     * 解析并进行校验
     */
    @Override
    public SqlNode parse(String statement) {
        CalciteParser parser = calciteParserSupplier.get();
        EasySqlPlannerImpl plannerImpl = validatorSupplier.get();

        SqlNode parsed = parser.parse(statement);

        // 校验
        return plannerImpl.validate(parsed);
    }

    @Override
    public String[] getCompletionHints(String statement, int position) {
        return new String[0];
    }
}
