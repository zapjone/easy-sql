package com.easy.sql.core.planner.plan.optimize;

import com.google.common.base.Preconditions;
import org.apache.calcite.rel.RelNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangap
 * @version 1.0, 2022/4/24
 */
public class EasySqlChainedProgram<OC extends EasySqlOptimizerContext>
        implements EasySqlOptimizeProgram<OC> {

    private static final Logger LOG = LoggerFactory.getLogger(EasySqlChainedProgram.class);


    // keep program as ordered
    private List<String> programNames = new ArrayList<>();
    // map program name to program instance
    private Map<String, EasySqlOptimizeProgram<OC>> programMap = new HashMap<>();


    @Override
    public RelNode optimize(RelNode root, OC context) {
        RelNode input = root;
        for (String programName : programNames) {
            long start = System.currentTimeMillis();
            input = programMap.get(programName).optimize(input, context);
            long end = System.currentTimeMillis();

            if (LOG.isDebugEnabled()) {
                LOG.debug("optimize {} cost {} - {} ms.", programName, start, end);
            }
        }
        return input;
    }

    public boolean addFirst(String name, EasySqlOptimizeProgram<OC> program) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(program);

        if (programNames.contains(name)) {
            return false;
        } else {
            programNames.add(0, name);
            programMap.put(name, program);
            return true;
        }
    }

    public boolean addList(String name, EasySqlOptimizeProgram<OC> program) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(program);
        if (programNames.contains(name)) {
            return false;
        } else {
            programNames.add(name);
            programMap.put(name, program);
            return true;
        }
    }
}
