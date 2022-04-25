package com.easy.sql.core.planner.plan.optimize;

import java.io.Serializable;

/**
 * @author zhangap
 * @version 1.0, 2022/4/25
 */
public class Tuple<ONE, TWO> implements Serializable {

    private final ONE one;
    private final TWO two;

    public Tuple(ONE one, TWO two) {
        this.one = one;
        this.two = two;
    }

    public ONE getOne() {
        return one;
    }

    public TWO getTwo() {
        return two;
    }

    public static <O, T> Tuple<O, T> of(O one, T two) {
        return new Tuple<>(one, two);
    }

}
