package com.easy.sql.core.planner.plan.optimize;

import com.google.common.base.Preconditions;
import org.apache.calcite.rel.RelNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangap
 * @version 1.0, 2022/4/25
 */
public class EasySqlGroupProgram<OC extends EasySqlOptimizeContext>
        implements EasySqlOptimizeProgram<OC> {

    private static final Logger LOG = LoggerFactory.getLogger(EasySqlGroupProgram.class);

    private List<Tuple<EasySqlOptimizeProgram<OC>, String>> programs = new ArrayList<>();

    private int iterations = 1;


    @Override
    public RelNode optimize(RelNode root, OC context) {
        if (programs.isEmpty()) {
            return root;
        }

        RelNode input = root;
        for (int i = 0; i < iterations; i++) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("iteration:" + (i + 1));
            }
            RelNode currentInput = input;
            for (Tuple<EasySqlOptimizeProgram<OC>, String> program : programs) {
                long start = System.currentTimeMillis();
                RelNode result = program.getOne().optimize(currentInput, context);
                long end = System.currentTimeMillis();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("optimize {} cost {} ms.", program.getTwo(), start - end);
                }
                currentInput = result;
            }
            input = currentInput;
        }
        return input;
    }

    public void addProgram(EasySqlOptimizeProgram<OC> program, String description) {
        Preconditions.checkNotNull(program);
        String desc = null != description ? description : "";
        programs.add(Tuple.of(program, desc));
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public static <OC extends EasySqlOptimizeContext> EasySqlGroupProgramBuilder<OC> newBuilder() {
        return new EasySqlGroupProgramBuilder<>();
    }

    static class EasySqlGroupProgramBuilder<OC extends EasySqlOptimizeContext> {
        private EasySqlGroupProgram<OC> groupProgram = new EasySqlGroupProgram<>();

        public EasySqlGroupProgramBuilder<OC> addProgram(EasySqlOptimizeProgram<OC> program,
                                                         String description) {
            groupProgram.addProgram(program, description);
            return this;
        }


        public EasySqlGroupProgramBuilder<OC> setIterations(int iterations) {
            groupProgram.setIterations(iterations);
            return this;
        }

        public EasySqlGroupProgram<OC> build() {
            return groupProgram;
        }
    }
}
