package com.easy.sql.core.configuration.desc;

import com.google.common.base.Strings;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class TextElement implements BlockElement, InlineElement {
    private final String format;
    private final List<InlineElement> elements;
    private final EnumSet<TextStyle> textStyles = EnumSet.noneOf(TextStyle.class);


    /**
     * <p>{@code text("This is a text with a link %s", link("https://somepage", "to here"))}
     */
    public static TextElement text(String format, InlineElement... elements) {
        return new TextElement(format, Arrays.asList(elements));
    }

    /**
     * Creates a simple block of text.
     */
    public static TextElement text(String text) {
        return new TextElement(text, Collections.emptyList());
    }

    /**
     * Wraps a list of {@link InlineElement}s into a single {@link TextElement}.
     */
    public static InlineElement wrap(InlineElement... elements) {
        return text(Strings.repeat("%s", elements.length), elements);
    }

    /**
     * Creates a block of text formatted as code.
     */
    public static TextElement code(String text) {
        TextElement element = text(text);
        element.textStyles.add(TextStyle.CODE);
        return element;
    }

    public String getFormat() {
        return format;
    }

    public List<InlineElement> getElements() {
        return elements;
    }

    public EnumSet<TextStyle> getStyles() {
        return textStyles;
    }

    private TextElement(String format, List<InlineElement> elements) {
        this.format = format;
        this.elements = elements;
    }

    @Override
    public void format(Formatter formatter) {
        formatter.format(this);
    }

    /**
     * Styles that can be applied to {@link TextElement} e.g. code, bold etc.
     */
    public enum TextStyle {
        CODE
    }
}
