package com.easy.sql.core.planner.delegation;

import com.easy.sql.parser.impl.EasySqlParserImpl;
import com.easy.sql.parser.validate.EasySqlConformance;
import org.apache.calcite.sql.parser.SqlParserImplFactory;
import org.apache.calcite.sql.validate.SqlConformance;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class EasySqlParserFactories {

    private EasySqlParserFactories() {
    }

    public static SqlParserImplFactory create(SqlConformance conformance) {
        if (conformance == EasySqlConformance.DEFAULT) {
            return EasySqlParserImpl.FACTORY;
        }
        throw new UnsupportedOperationException("Unsupported conformance: " + conformance);
    }

}
