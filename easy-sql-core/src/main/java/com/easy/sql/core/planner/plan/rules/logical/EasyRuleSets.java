package com.easy.sql.core.planner.plan.rules.logical;

import org.apache.calcite.tools.RuleSet;
import org.apache.calcite.tools.RuleSets;

/**
 * @author zhangap
 * @version 1.0, 2022/5/6
 */
public final class EasyRuleSets {

    public static final RuleSet TABLE_REF_RULES = RuleSets.ofList(EnumerableToLogicalTableScan.INSTANCE);

    /**
     * semi join 规则
     */
    public static final RuleSet SEMI_JOIN_RULES = RuleSets.ofList(SimplifyFilterConditionRule.EXTENDED);

}
