package com.easy.sql.core.planner.catalog;

import com.easy.sql.core.exceptions.CatalogException;
import com.easy.sql.core.exceptions.DatabaseAlreadyExistException;
import com.easy.sql.core.exceptions.DatabaseNotEmptyException;
import com.easy.sql.core.exceptions.DatabaseNotExistException;
import com.easy.sql.core.exceptions.TableAlreadyExistException;
import com.easy.sql.core.exceptions.TableNotExistException;
import com.easy.sql.core.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 默认基于内存的Catalog
 *
 * @author zhangap
 * @version 1.0, 2022/4/20
 */
public class DefaultMemoryCatalog extends AbstractCatalog {

    public static final String DEFAULT_DB = "default";

    private final Map<String, CatalogDatabase> databases;
    private final Map<ObjectPath, CatalogBaseTable> tables;
    //private final Map<ObjectPath, CatalogFunction> functions;
    //private final Map<ObjectPath, Map<CatalogPartitionSpec, CatalogPartition>> partitions;

    public DefaultMemoryCatalog(String name) {
        this(name, DEFAULT_DB);
    }

    public DefaultMemoryCatalog(String name, String defaultDatabase) {
        super(name, defaultDatabase);

        this.databases = new LinkedHashMap<>();
        this.databases.put(defaultDatabase, new CatalogDatabaseImpl(new HashMap<>(), null));
        this.tables = new LinkedHashMap<>();
        //this.functions = new LinkedHashMap<>();
        //this.partitions = new LinkedHashMap<>();
    }

    @Override
    public void open() throws CatalogException {
    }

    @Override
    public void close() throws CatalogException {
    }

    @Override
    public List<String> listDatabases() throws CatalogException {
        return null;
    }

    @Override
    public boolean databaseExists(String databaseName) throws CatalogException {
        return false;
    }

    @Override
    public void createDatabase(String databaseName, CatalogDatabase db, boolean ignoreIfExists) throws DatabaseAlreadyExistException, CatalogException {
        checkArgument(!StringUtils.isNullOrWhitespaceOnly(databaseName));
        checkNotNull(db);

        if (databaseExists(databaseName)) {
            if (!ignoreIfExists) {
                throw new DatabaseAlreadyExistException(getName(), databaseName);
            }
        } else {
            databases.put(databaseName, db.copy());
        }
    }

    @Override
    public void dropDatabase(String name, boolean ignoreIfNotExists, boolean cascade) throws DatabaseNotExistException, DatabaseNotEmptyException, CatalogException {

    }

    @Override
    public void alterDatabase(String name, CatalogDatabase newDatabase, boolean ignoreIfNotExists) throws DatabaseNotExistException, CatalogException {

    }

    @Override
    public List<String> listTables(String databaseName) throws DatabaseNotExistException, CatalogException {
        return null;
    }

    @Override
    public List<String> listViews(String databaseName) throws DatabaseNotExistException, CatalogException {
        return null;
    }

    @Override
    public CatalogBaseTable getTable(ObjectPath tablePath) throws TableNotExistException, CatalogException {
        return null;
    }

    @Override
    public boolean tableExists(ObjectPath tablePath) throws CatalogException {
        return false;
    }

    @Override
    public void dropTable(ObjectPath tablePath, boolean ignoreIfNotExists) throws TableNotExistException, CatalogException {

    }

    @Override
    public void renameTable(ObjectPath tablePath, String newTableName, boolean ignoreIfNotExists) throws TableNotExistException, TableAlreadyExistException, CatalogException {

    }

    @Override
    public void createTable(ObjectPath tablePath, CatalogBaseTable table, boolean ignoreIfExists) throws TableAlreadyExistException, DatabaseNotExistException, CatalogException {

    }

    @Override
    public void alterTable(ObjectPath tablePath, CatalogBaseTable newTable, boolean ignoreIfNotExists) throws TableNotExistException, CatalogException {

    }
}
