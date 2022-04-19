package com.easy.sql.core.planner.catalog;

import com.easy.sql.core.util.StringUtils;

import java.io.Serializable;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class ObjectPath implements Serializable {

    private final String databaseName;
    private final String objectName;

    public ObjectPath(String databaseName, String objectName) {
        checkArgument(
                !StringUtils.isNullOrWhitespaceOnly(databaseName),
                "databaseName cannot be null or empty");
        checkArgument(
                !StringUtils.isNullOrWhitespaceOnly(objectName),
                "objectName cannot be null or empty");

        this.databaseName = databaseName;
        this.objectName = objectName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getFullName() {
        return String.format("%s.%s", databaseName, objectName);
    }

    public static ObjectPath fromString(String fullName) {
        checkArgument(
                !StringUtils.isNullOrWhitespaceOnly(fullName), "fullName cannot be null or empty");

        String[] paths = fullName.split("\\.");

        if (paths.length != 2) {
            throw new IllegalArgumentException(
                    String.format(
                            "Cannot get split '%s' to get databaseName and objectName", fullName));
        }

        return new ObjectPath(paths[0], paths[1]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ObjectPath that = (ObjectPath) o;

        return Objects.equals(databaseName, that.databaseName)
                && Objects.equals(objectName, that.objectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(databaseName, objectName);
    }

    @Override
    public String toString() {
        return String.format("%s.%s", databaseName, objectName);
    }
}
