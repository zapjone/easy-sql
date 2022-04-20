package com.easy.sql.core.planner.plan;

import com.easy.sql.core.exceptions.ValidationException;
import com.easy.sql.core.planner.calcite.EasySqlNameMatcher;
import com.easy.sql.core.planner.catalog.CatalogBaseTable;
import com.easy.sql.core.planner.catalog.CatalogSchemaTable;
import com.easy.sql.core.planner.catalog.ObjectIdentifier;
import com.easy.sql.core.planner.plan.schema.EasySqlPreparingTableBase;
import org.apache.calcite.config.CalciteConnectionConfig;
import org.apache.calcite.jdbc.CalciteSchema;
import org.apache.calcite.plan.RelOptSchema;
import org.apache.calcite.prepare.CalciteCatalogReader;
import org.apache.calcite.prepare.Prepare;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.sql.validate.SqlNameMatchers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhangap
 * @version 1.0, 2022/4/20
 */
public class EasySqlCalciteCatalogReader extends CalciteCatalogReader {

    public EasySqlCalciteCatalogReader(CalciteSchema rootSchema,
                                       List<List<String>> defaultSchemas,
                                       RelDataTypeFactory typeFactory,
                                       CalciteConnectionConfig config) {
        super(rootSchema,
                new EasySqlNameMatcher(SqlNameMatchers.withCaseSensitive(config != null && config.caseSensitive()),
                        typeFactory),
                Stream.concat(defaultSchemas.stream(), Stream.of(Collections.<String>emptyList()))
                        .collect(Collectors.toList()),
                typeFactory, config);
    }

/*
    @Override
    public Prepare.PreparingTable getTable(List<String> names) {
        Prepare.PreparingTable originRelOptTable = super.getTable(names);
        if (originRelOptTable == null) {
            return null;
        } else {
            // Wrap as FlinkPreparingTableBase to use in query optimization.
            CatalogSchemaTable table = originRelOptTable.unwrap(CatalogSchemaTable.class);
            if (table != null) {
                return toPreparingTable(
                        originRelOptTable.getRelOptSchema(),
                        originRelOptTable.getQualifiedName(),
                        originRelOptTable.getRowType(),
                        table);
            } else {
                return originRelOptTable;
            }
        }
    }

    *//**
     * Translate this {@link CatalogSchemaTable} into Flink source table.
     *//*
    private static EasySqlPreparingTableBase toPreparingTable(
            RelOptSchema relOptSchema,
            List<String> names,
            RelDataType rowType,
            CatalogSchemaTable schemaTable) {
        final ResolvedCatalogBaseTable<?> resolvedBaseTable = schemaTable.getResolvedCatalogTable();
        final CatalogBaseTable originTable = resolvedBaseTable.getOrigin();
        if (originTable instanceof QueryOperationCatalogView) {
            return convertQueryOperationView(
                    relOptSchema, names, rowType, (QueryOperationCatalogView) originTable);
        } else if (originTable instanceof ConnectorCatalogTable) {
            ConnectorCatalogTable<?, ?> connectorTable = (ConnectorCatalogTable<?, ?>) originTable;
            if ((connectorTable).getTableSource().isPresent()) {
                return convertSourceTable(
                        relOptSchema,
                        rowType,
                        schemaTable.getTableIdentifier(),
                        connectorTable);
            } else {
                throw new ValidationException(
                        "Cannot convert a connector table " + "without source.");
            }
        } else if (originTable instanceof CatalogView) {
            return convertCatalogView(
                    relOptSchema,
                    names,
                    rowType,
                    schemaTable.getStatistic(),
                    (CatalogView) originTable);
        } else if (originTable instanceof CatalogTable) {
            return convertCatalogTable(
                    relOptSchema,
                    names,
                    rowType,
                    (ResolvedCatalogTable) resolvedBaseTable,
                    schemaTable);
        } else {
            throw new ValidationException("Unsupported table type: " + originTable);
        }
    }

    private static EasySqlPreparingTableBase convertQueryOperationView(
            RelOptSchema relOptSchema,
            List<String> names,
            RelDataType rowType,
            QueryOperationCatalogView view) {
        return QueryOperationCatalogViewTable.create(relOptSchema, names, rowType, view);
    }

    private static EasySqlPreparingTableBase convertCatalogView(
            RelOptSchema relOptSchema,
            List<String> names,
            RelDataType rowType,
            CatalogView view) {
        return new SqlCatalogViewTable(
                relOptSchema, rowType, names, view, names.subList(0, 2));
    }

    private static EasySqlPreparingTableBase convertSourceTable(
            RelOptSchema relOptSchema,
            RelDataType rowType,
            ObjectIdentifier tableIdentifier,
            ConnectorCatalogTable<?, ?> table) {
        TableSource<?> tableSource = table.getTableSource().get();
        if (!(tableSource instanceof StreamTableSource
                || tableSource instanceof LookupableTableSource)) {
            throw new ValidationException(
                    "Only StreamTableSource and LookupableTableSource can be used in planner.");
        }
        if (tableSource instanceof StreamTableSource
                && !((StreamTableSource<?>) tableSource).isBounded()) {
            throw new ValidationException(
                    "Only bounded StreamTableSource can be used in batch mode.");
        }

        return new LegacyTableSourceTable<>(
                relOptSchema,
                tableIdentifier,
                rowType,
                statistic,
                tableSource,
                isStreamingMode,
                table);
    }

    private static EasySqlPreparingTableBase convertCatalogTable(
            RelOptSchema relOptSchema,
            List<String> names,
            RelDataType rowType,
            ResolvedCatalogTable catalogTable,
            CatalogSchemaTable schemaTable) {
        if (isLegacySourceOptions(catalogTable.getOrigin(), schemaTable)) {
            return new LegacyCatalogSourceTable<>(
                    relOptSchema, names, rowType, schemaTable, catalogTable.getOrigin());
        } else {
            return new CatalogSourceTable(relOptSchema, names, rowType, schemaTable, catalogTable);
        }
    }

    *//**
     * Checks whether the {@link CatalogTable} uses legacy connector source options.
     *//*
    private static boolean isLegacySourceOptions(
            CatalogTable catalogTable, CatalogSchemaTable schemaTable) {
        // normalize option keys
        DescriptorProperties properties = new DescriptorProperties(true);
        properties.putProperties(catalogTable.getOptions());
        if (properties.containsKey(ConnectorDescriptorValidator.CONNECTOR_TYPE)) {
            return true;
        } else {
            // try to create legacy table source using the options,
            // some legacy factories uses the new 'connector' key
            try {
                TableFactoryUtil.findAndCreateTableSource(
                        schemaTable.getCatalog().orElse(null),
                        schemaTable.getTableIdentifier(),
                        catalogTable,
                        new Configuration(),
                        schemaTable.isTemporary());
                // success, then we will use the legacy factories
                return true;
            } catch (Throwable e) {
                // fail, then we will use new factories
                return false;
            }
        }
    }*/
}
