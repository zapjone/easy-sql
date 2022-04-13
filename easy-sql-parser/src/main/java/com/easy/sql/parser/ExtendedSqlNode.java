package com.easy.sql.parser;

import com.easy.sql.parser.error.SqlValidateException;

/**
 * 结合定义的规则自定义验证规则
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public interface ExtendedSqlNode {

    /**
     * sql解析验证
     */
    void validate() throws SqlValidateException;

}
