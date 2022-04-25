package com.easy.sql.core.planner.plan.optimize;

import com.google.common.base.Preconditions;
import org.apache.calcite.plan.RelTrait;
import org.apache.calcite.plan.hep.HepMatchOrder;
import org.apache.calcite.plan.hep.HepProgramBuilder;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.tools.RuleSet;

import java.util.Optional;

/**
 * @author zhangap
 * @version 1.0, 2022/4/25
 */
public class EasySqlHepRuleSetProgram<OC extends EasySqlOptimizeContext>
        extends EasySqlRuleSetProgram<OC> {

    private HepMatchOrder matchOrder = HepMatchOrder.ARBITRARY;
    private int matchLimit = Integer.MAX_VALUE;
    private HepRulesExecutionType executionType = HepRulesExecutionType.RULE_SEQUENCE;
    private Optional<RelTrait[]> requestedRootTraits = Optional.empty();

    @Override
    public RelNode optimize(RelNode input, OC context) {
        if (rules.isEmpty()) {
            return input;
        }

        HepProgramBuilder builder = new HepProgramBuilder();
        builder.addMatchOrder(matchOrder);
        builder.addMatchLimit(matchLimit);
        switch (executionType) {
            case RULE_SEQUENCE:
                rules.forEach(builder::addRuleInstance);
                break;
            case RULE_COLLECTION:
                builder.addRuleCollection(rules);
                break;
            default:
                throw new RuntimeException("Unsupported HEP_RULES_EXECUTION_TYPE:" + executionType);
        }

        // optimize with HepProgram
        EasySqlHepProgram<OC> flinkHepProgram = new EasySqlHepProgram<>(builder.build(), requestedRootTraits);
        return flinkHepProgram.optimize(input, context);
    }


    /**
     * Sets rules match order.
     */
    public void setHepMatchOrder(HepMatchOrder matchOrder) {
        this.matchOrder = Preconditions.checkNotNull(matchOrder);
    }

    /**
     * Sets the limit of pattern matches.
     */
    public void setMatchLimit(int matchLimit) {
        Preconditions.checkArgument(matchLimit > 0);
        this.matchLimit = matchLimit;
    }

    /**
     * Sets hep rule execution type.
     */
    public void setHepRulesExecutionType(HepRulesExecutionType executionType) {
        this.executionType = Preconditions.checkNotNull(executionType);
    }

    /**
     * Sets requested root traits.
     */
    public void setRequestedRootTraits(RelTrait[] relTraits) {
        requestedRootTraits = Optional.ofNullable(relTraits);
    }

    public static <OC extends EasySqlOptimizeContext> EasySqlHepRuleSetProgramBuilder<OC> newBuilder() {
        return new EasySqlHepRuleSetProgramBuilder<>();
    }

    static class EasySqlHepRuleSetProgramBuilder<OC extends EasySqlOptimizeContext> {
        private EasySqlHepRuleSetProgram<OC> hepRuleSetProgram = new EasySqlHepRuleSetProgram<>();

        public EasySqlHepRuleSetProgramBuilder<OC> setHepRulesExecutionType(HepRulesExecutionType executionType) {
            hepRuleSetProgram.setHepRulesExecutionType(executionType);
            return this;
        }

        /**
         * Sets rules match order.
         */
        public EasySqlHepRuleSetProgramBuilder<OC> setHepMatchOrder(HepMatchOrder matchOrder) {
            hepRuleSetProgram.setHepMatchOrder(matchOrder);
            return this;
        }

        /**
         * Sets the limit of pattern matches.
         */
        public EasySqlHepRuleSetProgramBuilder<OC> setMatchLimit(int matchLimit) {
            hepRuleSetProgram.setMatchLimit(matchLimit);
            return this;
        }

        /**
         * Adds rules for this program.
         */
        public EasySqlHepRuleSetProgramBuilder<OC> add(RuleSet ruleSet) {
            hepRuleSetProgram.add(ruleSet);
            return this;
        }

        /**
         * Sets requested root traits.
         */
        public EasySqlHepRuleSetProgramBuilder<OC> setRequestedRootTraits(RelTrait[] relTraits) {
            hepRuleSetProgram.setRequestedRootTraits(relTraits);
            return this;
        }

        public EasySqlHepRuleSetProgram<OC> build() {
            return hepRuleSetProgram;
        }
    }

    enum HepRulesExecutionType {
        RULE_SEQUENCE,
        RULE_COLLECTION;
    }

}
