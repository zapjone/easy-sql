package com.easy.sql.core.planner.delegation;

import org.apache.calcite.rel.RelNode;
import org.apache.calcite.sql.SqlNode;

/**
 * easy-sql的sql解析器
 *
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public interface Parser {

    /**
     * 解析sql语句
     */
    SqlNode parse(String statement);

    /**
     * 转换为逻辑计划
     *
     * @param sqlNode 解析的SqlNode
     * @return 转换后的逻辑计划，RelNode
     */
    RelNode rel(SqlNode sqlNode);

    /**
     *
     */
    String[] getCompletionHints(String statement, int position);

}
