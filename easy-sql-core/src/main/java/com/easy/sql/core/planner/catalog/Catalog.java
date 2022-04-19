package com.easy.sql.core.planner.catalog;

import com.easy.sql.core.exceptions.CatalogException;
import com.easy.sql.core.exceptions.DatabaseAlreadyExistException;
import com.easy.sql.core.exceptions.DatabaseNotEmptyException;
import com.easy.sql.core.exceptions.DatabaseNotExistException;
import com.easy.sql.core.exceptions.TableAlreadyExistException;
import com.easy.sql.core.exceptions.TableNotExistException;
import com.easy.sql.core.factories.Factory;

import java.util.List;
import java.util.Optional;

/**
 * easy-sql的Catalog接口，后续将使用spi进行加载
 *
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public interface Catalog {

    default Optional<Factory> getFactory() {
        return Optional.empty();
    }

    /**
     * 打开
     */
    void open() throws CatalogException;

    /**
     * 关闭
     */
    void close() throws CatalogException;

    /**
     * 获取默认数据库名称
     */
    String getDefaultDatabase() throws CatalogException;

    /**
     * 列出所有数据库名称
     */
    List<String> listDatabases() throws CatalogException;

    /**
     * 判断数据库是否存在
     */
    boolean databaseExists(String databaseName) throws CatalogException;

    /**
     * 创建数据库
     */
    void createDatabase(String name, CatalogDatabase database, boolean ignoreIfExists)
            throws DatabaseAlreadyExistException, CatalogException;

    /**
     * 删除数据库
     */
    default void dropDatabase(String name, boolean ignoreIfNotExists)
            throws DatabaseNotExistException, DatabaseNotEmptyException, CatalogException {
        dropDatabase(name, ignoreIfNotExists, false);
    }

    void dropDatabase(String name, boolean ignoreIfNotExists, boolean cascade)
            throws DatabaseNotExistException, DatabaseNotEmptyException, CatalogException;

    /**
     * 修改数据库
     */
    void alterDatabase(String name, CatalogDatabase newDatabase, boolean ignoreIfNotExists)
            throws DatabaseNotExistException, CatalogException;

    // ------ tables and views ------

    /**
     * 所有表
     */
    List<String> listTables(String databaseName) throws DatabaseNotExistException, CatalogException;

    /**
     * 所有视图
     */
    List<String> listViews(String databaseName) throws DatabaseNotExistException, CatalogException;

    /**
     * 获取表
     */
    CatalogBaseTable getTable(ObjectPath tablePath) throws TableNotExistException, CatalogException;

    /**
     * 表是否存在
     */
    boolean tableExists(ObjectPath tablePath) throws CatalogException;

    /**
     * 删除表
     */
    void dropTable(ObjectPath tablePath, boolean ignoreIfNotExists)
            throws TableNotExistException, CatalogException;

    /**
     * 表重命名
     */
    void renameTable(ObjectPath tablePath, String newTableName, boolean ignoreIfNotExists)
            throws TableNotExistException, TableAlreadyExistException, CatalogException;

    /**
     * 创建表
     */
    void createTable(ObjectPath tablePath, CatalogBaseTable table, boolean ignoreIfExists)
            throws TableAlreadyExistException, DatabaseNotExistException, CatalogException;

    /**
     * 修改表
     */
    void alterTable(ObjectPath tablePath, CatalogBaseTable newTable, boolean ignoreIfNotExists)
            throws TableNotExistException, CatalogException;
}
