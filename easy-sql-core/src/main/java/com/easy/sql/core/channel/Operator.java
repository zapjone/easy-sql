package com.easy.sql.core.channel;

/**
 * 操作算子
 *
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public interface Operator<IN, OUT> {

    /**
     * 生命周期中打开
     */
    default void open() {
    }

    /**
     * 数据处理并输出
     */
    OUT handle(IN in)

    /**
     * 生命周期关闭
     */
    default void close() {
    }


}
