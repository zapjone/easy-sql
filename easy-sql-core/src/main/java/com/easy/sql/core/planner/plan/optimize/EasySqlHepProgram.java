package com.easy.sql.core.planner.plan.optimize;

import com.easy.sql.core.exceptions.EasySqlException;
import com.google.common.base.Preconditions;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelTrait;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.plan.hep.HepPlanner;
import org.apache.calcite.plan.hep.HepProgram;
import org.apache.calcite.rel.RelNode;

import java.util.Optional;

/**
 * @author zhangap
 * @version 1.0, 2022/4/25
 */
public class EasySqlHepProgram<OC extends EasySqlOptimizeContext> implements EasySqlOptimizeProgram<OC> {

    private Optional<HepProgram> hepProgram = Optional.empty();
    private Optional<RelTrait[]> requestedRootTraits = Optional.empty();


    public EasySqlHepProgram(HepProgram hepProgram, Optional<RelTrait[]> requestedRootTraits) {
        this.setHepProgram(hepProgram);
        requestedRootTraits.ifPresent(this::setRequestedRootTraits);
    }

    public void setHepProgram(HepProgram hepProgram) {
        Preconditions.checkNotNull(hepProgram);
        this.hepProgram = Optional.of(hepProgram);
    }

    /**
     * Sets requested root traits.
     */
    public void setRequestedRootTraits(RelTrait[] relTraits) {
        requestedRootTraits = Optional.ofNullable(relTraits);
    }

    @Override
    public RelNode optimize(RelNode root, OC context) {
        if (!hepProgram.isPresent()) {
            throw new EasySqlException("hepProgram should not be None in FlinkHepProgram");
        }

        try {
            HepPlanner planner = new HepPlanner(hepProgram.get(), context);
            EasySqlRelMdNonCumulativeCost.threadPlanner.set(planner);

            planner.setRoot(root);

            if (requestedRootTraits.isPresent()) {
                RelTraitSet targetTraitSet = root.getTraitSet().plusAll(requestedRootTraits.get());
                if (!root.getTraitSet().equals(targetTraitSet)) {
                    planner.changeTraits(root, targetTraitSet.simplify());
                }
            }

            return planner.findBestExp();
        } finally {
            EasySqlRelMdNonCumulativeCost.threadPlanner.remove();
        }
    }

    static class EasySqlRelMdNonCumulativeCost {
        static ThreadLocal<RelOptPlanner> threadPlanner = new ThreadLocal<>();
    }
}
