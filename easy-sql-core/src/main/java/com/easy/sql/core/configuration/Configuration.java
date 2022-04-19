package com.easy.sql.core.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.function.BiFunction;

import static com.easy.sql.core.configuration.ConfigurationUtils.canBePrefixMap;
import static com.easy.sql.core.configuration.ConfigurationUtils.containsPrefixMap;
import static com.easy.sql.core.configuration.ConfigurationUtils.convertToPropertiesPrefixed;
import static com.easy.sql.core.configuration.ConfigurationUtils.removePrefixMap;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * easy-sql的配置信息
 *
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class Configuration implements Serializable, Cloneable {

    private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);

    protected final HashMap<String, Object> confData;

    /**
     * 创建一个空的Configuration.
     */
    public Configuration() {
        this.confData = new HashMap<>();
    }

    /**
     * 根据指定的Configuration创建一个新的Configuration
     */
    public Configuration(Configuration other) {
        this.confData = new HashMap<>(other.confData);
    }

    /**
     * 给定的Map中创建Configuration
     *
     * @param map map信息
     * @return Configuration
     */
    public static Configuration fromMap(Map<String, String> map) {
        final Configuration configuration = new Configuration();
        map.forEach(configuration::setString);
        return configuration;
    }

    @SuppressWarnings("unchecked")
    public <T> Class<T> getClass(
            String key, Class<? extends T> defaultValue, ClassLoader classLoader)
            throws ClassNotFoundException {
        Optional<Object> o = getRawValue(key);
        if (!o.isPresent()) {
            return (Class<T>) defaultValue;
        }

        if (o.get().getClass() == String.class) {
            return (Class<T>) Class.forName((String) o.get(), true, classLoader);
        }

        throw new IllegalArgumentException(
                "Configuration cannot evaluate object of class "
                        + o.get().getClass()
                        + " as a class name");
    }

    public void setClass(String key, Class<?> klazz) {
        setValueInternal(key, klazz.getName());
    }

    public void setString(String key, String value) {
        setValueInternal(key, value);
    }

    public void setString(ConfigOption<String> key, String value) {
        setValueInternal(key.key(), value);
    }

    public String getString(ConfigOption<String> configOption, String overrideDefault) {
        return getOptional(configOption).orElse(overrideDefault);
    }

    public String getString(ConfigOption<String> configOption) {
        return getOptional(configOption).orElseGet(configOption::defaultValue);
    }

    public int getInteger(ConfigOption<Integer> configOption) {
        return getOptional(configOption).orElseGet(configOption::defaultValue);
    }

    public int getInteger(ConfigOption<Integer> configOption, int overrideDefault) {
        return getOptional(configOption).orElse(overrideDefault);
    }

    public void setInteger(String key, int value) {
        setValueInternal(key, value);
    }

    public void setInteger(ConfigOption<Integer> key, int value) {
        setValueInternal(key.key(), value);
    }

    public long getLong(ConfigOption<Long> configOption) {
        return getOptional(configOption).orElseGet(configOption::defaultValue);
    }

    public long getLong(ConfigOption<Long> configOption, long overrideDefault) {
        return getOptional(configOption).orElse(overrideDefault);
    }

    public void setLong(String key, long value) {
        setValueInternal(key, value);
    }

    public void setLong(ConfigOption<Long> key, long value) {
        setValueInternal(key.key(), value);
    }

    public boolean getBoolean(ConfigOption<Boolean> configOption) {
        return getOptional(configOption).orElseGet(configOption::defaultValue);
    }

    public boolean getBoolean(ConfigOption<Boolean> configOption, boolean overrideDefault) {
        return getOptional(configOption).orElse(overrideDefault);
    }

    public void setBoolean(String key, boolean value) {
        setValueInternal(key, value);
    }

    public void setBoolean(ConfigOption<Boolean> key, boolean value) {
        setValueInternal(key.key(), value);
    }

    public float getFloat(ConfigOption<Float> configOption) {
        return getOptional(configOption).orElseGet(configOption::defaultValue);
    }

    public float getFloat(ConfigOption<Float> configOption, float overrideDefault) {
        return getOptional(configOption).orElse(overrideDefault);
    }

    public void setFloat(String key, float value) {
        setValueInternal(key, value);
    }

    public void setFloat(ConfigOption<Float> key, float value) {
        setValueInternal(key.key(), value);
    }

    public double getDouble(ConfigOption<Double> configOption) {
        return getOptional(configOption).orElseGet(configOption::defaultValue);
    }

    public double getDouble(ConfigOption<Double> configOption, double overrideDefault) {
        return getOptional(configOption).orElse(overrideDefault);
    }

    public void setDouble(String key, double value) {
        setValueInternal(key, value);
    }

    public void setDouble(ConfigOption<Double> key, double value) {
        setValueInternal(key.key(), value);
    }

    public byte[] getBytes(String key, byte[] defaultValue) {
        return getRawValue(key)
                .map(
                        o -> {
                            if (o.getClass().equals(byte[].class)) {
                                return (byte[]) o;
                            } else {
                                throw new IllegalArgumentException(
                                        String.format(
                                                "Configuration cannot evaluate value %s as a byte[] value",
                                                o));
                            }
                        })
                .orElse(defaultValue);
    }

    public void setBytes(String key, byte[] bytes) {
        setValueInternal(key, bytes);
    }

    public String getValue(ConfigOption<?> configOption) {
        return Optional.ofNullable(
                        getRawValueFromOption(configOption).orElseGet(configOption::defaultValue))
                .map(String::valueOf)
                .orElse(null);
    }

    public <T extends Enum<T>> T getEnum(
            final Class<T> enumClass, final ConfigOption<String> configOption) {
        checkNotNull(enumClass, "enumClass must not be null");
        checkNotNull(configOption, "configOption must not be null");

        Object rawValue = getRawValueFromOption(configOption).orElseGet(configOption::defaultValue);
        try {
            return ConfigurationUtils.convertToEnum(rawValue, enumClass);
        } catch (IllegalArgumentException ex) {
            final String errorMessage =
                    String.format(
                            "Value for config option %s must be one of %s (was %s)",
                            configOption.key(),
                            Arrays.toString(enumClass.getEnumConstants()),
                            rawValue);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public Set<String> keySet() {
        synchronized (this.confData) {
            return new HashSet<>(this.confData.keySet());
        }
    }

    public void addAllToProperties(Properties props) {
        synchronized (this.confData) {
            for (Map.Entry<String, Object> entry : this.confData.entrySet()) {
                props.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public void addAll(Configuration other) {
        synchronized (this.confData) {
            synchronized (other.confData) {
                this.confData.putAll(other.confData);
            }
        }
    }

    public void addAll(Configuration other, String prefix) {
        final StringBuilder bld = new StringBuilder();
        bld.append(prefix);
        final int pl = bld.length();

        synchronized (this.confData) {
            synchronized (other.confData) {
                for (Map.Entry<String, Object> entry : other.confData.entrySet()) {
                    bld.setLength(pl);
                    bld.append(entry.getKey());
                    this.confData.put(bld.toString(), entry.getValue());
                }
            }
        }
    }

    @Override
    public Configuration clone() {
        Configuration config = new Configuration();
        config.addAll(this);

        return config;
    }

    public boolean containsKey(String key) {
        synchronized (this.confData) {
            return this.confData.containsKey(key);
        }
    }

    public boolean contains(ConfigOption<?> configOption) {
        synchronized (this.confData) {
            final BiFunction<String, Boolean, Optional<Boolean>> applier =
                    (key, canBePrefixMap) -> {
                        if (canBePrefixMap && containsPrefixMap(this.confData, key)
                                || this.confData.containsKey(key)) {
                            return Optional.of(true);
                        }
                        return Optional.empty();
                    };
            return applyWithOption(configOption, applier).orElse(false);
        }
    }

    private <T> Optional<T> applyWithOption(
            ConfigOption<?> option, BiFunction<String, Boolean, Optional<T>> applier) {
        final boolean canBePrefixMap = canBePrefixMap(option);
        final Optional<T> valueFromExactKey = applier.apply(option.key(), canBePrefixMap);
        if (valueFromExactKey.isPresent()) {
            return valueFromExactKey;
        } else if (option.hasFallbackKeys()) {
            // try the fallback keys
            for (FallbackKey fallbackKey : option.fallbackKeys()) {
                final Optional<T> valueFromFallbackKey =
                        applier.apply(fallbackKey.getKey(), canBePrefixMap);
                if (valueFromFallbackKey.isPresent()) {
                    loggingFallback(fallbackKey, option);
                    return valueFromFallbackKey;
                }
            }
        }
        return Optional.empty();
    }

    /**
     * 获取配置
     *
     * @param option 配置项
     * @param <T>    内容类型
     */
    public <T> T get(ConfigOption<T> option) {
        return getOptional(option).orElseGet(option::defaultValue);
    }

    public <T> Optional<T> getOptional(ConfigOption<T> option) {
        Optional<Object> rawValue = getRawValueFromOption(option);
        Class<?> clazz = option.getClazz();

        try {
            if (option.isList()) {
                return rawValue.map(v -> ConfigurationUtils.convertToList(v, clazz));
            } else {
                return rawValue.map(v -> ConfigurationUtils.convertValue(v, clazz));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    String.format(
                            "Could not parse value '%s' for key '%s'.",
                            rawValue.map(Object::toString).orElse(""), option.key()),
                    e);
        }
    }

    public <T> Configuration set(ConfigOption<T> option, T value) {
        final boolean canBePrefixMap = canBePrefixMap(option);
        setValueInternal(option.key(), value, canBePrefixMap);
        return this;
    }

    public <T> boolean removeConfig(ConfigOption<T> configOption) {
        synchronized (this.confData) {
            final BiFunction<String, Boolean, Optional<Boolean>> applier =
                    (key, canBePrefixMap) -> {
                        if (canBePrefixMap && removePrefixMap(this.confData, key)
                                || this.confData.remove(key) != null) {
                            return Optional.of(true);
                        }
                        return Optional.empty();
                    };
            return applyWithOption(configOption, applier).orElse(false);
        }
    }

    // --------------------------------------------------------------------------------------------

    <T> void setValueInternal(String key, T value, boolean canBePrefixMap) {
        if (key == null) {
            throw new NullPointerException("Key must not be null.");
        }
        if (value == null) {
            throw new NullPointerException("Value must not be null.");
        }

        synchronized (this.confData) {
            if (canBePrefixMap) {
                removePrefixMap(this.confData, key);
            }
            this.confData.put(key, value);
        }
    }

    private <T> void setValueInternal(String key, T value) {
        setValueInternal(key, value, false);
    }

    private Optional<Object> getRawValue(String key) {
        return getRawValue(key, false);
    }

    private Optional<Object> getRawValue(String key, boolean canBePrefixMap) {
        if (key == null) {
            throw new NullPointerException("Key must not be null.");
        }

        synchronized (this.confData) {
            final Object valueFromExactKey = this.confData.get(key);
            if (!canBePrefixMap || valueFromExactKey != null) {
                return Optional.ofNullable(valueFromExactKey);
            }
            final Map<String, String> valueFromPrefixMap =
                    convertToPropertiesPrefixed(confData, key);
            if (valueFromPrefixMap.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(valueFromPrefixMap);
        }
    }

    private Optional<Object> getRawValueFromOption(ConfigOption<?> configOption) {
        return applyWithOption(configOption, this::getRawValue);
    }

    private void loggingFallback(FallbackKey fallbackKey, ConfigOption<?> configOption) {
        if (fallbackKey.isDeprecated()) {
            LOG.warn(
                    "Config uses deprecated configuration key '{}' instead of proper key '{}'",
                    fallbackKey.getKey(),
                    configOption.key());
        } else {
            LOG.info(
                    "Config uses fallback configuration key '{}' instead of key '{}'",
                    fallbackKey.getKey(),
                    configOption.key());
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (String s : this.confData.keySet()) {
            hash ^= s.hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof Configuration) {
            Map<String, Object> otherConf = ((Configuration) obj).confData;

            for (Map.Entry<String, Object> e : this.confData.entrySet()) {
                Object thisVal = e.getValue();
                Object otherVal = otherConf.get(e.getKey());

                if (!thisVal.getClass().equals(byte[].class)) {
                    if (!thisVal.equals(otherVal)) {
                        return false;
                    }
                } else if (otherVal.getClass().equals(byte[].class)) {
                    if (!Arrays.equals((byte[]) thisVal, (byte[]) otherVal)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.confData.toString();
    }

}
