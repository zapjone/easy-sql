package com.easy.sql.core.planner.service;

import com.easy.sql.core.configuration.EasySqlConfig;
import com.easy.sql.core.channel.Operator;
import com.easy.sql.core.planner.catalog.CatalogManager;
import com.easy.sql.core.planner.catalog.DefaultMemoryCatalog;
import com.easy.sql.core.planner.catalog.FunctionCatalog;
import com.easy.sql.core.planner.delegation.DefaultPlanner;
import com.easy.sql.core.planner.delegation.Planner;
import com.easy.sql.parser.ddl.SqlCreateCatalog;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;

import java.util.Collections;
import java.util.List;

/**
 * @author zhangap
 * @version 1.0, 2022/4/20
 */
public class EasySqlService {

    private final CatalogManager catalogManager;
    private final FunctionCatalog functionCatalog;
    private final EasySqlConfig config;
    private final Planner planner;

    public EasySqlService(CatalogManager catalogManager,
                          FunctionCatalog functionCatalog,
                          EasySqlConfig config) {
        this.catalogManager = catalogManager;
        this.functionCatalog = functionCatalog;
        this.config = config;

        this.planner = new DefaultPlanner(config, functionCatalog, catalogManager);
    }

    /**
     * SQL执行
     *
     * @param statement sql语句
     * @return SQL执行结果
     */
    public EasySqlResult executeSql(String statement) {
        // 解析sql并进行校验
        SqlNode validated = planner.getParser().parse(statement);

        return executeInternal(validated);
    }

    /**
     * 创建EasySqlService
     */
    public static EasySqlService create(Settings settings) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        CatalogManager catalogManager = CatalogManager.newBuilder()
                .classLoader(classLoader)
                .defaultCatalog(settings.getCatalogName(),
                        new DefaultMemoryCatalog(settings.getCatalogName(),
                                settings.getDatabaseName()))
                .config(settings.getConfig())
                .build();

        FunctionCatalog functionCatalog = new FunctionCatalog(catalogManager);

        return new EasySqlService(catalogManager,
                functionCatalog,
                settings.getConfig());
    }

    /**
     * 内置执行
     */
    private EasySqlResult executeInternal(SqlNode sqlNode) {
        // 根据Sql类型类型进行判断，只有sql查询需要进行优化和转换，其他的可以直接调用catalog进行执行
        if (sqlNode instanceof SqlCreateCatalog) {
            SqlCreateCatalog createCatalog = (SqlCreateCatalog) sqlNode;
            //catalogManager.registerCatalog();
            return EasySqlResult.RESULT_OK;
        } else if (sqlNode.getKind().belongsTo(SqlKind.QUERY)) {
            // 查询优化操作
            List<Operator> operators = planner.translate(Collections.singletonList(sqlNode));

            // 执行operators
            return executePlan(operators);
        } else {
            throw new UnsupportedOperationException("不支持的操作: " + sqlNode.getKind().lowerName);
        }
    }

    /**
     * 执行sql翻译后的执行组件列表
     */
    private EasySqlResult executePlan(List<Operator> operators) {
        /*TODO 当前还未写执行方式*/
        return null;
    }
}
