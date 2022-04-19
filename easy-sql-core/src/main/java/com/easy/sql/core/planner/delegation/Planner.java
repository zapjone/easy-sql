package com.easy.sql.core.planner.delegation;

import com.easy.sql.core.dag.Information;
import org.apache.calcite.sql.SqlNode;

import java.util.List;

/**
 * Easy-sql的planner接口
 *
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public interface Planner {

    /**
     * 获取解析器
     */
    Parser getParser();

    /**
     * 将SqlNode转换为Information
     */
    List<Information> translate(List<SqlNode> sqlNode);
}
