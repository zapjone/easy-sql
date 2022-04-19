package com.easy.sql.core.configuration;

import com.easy.sql.core.common.SqlDialect;
import com.easy.sql.core.configuration.desc.Description;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public final class EasySqlConfigOptions {

    private EasySqlConfigOptions() {
    }

    public static OptionBuilder key(String key) {
        checkNotNull(key);
        return new OptionBuilder(key);
    }

    public static final class OptionBuilder {
        @SuppressWarnings("unchecked")
        private static final Class<Map<String, String>> PROPERTIES_MAP_CLASS =
                (Class<Map<String, String>>) (Class<?>) Map.class;

        private final String key;

        /**
         *
         */
        OptionBuilder(String key) {
            this.key = key;
        }

        public TypedConfigOptionBuilder<Boolean> booleanType() {
            return new TypedConfigOptionBuilder<>(key, Boolean.class);
        }

        public TypedConfigOptionBuilder<Integer> intType() {
            return new TypedConfigOptionBuilder<>(key, Integer.class);
        }

        public TypedConfigOptionBuilder<Long> longType() {
            return new TypedConfigOptionBuilder<>(key, Long.class);
        }

        public TypedConfigOptionBuilder<Float> floatType() {
            return new TypedConfigOptionBuilder<>(key, Float.class);
        }

        public TypedConfigOptionBuilder<Double> doubleType() {
            return new TypedConfigOptionBuilder<>(key, Double.class);
        }

        public TypedConfigOptionBuilder<String> stringType() {
            return new TypedConfigOptionBuilder<>(key, String.class);
        }

        public TypedConfigOptionBuilder<Duration> durationType() {
            return new TypedConfigOptionBuilder<>(key, Duration.class);
        }

        public <T extends Enum<T>> TypedConfigOptionBuilder<T> enumType(Class<T> enumClass) {
            return new TypedConfigOptionBuilder<>(key, enumClass);
        }

        public TypedConfigOptionBuilder<Map<String, String>> mapType() {
            return new TypedConfigOptionBuilder<>(key, PROPERTIES_MAP_CLASS);
        }

    }

    public static class TypedConfigOptionBuilder<T> {
        private final String key;
        private final Class<T> clazz;

        TypedConfigOptionBuilder(String key, Class<T> clazz) {
            this.key = key;
            this.clazz = clazz;
        }

        public ListConfigOptionBuilder<T> asList() {
            return new ListConfigOptionBuilder<>(key, clazz);
        }

        public ConfigOption<T> defaultValue(T value) {
            return new ConfigOption<>(key, clazz, ConfigOption.EMPTY_DESCRIPTION, value, false);
        }

        public ConfigOption<T> noDefaultValue() {
            return new ConfigOption<>(
                    key, clazz, Description.builder().text("").build(), null, false);
        }
    }

    public static class ListConfigOptionBuilder<E> {
        private final String key;
        private final Class<E> clazz;

        ListConfigOptionBuilder(String key, Class<E> clazz) {
            this.key = key;
            this.clazz = clazz;
        }

        @SafeVarargs
        public final ConfigOption<List<E>> defaultValues(E... values) {
            return new ConfigOption<>(
                    key, clazz, ConfigOption.EMPTY_DESCRIPTION, Arrays.asList(values), true);
        }

        public ConfigOption<List<E>> noDefaultValue() {
            return new ConfigOption<>(key, clazz, ConfigOption.EMPTY_DESCRIPTION, null, true);
        }
    }


    public static final ConfigOption<String> EASY_SQL_DIALECT =
            key("easy.sql-dialect")
                    .stringType()
                    .defaultValue(SqlDialect.DEFAULT.name().toLowerCase())
                    .withDescription(
                            "SQL方言，默认为:default");

    public static final ConfigOption<String> LOCAL_TIME_ZONE =
            key("easy.local-time-zone")
                    .stringType()
                    .defaultValue("default")
                    .withDescription(
                            "The local time zone defines current session time zone id. It is used when converting to/from "
                                    + "<code>TIMESTAMP WITH LOCAL TIME ZONE</code>. Internally, timestamps with local time zone are always represented in the UTC time zone. "
                                    + "However, when converting to data types that don't include a time zone (e.g. TIMESTAMP, TIME, or simply STRING), "
                                    + "the session time zone is used during conversion. The input of option is either a full name "
                                    + "such as \"America/Los_Angeles\", or a custom timezone id such as \"GMT-08:00\".");

    public static final ConfigOption<Integer> MAX_LENGTH_GENERATED_CODE =
            key("easy.generated-code.max-length")
                    .intType()
                    .defaultValue(4000)
                    .withDescription(
                            "Specifies a threshold where generated code will be split into sub-function calls. "
                                    + "Java has a maximum method length of 64 KB. This setting allows for finer granularity if necessary. "
                                    + "Default value is 4000 instead of 64KB as by default JIT refuses to work on methods with more than 8K byte code.");

}
