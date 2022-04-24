package com.easy.sql.core.channel;

/**
 * source输入操作
 *
 * @author zhangap
 * @version 1.0, 2022/4/22
 */
public abstract class AbstractSourceOperator<OUT> implements Operator<Void, OUT> {

    @Override
    public final OUT handle(Void unused) {
        return source();
    }

    /**
     * 生成源数据
     *
     * @return 输出数据
     */
    protected abstract OUT source();

}
