package com.easy.sql.core.planner.catalog;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.easy.sql.core.util.EncodingUtils.escapeIdentifier;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class ObjectIdentifier {
    private final String catalogName;

    private final String databaseName;

    private final String objectName;

    public static ObjectIdentifier of(String catalogName, String databaseName, String objectName) {
        return new ObjectIdentifier(catalogName, databaseName, objectName);
    }

    private ObjectIdentifier(String catalogName, String databaseName, String objectName) {
        this.catalogName =
                Preconditions.checkNotNull(catalogName, "Catalog name must not be null.");
        this.databaseName =
                Preconditions.checkNotNull(databaseName, "Database name must not be null.");
        this.objectName = Preconditions.checkNotNull(objectName, "Object name must not be null.");
    }

    public String getCatalogName() {
        return catalogName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getObjectName() {
        return objectName;
    }

    public ObjectPath toObjectPath() {
        return new ObjectPath(databaseName, objectName);
    }

    /** List of the component names of this object identifier. */
    public List<String> toList() {
        return Arrays.asList(getCatalogName(), getDatabaseName(), getObjectName());
    }

    /**
     * Returns a string that fully serializes this instance. The serialized string can be used for
     * transmitting or persisting an object identifier.
     */
    public String asSerializableString() {
        return String.format(
                "%s.%s.%s",
                escapeIdentifier(catalogName),
                escapeIdentifier(databaseName),
                escapeIdentifier(objectName));
    }

    /** Returns a string that summarizes this instance for printing to a console or log. */
    public String asSummaryString() {
        return String.join(".", catalogName, databaseName, objectName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ObjectIdentifier that = (ObjectIdentifier) o;
        return catalogName.equals(that.catalogName)
                && databaseName.equals(that.databaseName)
                && objectName.equals(that.objectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(catalogName, databaseName, objectName);
    }

    @Override
    public String toString() {
        return asSerializableString();
    }
}
