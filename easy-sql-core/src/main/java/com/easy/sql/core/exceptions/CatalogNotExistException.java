package com.easy.sql.core.exceptions;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class CatalogNotExistException  extends RuntimeException {

    public CatalogNotExistException(String catalogName) {
        this(catalogName, null);
    }

    public CatalogNotExistException(String catalogName, Throwable cause) {
        super("Catalog " + catalogName + " does not exist.", cause);
    }
}

