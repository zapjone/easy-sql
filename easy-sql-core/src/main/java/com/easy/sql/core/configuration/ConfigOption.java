package com.easy.sql.core.configuration;

import com.easy.sql.core.configuration.desc.Description;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 配置
 *
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class ConfigOption<T> {

    private static final FallbackKey[] EMPTY = new FallbackKey[0];

    static final Description EMPTY_DESCRIPTION = Description.builder().text("").build();

    // ------------------------------------------------------------------------

    /**
     * The current key for that config option.
     */
    private final String key;

    /**
     * The list of deprecated keys, in the order to be checked.
     */
    private final FallbackKey[] fallbackKeys;

    /**
     * The default value for this option.
     */
    private final T defaultValue;

    /**
     * The description for this option.
     */
    private final Description description;

    /**
     * Type of the value that this ConfigOption describes.
     */
    private final Class<?> clazz;

    private final boolean isList;

    // ------------------------------------------------------------------------

    Class<?> getClazz() {
        return clazz;
    }

    boolean isList() {
        return isList;
    }

    /**
     * Creates a new config option with fallback keys.
     */
    ConfigOption(
            String key,
            Class<?> clazz,
            Description description,
            T defaultValue,
            boolean isList,
            FallbackKey... fallbackKeys) {
        this.key = checkNotNull(key);
        this.description = description;
        this.defaultValue = defaultValue;
        this.fallbackKeys = fallbackKeys == null || fallbackKeys.length == 0 ? EMPTY : fallbackKeys;
        this.clazz = checkNotNull(clazz);
        this.isList = isList;
    }

    // ------------------------------------------------------------------------

    /**
     * Creates a new config option, using this option's key and default value, and adding the given
     * fallback keys.
     */
    public ConfigOption<T> withFallbackKeys(String... fallbackKeys) {
        final Stream<FallbackKey> newFallbackKeys =
                Arrays.stream(fallbackKeys).map(FallbackKey::createFallbackKey);
        final Stream<FallbackKey> currentAlternativeKeys = Arrays.stream(this.fallbackKeys);

        // put fallback keys first so that they are prioritized
        final FallbackKey[] mergedAlternativeKeys =
                Stream.concat(newFallbackKeys, currentAlternativeKeys).toArray(FallbackKey[]::new);
        return new ConfigOption<>(
                key, clazz, description, defaultValue, isList, mergedAlternativeKeys);
    }

    /**
     * Creates a new config option, using this option's key and default value, and adding the given
     * deprecated keys.
     */
    public ConfigOption<T> withDeprecatedKeys(String... deprecatedKeys) {
        final Stream<FallbackKey> newDeprecatedKeys =
                Arrays.stream(deprecatedKeys).map(FallbackKey::createDeprecatedKey);
        final Stream<FallbackKey> currentAlternativeKeys = Arrays.stream(this.fallbackKeys);

        // put deprecated keys last so that they are de-prioritized
        final FallbackKey[] mergedAlternativeKeys =
                Stream.concat(currentAlternativeKeys, newDeprecatedKeys)
                        .toArray(FallbackKey[]::new);
        return new ConfigOption<>(
                key, clazz, description, defaultValue, isList, mergedAlternativeKeys);
    }

    /**
     * Creates a new config option, using this option's key and default value, and adding the given
     * description. The given description is used when generation the configuration documention.
     */
    public ConfigOption<T> withDescription(final String description) {
        return withDescription(Description.builder().text(description).build());
    }

    /**
     * Creates a new config option, using this option's key and default value, and adding the given
     * description. The given description is used when generation the configuration documention.
     */
    public ConfigOption<T> withDescription(final Description description) {
        return new ConfigOption<>(key, clazz, description, defaultValue, isList, fallbackKeys);
    }

    // ------------------------------------------------------------------------

    /**
     * Gets the configuration key.
     */
    public String key() {
        return key;
    }

    /**
     * Checks if this option has a default value.
     */
    public boolean hasDefaultValue() {
        return defaultValue != null;
    }

    /**
     * Returns the default value, or null, if there is no default value.
     */
    public T defaultValue() {
        return defaultValue;
    }

    /**
     * Checks whether this option has fallback keys.
     */
    public boolean hasFallbackKeys() {
        return fallbackKeys != EMPTY;
    }

    /**
     * Gets the fallback keys, in the order to be checked.
     */
    public Iterable<FallbackKey> fallbackKeys() {
        return (fallbackKeys == EMPTY) ? Collections.emptyList() : Arrays.asList(fallbackKeys);
    }

    /**
     * Returns the description of this option.
     */
    public Description description() {
        return description;
    }

    // ------------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && o.getClass() == ConfigOption.class) {
            ConfigOption<?> that = (ConfigOption<?>) o;
            return this.key.equals(that.key)
                    && Arrays.equals(this.fallbackKeys, that.fallbackKeys)
                    && (this.defaultValue == null
                    ? that.defaultValue == null
                    : (that.defaultValue != null
                    && this.defaultValue.equals(that.defaultValue)));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return 31 * key.hashCode()
                + 17 * Arrays.hashCode(fallbackKeys)
                + (defaultValue != null ? defaultValue.hashCode() : 0);
    }

    @Override
    public String toString() {
        return String.format(
                "Key: '%s' , default: %s (fallback keys: %s)",
                key, defaultValue, Arrays.toString(fallbackKeys));
    }
}
