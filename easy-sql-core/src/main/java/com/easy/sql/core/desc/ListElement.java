package com.easy.sql.core.desc;

import java.util.Arrays;
import java.util.List;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class ListElement implements BlockElement {

    private final List<InlineElement> entries;

    /**
     * <pre>{@code
     * .list(
     * 	text("this is first element of list"),
     * 	text("this is second element of list with a %s", link("https://link"))
     * )
     * }</pre>
     */
    public static ListElement list(InlineElement... elements) {
        return new ListElement(Arrays.asList(elements));
    }

    public List<InlineElement> getEntries() {
        return entries;
    }

    private ListElement(List<InlineElement> entries) {
        this.entries = entries;
    }

    @Override
    public void format(Formatter formatter) {
        formatter.format(this);
    }
}
