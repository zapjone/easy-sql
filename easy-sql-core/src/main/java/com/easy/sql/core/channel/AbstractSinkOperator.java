package com.easy.sql.core.channel;

/**
 * sink输出操作
 *
 * @author zhangap
 * @version 1.0, 2022/4/22
 */
public abstract class AbstractSinkOperator<IN> implements Operator<IN, Void> {

    @Override
    public final Void handle(IN in) {
        sink(in);
        return null;
    }

    /**
     * sink输出操作
     *
     * @param in 输入操作
     */
    protected abstract void sink(IN in);

}
