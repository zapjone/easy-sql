package com.easy.sql.core.planner.delegation;

import com.easy.sql.core.channel.Operator;
import com.easy.sql.core.configuration.EasySqlConfig;
import org.apache.calcite.plan.ConventionTraitDef;
import org.apache.calcite.plan.RelTraitDef;
import org.apache.calcite.sql.SqlNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Easy-sql的planner接口
 *
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public interface Planner {

    /**
     * 配置信息
     */
    EasySqlConfig getConfig();

    /**
     * 获取解析器
     */
    Parser getParser();

    /**
     * 将SqlNode转换为Information
     */
    List<Operator> translate(List<SqlNode> sqlNode);

    /**
     * 获取优化规则列表，默认添加ConventionTraitDef规则
     */
    default List<RelTraitDef> getTraitDefs() {
        List<RelTraitDef> relTraitDefs = new ArrayList<>();
        relTraitDefs.add(ConventionTraitDef.INSTANCE);
        return relTraitDefs;
    }
}
