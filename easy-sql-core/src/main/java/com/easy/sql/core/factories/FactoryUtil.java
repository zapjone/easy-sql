package com.easy.sql.core.factories;

import com.easy.sql.core.exceptions.EasySqlException;
import com.easy.sql.core.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public final class FactoryUtil {

    private static final Logger LOG = LoggerFactory.getLogger(FactoryUtil.class);

    /**
     * 使用spi加载Factory
     *
     * @param classLoader       classloader
     * @param factoryClass      Factory名称
     * @param factoryIdentifier factory标识
     * @param <T>               factory类型
     * @return 查找到的Factory
     */
    @SuppressWarnings("all")
    public static <T extends Factory> T discoverFactory(
            ClassLoader classLoader, Class<T> factoryClass, String factoryIdentifier) {
        /* 查找所有的Factory实现, SPI实现 */
        final List<Factory> factories = discoverFactories(classLoader);

        /* 1. 按给定的Factory来过滤其实现的Factory */
        final List<Factory> foundFactories =
                factories.stream()
                        .filter(f -> factoryClass.isAssignableFrom(f.getClass()))
                        .collect(Collectors.toList());

        /* 如果过滤后，未找到对应的Factory，将抛出异常 */
        if (foundFactories.isEmpty()) {
            throw new ValidationException(
                    String.format(
                            "Could not find any factories that implement '%s' in the classpath.",
                            factoryClass.getName()));
        }

        /* 2. 按照Factory的标识来进行再次过滤，默认为default */
        final List<Factory> matchingFactories =
                foundFactories.stream()
                        .filter(f -> f.factoryIdentifier().equals(factoryIdentifier))
                        .collect(Collectors.toList());

        /* 如果第二次过滤后，发现为空，将抛出异常 */
        if (matchingFactories.isEmpty()) {
            throw new ValidationException(
                    String.format(
                            "Could not find any factory for identifier '%s' that implements '%s' in the classpath.\n\n"
                                    + "Available factory identifiers are:\n\n"
                                    + "%s",
                            factoryIdentifier,
                            factoryClass.getName(),
                            foundFactories.stream()
                                    .map(Factory::factoryIdentifier)
                                    .distinct()
                                    .sorted()
                                    .collect(Collectors.joining("\n"))));
        }
        /* 如果过滤后，发现多余一个Factory，将抛出异常信息，可能是因为identifier名称重复 */
        if (matchingFactories.size() > 1) {
            throw new ValidationException(
                    String.format(
                            "Multiple factories for identifier '%s' that implement '%s' found in the classpath.\n\n"
                                    + "Ambiguous factory classes are:\n\n"
                                    + "%s",
                            factoryIdentifier,
                            factoryClass.getName(),
                            matchingFactories.stream()
                                    .map(f -> f.getClass().getName())
                                    .sorted()
                                    .collect(Collectors.joining("\n"))));
        }

        /* 获取第一个Factory实现并转换为最终的Facotry返回 */
        return (T) matchingFactories.get(0);
    }

    private static List<Factory> discoverFactories(ClassLoader classLoader) {
        try {
            final List<Factory> result = new LinkedList<>();
            ServiceLoader.load(Factory.class, classLoader).iterator().forEachRemaining(result::add);
            return result;
        } catch (ServiceConfigurationError e) {
            LOG.error("Could not load service provider for factories.", e);
            throw new EasySqlException("Could not load service provider for factories.", e);
        }
    }

}
