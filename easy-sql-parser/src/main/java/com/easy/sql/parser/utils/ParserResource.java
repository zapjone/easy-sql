package com.easy.sql.parser.utils;

import com.easy.sql.parser.impl.ParseException;

import org.apache.calcite.runtime.Resources;

/**
 * Compiler-checked resources for the Flink SQL parser.
 *
 * @author zhangap
 * @version 1.0, 2022/4/14
 */
public interface ParserResource {
    /**
     * Resources.
     */
    ParserResource RESOURCE = Resources.create(ParserResource.class);

    @Resources.BaseMessage("OVERWRITE expression is only used with INSERT statement.")
    Resources.ExInst<ParseException> overwriteIsOnlyUsedWithInsert();

    @Resources.BaseMessage(
            "CREATE SYSTEM FUNCTION is not supported, system functions can only be registered as temporary function, you can use CREATE TEMPORARY SYSTEM FUNCTION instead.")
    Resources.ExInst<ParseException> createSystemFunctionOnlySupportTemporary();

    @Resources.BaseMessage("Duplicate EXPLAIN DETAIL is not allowed.")
    Resources.ExInst<ParseException> explainDetailIsDuplicate();
}
