package com.easy.sql.core.planner.plan.rules.logical;

import org.apache.calcite.plan.RelOptRule;
import org.apache.calcite.plan.RelOptRuleCall;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelShuttleImpl;
import org.apache.calcite.rel.core.Filter;
import org.apache.calcite.rel.logical.LogicalFilter;
import org.apache.calcite.rex.RexBuilder;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.rex.RexShuttle;
import org.apache.calcite.rex.RexSubQuery;
import org.apache.calcite.rex.RexUtil;

import java.util.Optional;

/**
 * @author zhangap
 * @version 1.0, 2022/5/6
 */
public class SimplifyFilterConditionRule extends RelOptRule {

    public static final SimplifyFilterConditionRule INSTANCE = new SimplifyFilterConditionRule(
            false, "SimplifyFilterConditionRule");

    public static final SimplifyFilterConditionRule EXTENDED = new SimplifyFilterConditionRule(
            true, "SimplifyFilterConditionRule:simplifySubQuery");

    private final boolean simplifySubQuery;

    public SimplifyFilterConditionRule(boolean simplifySubQuery, String description) {
        super(operand(Filter.class, any()), description);
        this.simplifySubQuery = simplifySubQuery;
    }

    @Override
    public void onMatch(RelOptRuleCall call) {
        Filter filter = call.rel(0);
        Optional<Filter> newFilter = simplify(filter, false);
        if (newFilter.isPresent()) {
            Filter f = newFilter.get();
            call.transformTo(f);
            call.getPlanner().prune(filter);
        }
    }

    private Optional<Filter> simplify(Filter filter, Boolean changed) {
        RexNode condition;
        if (simplifySubQuery) {
            condition = simplifyFilterConditionInSubQuery(filter.getCondition(), changed);
        } else {
            condition = filter.getCondition();
        }

        RexBuilder rexBuilder = filter.getCluster().getRexBuilder();
        RexNode newCondition = RexUtil.pullFactors(rexBuilder, condition);

        if (!changed && !condition.equals(newCondition)) {
            changed = true;
        }

        if (changed) {
            return Optional.of(filter.copy(filter.getTraitSet(), filter.getInput(), newCondition));
        }
        return Optional.empty();
    }

    private RexNode simplifyFilterConditionInSubQuery(RexNode condition, Boolean changed) {
        return condition.accept(new RexShuttle() {
            @Override
            public RexNode visitSubQuery(RexSubQuery subQuery) {
                RelNode newRel = subQuery.rel.accept(new RelShuttleImpl() {
                    @Override
                    public RelNode visit(LogicalFilter filter) {
                        return simplify(filter, changed).orElse(filter);
                    }
                });
                if (changed) {
                    return subQuery.clone(newRel);
                } else {
                    return subQuery;
                }
            }
        });
    }

}
