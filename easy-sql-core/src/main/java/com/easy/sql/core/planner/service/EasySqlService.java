package com.easy.sql.core.planner.service;

import com.easy.sql.core.configuration.EasySqlConfig;
import com.easy.sql.core.dag.Information;
import com.easy.sql.core.planner.catalog.CatalogManager;
import com.easy.sql.core.planner.catalog.DefaultMemoryCatalog;
import com.easy.sql.core.planner.catalog.FunctionCatalog;
import com.easy.sql.core.planner.delegation.DefaultPlanner;
import com.easy.sql.core.planner.delegation.Planner;
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

        // 转换为底层Information结构
        List<Information> informations = planner.translate(Collections.singletonList(validated));

        // 执行Information
        return null;
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

}
