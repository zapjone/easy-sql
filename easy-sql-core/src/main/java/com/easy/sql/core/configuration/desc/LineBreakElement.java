package com.easy.sql.core.configuration.desc;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class LineBreakElement implements InlineElement, BlockElement{
    public static LineBreakElement linebreak() {
        return new LineBreakElement();
    }

    private LineBreakElement() {}

    @Override
    public void format(Formatter formatter) {
        formatter.format(this);
    }
}
