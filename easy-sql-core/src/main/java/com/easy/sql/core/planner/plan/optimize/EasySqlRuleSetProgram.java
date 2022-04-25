package com.easy.sql.core.planner.plan.optimize;

import com.google.common.base.Preconditions;
import org.apache.calcite.plan.RelOptRule;
import org.apache.calcite.tools.RuleSet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangap
 * @version 1.0, 2022/4/25
 */
public abstract class EasySqlRuleSetProgram<OC extends EasySqlOptimizeContext>
        implements EasySqlOptimizeProgram<OC> {

    protected List<RelOptRule> rules = new ArrayList<>();

    public void add(RuleSet ruleSet) {
        Preconditions.checkNotNull(ruleSet);
        ruleSet.forEach(rule -> {
            if (!contains(rule)) {
                rules.add(rule);
            }
        });
    }

    public void remove(RuleSet ruleSet) {
        Preconditions.checkNotNull(ruleSet);
        ruleSet.forEach(rules::remove);
    }

    public void replaceAll(RuleSet ruleSet) {
        Preconditions.checkNotNull(ruleSet);
        rules.clear();
        ruleSet.forEach(rules::add);
    }

    public boolean contains(RelOptRule rule) {
        Preconditions.checkNotNull(rule);
        return rules.contains(rule);
    }

}
