package com.easy.sql.core.planner.catalog;

import com.easy.sql.core.exceptions.CatalogException;
import com.easy.sql.core.exceptions.CatalogNotExistException;
import com.easy.sql.core.exceptions.DatabaseNotExistException;
import com.easy.sql.core.exceptions.EasySqlException;
import com.easy.sql.core.util.StringUtils;
import org.apache.calcite.schema.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

/**
 * easy-sql的CatalogMangaer，用于管理Catalog
 *
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public final class CatalogManager {
    private static final Logger LOG = LoggerFactory.getLogger(CatalogManager.class);

    /**
     * 记录的catalog列表
     */
    private final Map<String, Catalog> catalogs;

    /**
     * 临时表
     */
    private final Map<ObjectIdentifier, CatalogBaseTable> temporaryTables;

    private String currentCatalogName;

    private String currentDatabaseName;

    private CatalogManager(String defaultCatalogName, Catalog defaultCatalog) {
        checkArgument(
                !StringUtils.isNullOrWhitespaceOnly(defaultCatalogName),
                "Default catalog name cannot be null or empty");
        checkNotNull(defaultCatalog, "Default catalog cannot be null");

        catalogs = new LinkedHashMap<>();
        catalogs.put(defaultCatalogName, defaultCatalog);
        currentCatalogName = defaultCatalogName;
        currentDatabaseName = defaultCatalog.getDefaultDatabase();

        temporaryTables = new HashMap<>();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {

        private ClassLoader classLoader;


        private String defaultCatalogName;

        private Catalog defaultCatalog;

        public Builder classLoader(ClassLoader classLoader) {
            this.classLoader = classLoader;
            return this;
        }


        public Builder defaultCatalog(String defaultCatalogName, Catalog defaultCatalog) {
            this.defaultCatalogName = defaultCatalogName;
            this.defaultCatalog = defaultCatalog;
            return this;
        }


        public CatalogManager build() {
            checkNotNull(classLoader, "Class loader cannot be null");
            return new CatalogManager(defaultCatalogName, defaultCatalog);
        }
    }

    /**
     * 注册catalog
     *
     * @param catalogName catalog名称
     * @param catalog     Catalog
     */
    public void registerCatalog(String catalogName, Catalog catalog) {
        checkArgument(
                !StringUtils.isNullOrWhitespaceOnly(catalogName),
                "Catalog name cannot be null or empty.");
        checkNotNull(catalog, "Catalog cannot be null");

        if (catalogs.containsKey(catalogName)) {
            throw new CatalogException(format("Catalog %s already exists.", catalogName));
        }

        catalog.open();
        catalogs.put(catalogName, catalog);
    }

    /**
     * 取消注册catalog
     */
    public void unregisterCatalog(String catalogName, boolean ignoreIfNotExists) {
        checkArgument(
                !StringUtils.isNullOrWhitespaceOnly(catalogName),
                "Catalog name cannot be null or empty.");

        if (catalogs.containsKey(catalogName)) {
            Catalog catalog = catalogs.remove(catalogName);
            catalog.close();
        } else if (!ignoreIfNotExists) {
            throw new CatalogException(format("Catalog %s does not exist.", catalogName));
        }
    }

    /**
     * 获取指定的catalog
     *
     * @param catalogName catalog名称
     */
    public Optional<Catalog> getCatalog(String catalogName) {
        return Optional.ofNullable(catalogs.get(catalogName));
    }

    /**
     * 获取当前catalog
     */
    public String getCurrentCatalog() {
        return currentCatalogName;
    }

    /**
     * 设置catalog
     */
    public void setCurrentCatalog(String catalogName) throws CatalogNotExistException {
        checkArgument(
                !StringUtils.isNullOrWhitespaceOnly(catalogName),
                "Catalog name cannot be null or empty.");

        Catalog potentialCurrentCatalog = catalogs.get(catalogName);
        if (potentialCurrentCatalog == null) {
            throw new CatalogException(
                    format("A catalog with name [%s] does not exist.", catalogName));
        }

        if (!currentCatalogName.equals(catalogName)) {
            currentCatalogName = catalogName;
            currentDatabaseName = potentialCurrentCatalog.getDefaultDatabase();

            LOG.info(
                    "Set the current default catalog as [{}] and the current default database as [{}].",
                    currentCatalogName,
                    currentDatabaseName);
        }
    }

    /**
     * 获取当前数据库
     */
    public String getCurrentDatabase() {
        return currentDatabaseName;
    }

    /**
     * 设置当前数据库
     *
     * @param databaseName 数据库名称
     */
    public void setCurrentDatabase(String databaseName) {
        checkArgument(
                !StringUtils.isNullOrWhitespaceOnly(databaseName),
                "The database name cannot be null or empty.");

        if (!catalogs.get(currentCatalogName).databaseExists(databaseName)) {
            throw new CatalogException(
                    format(
                            "A database with name [%s] does not exist in the catalog: [%s].",
                            databaseName, currentCatalogName));
        }

        if (!currentDatabaseName.equals(databaseName)) {
            currentDatabaseName = databaseName;

            LOG.info(
                    "Set the current default database as [{}] in the current default catalog [{}].",
                    currentDatabaseName,
                    currentCatalogName);
        }
    }

    /**
     * 获取所有catalog
     */
    public Set<String> listCatalogs() {
        return Collections.unmodifiableSet(catalogs.keySet());
    }

    /**
     * 获取所有表
     */
    public Set<String> listTables() {
        return listTables(getCurrentCatalog(), getCurrentDatabase());
    }

    public Set<String> listTables(String catalogName, String databaseName) {
        Catalog currentCatalog = catalogs.get(getCurrentCatalog());

        try {
            return Stream.concat(
                            currentCatalog.listTables(getCurrentDatabase()).stream(),
                            listTemporaryTablesInternal(catalogName, databaseName)
                                    .map(e -> e.getKey().getObjectName()))
                    .collect(Collectors.toSet());
        } catch (DatabaseNotExistException e) {
            throw new EasySqlException("Current database does not exist", e);
        }
    }

    /**
     * 获取临时表列表
     */
    public Set<String> listTemporaryTables() {
        return listTemporaryTablesInternal(getCurrentCatalog(), getCurrentDatabase())
                .map(e -> e.getKey().getObjectName())
                .collect(Collectors.toSet());
    }

    /**
     * 临时视图
     */
    public Set<String> listTemporaryViews() {
        return listTemporaryViewsInternal(getCurrentCatalog(), getCurrentDatabase())
                .map(e -> e.getKey().getObjectName())
                .collect(Collectors.toSet());
    }

    private Stream<Map.Entry<ObjectIdentifier, CatalogBaseTable>> listTemporaryTablesInternal(
            String catalogName, String databaseName) {
        return temporaryTables.entrySet().stream()
                .filter(
                        e -> {
                            ObjectIdentifier identifier = e.getKey();
                            return identifier.getCatalogName().equals(catalogName)
                                    && identifier.getDatabaseName().equals(databaseName);
                        });
    }

    /**
     * 所有视图
     */
    public Set<String> listViews(String catalogName, String databaseName) {
        Catalog currentCatalog = catalogs.get(getCurrentCatalog());

        try {
            return Stream.concat(
                            currentCatalog.listViews(getCurrentDatabase()).stream(),
                            listTemporaryViewsInternal(catalogName, databaseName)
                                    .map(e -> e.getKey().getObjectName()))
                    .collect(Collectors.toSet());
        } catch (DatabaseNotExistException e) {
            throw new EasySqlException("Current database does not exist", e);
        }
    }

    private Stream<Map.Entry<ObjectIdentifier, CatalogBaseTable>> listTemporaryViewsInternal(
            String catalogName, String databaseName) {
        //TODO 未实现
        return null;
    }

    public Set<String> listSchemas() {
        return Stream.concat(
                        catalogs.keySet().stream(),
                        temporaryTables.keySet().stream().map(ObjectIdentifier::getCatalogName))
                .collect(Collectors.toSet());
    }

    public Set<String> listSchemas(String catalogName) {
        return Stream.concat(
                        Optional.ofNullable(catalogs.get(catalogName)).map(Catalog::listDatabases)
                                .orElse(Collections.emptyList()).stream(),
                        temporaryTables.keySet().stream()
                                .filter(i -> i.getCatalogName().equals(catalogName))
                                .map(ObjectIdentifier::getDatabaseName))
                .collect(Collectors.toSet());
    }

    public boolean schemaExists(String catalogName) {
        return getCatalog(catalogName).isPresent()
                || temporaryTables.keySet().stream()
                .anyMatch(i -> i.getCatalogName().equals(catalogName));
    }

    public boolean schemaExists(String catalogName, String databaseName) {
        return temporaryDatabaseExists(catalogName, databaseName)
                || permanentDatabaseExists(catalogName, databaseName);
    }

    private boolean temporaryDatabaseExists(String catalogName, String databaseName) {
        return temporaryTables.keySet().stream()
                .anyMatch(
                        i ->
                                i.getCatalogName().equals(catalogName)
                                        && i.getDatabaseName().equals(databaseName));
    }

    private boolean permanentDatabaseExists(String catalogName, String databaseName) {
        return getCatalog(catalogName).map(c -> c.databaseExists(databaseName)).orElse(false);
    }

    /**
     * 创建表
     */
    public void createTable(
            CatalogBaseTable table, ObjectIdentifier objectIdentifier, boolean ignoreIfExists) {
    }

    /**
     * 获取表
     */
    public Table getTable(ObjectIdentifier objectIdentifier){
        // CatalogSchemaTable
        return null;
    }

    /**
     * 创建临时表
     */
    public void createTemporaryTable(
            CatalogBaseTable table, ObjectIdentifier objectIdentifier, boolean ignoreIfExists) {
    }

    public void dropTemporaryTable(ObjectIdentifier objectIdentifier, boolean ignoreIfNotExists) {
    }

    public void dropTemporaryView(ObjectIdentifier objectIdentifier, boolean ignoreIfNotExists) {
    }


}
