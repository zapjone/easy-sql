package com.easy.sql.core.planner.calcite;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.rel.type.RelDataTypeFieldImpl;
import org.apache.calcite.sql.validate.SqlNameMatcher;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangap
 * @version 1.0, 2022/4/20
 */
public class EasySqlNameMatcher implements SqlNameMatcher {

    private final SqlNameMatcher baseMatcher;
    private final RelDataTypeFactory typeFactory;

    public EasySqlNameMatcher(SqlNameMatcher baseMatcher, RelDataTypeFactory typeFactory) {
        this.baseMatcher = baseMatcher;
        this.typeFactory = typeFactory;
    }

    @Override
    public boolean isCaseSensitive() {
        return baseMatcher.isCaseSensitive();
    }

    @Override
    public boolean matches(String string, String name) {
        return baseMatcher.matches(string, name);
    }

    @Override
    public <K extends List<String>, V> V get(
            Map<K, V> map, List<String> prefixNames, List<String> names) {
        return baseMatcher.get(map, prefixNames, names);
    }

    @Override
    public String bestString() {
        return baseMatcher.bestString();
    }

    /**
     * Compared to the original method we adjust the nullability of the nested column based on the
     * nullability of the enclosing type.
     *
     * <p>If the fields type is NOT NULL, but the enclosing ROW is nullable we still can produce
     * nulls.
     */
    @Override
    public RelDataTypeField field(RelDataType rowType, String fieldName) {
        RelDataTypeField field = baseMatcher.field(rowType, fieldName);
        if (field != null && rowType.isNullable() && !field.getType().isNullable()) {
            RelDataType typeWithNullability =
                    typeFactory.createTypeWithNullability(field.getType(), true);
            return new RelDataTypeFieldImpl(field.getName(), field.getIndex(), typeWithNullability);
        }

        return field;
    }

    @Override
    public int frequency(Iterable<String> names, String name) {
        return baseMatcher.frequency(names, name);
    }

    @Override
    public Set<String> createSet() {
        return baseMatcher.createSet();
    }
}
