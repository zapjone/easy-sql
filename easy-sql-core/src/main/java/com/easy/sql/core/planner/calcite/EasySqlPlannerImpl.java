package com.easy.sql.core.planner.calcite;

import com.easy.sql.core.exceptions.ValidationException;
import com.easy.sql.core.planner.parser.CalciteParser;
import com.easy.sql.core.planner.plan.EasySqlCalciteCatalogReader;
import com.easy.sql.parser.ddl.SqlReset;
import com.easy.sql.parser.ddl.SqlSet;
import com.easy.sql.parser.dml.SqlBeginStatementSet;
import com.easy.sql.parser.dml.SqlEndStatementSet;
import com.easy.sql.parser.dql.SqlRichDescribeTable;
import com.easy.sql.parser.dql.SqlShowCatalogs;
import com.easy.sql.parser.dql.SqlShowCurrentCatalog;
import com.easy.sql.parser.dql.SqlShowCurrentDatabase;
import com.easy.sql.parser.dql.SqlShowDatabases;
import com.easy.sql.parser.dql.SqlShowFunctions;
import com.easy.sql.parser.dql.SqlShowPartitions;
import com.easy.sql.parser.dql.SqlShowTables;
import com.easy.sql.parser.dql.SqlShowViews;
import com.google.common.collect.ImmutableList;
import org.apache.calcite.config.NullCollation;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.prepare.CalciteCatalogReader;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.rel.hint.RelHint;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperatorTable;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.sql2rel.SqlRexConvertletTable;
import org.apache.calcite.sql2rel.SqlToRelConverter;
import org.apache.calcite.tools.FrameworkConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author zhangap
 * @version 1.0, 2022/4/20
 */
public class EasySqlPlannerImpl {

    private final FrameworkConfig config;
    private final Function<Boolean, CalciteCatalogReader> catalogReaderSupplier;
    private final EasySqlTypeFactory typeFactory;
    private final RelOptCluster cluster;

    private final CalciteParser parser;
    private final SqlRexConvertletTable convertletTable;
    private final SqlToRelConverter.Config sqlToRelConverterConfig;

    private EasyCalciteSqlValidator validator;
    private SqlOperatorTable operatorTable;

    public EasySqlPlannerImpl(FrameworkConfig config,
                              Function<Boolean, CalciteCatalogReader> catalogReaderSupplier,
                              EasySqlTypeFactory typeFactory,
                              RelOptCluster cluster) {
        this.config = config;
        this.catalogReaderSupplier = catalogReaderSupplier;
        this.typeFactory = typeFactory;
        this.cluster = cluster;

        this.operatorTable = config.getOperatorTable();
        this.parser = new CalciteParser(config.getParserConfig());
        this.convertletTable = config.getConvertletTable();
        this.sqlToRelConverterConfig = config.getSqlToRelConverterConfig();
    }

    /**
     * 对SqlNode进行校验
     *
     * @param sqlNode 校验前的SqlNode
     * @return 校验后的SqlNode
     */
    public SqlNode validate(SqlNode sqlNode) {
        return validate(sqlNode, getOrCreateSqlValidator());
    }

    private SqlNode validate(SqlNode sqlNode, EasyCalciteSqlValidator sqlValidator) {
        try {
            if (sqlNode.getKind().belongsTo(SqlKind.DDL)
                    || sqlNode.getKind() == SqlKind.INSERT
                    || sqlNode.getKind() == SqlKind.CREATE_FUNCTION
                    || sqlNode.getKind() == SqlKind.DROP_FUNCTION
                    || sqlNode.getKind() == SqlKind.OTHER_DDL
                    || sqlNode instanceof SqlShowCatalogs
                    || sqlNode instanceof SqlShowCurrentCatalog
                    || sqlNode instanceof SqlShowDatabases
                    || sqlNode instanceof SqlShowCurrentDatabase
                    || sqlNode instanceof SqlShowTables
                    || sqlNode instanceof SqlShowFunctions
                    || sqlNode instanceof SqlShowViews
                    || sqlNode instanceof SqlShowPartitions
                    || sqlNode instanceof SqlRichDescribeTable
                    || sqlNode instanceof SqlBeginStatementSet
                    || sqlNode instanceof SqlEndStatementSet
                    || sqlNode instanceof SqlSet
                    || sqlNode instanceof SqlReset) {
                return sqlNode;
            }

            return sqlValidator.validate(sqlNode);
        } catch (Exception e) {
            throw new ValidationException("SQL validation failed. ${e.getMessage}", e);
        }
    }

    /**
     * 将SqlNode转换为RelNode,主要用于查询时
     */
    public RelRoot rel(SqlNode validateSqlNode) {
        return rel(validateSqlNode, getOrCreateSqlValidator());
    }

    /**
     * 将SqlNode转换为RelNode
     */
    private RelRoot rel(SqlNode sqlNode, EasyCalciteSqlValidator sqlValidator) {
        SqlToRelConverter sqlToRelConverter = createSqlToRelConverter(sqlValidator);
        return sqlToRelConverter.convertQuery(sqlNode, false, false);
    }

    private SqlToRelConverter createSqlToRelConverter(EasyCalciteSqlValidator sqlValidator) {
        return new SqlToRelConverter(createToRelContext(),
                sqlValidator, sqlValidator.getCatalogReader().unwrap(CalciteCatalogReader.class),
                cluster, convertletTable, sqlToRelConverterConfig);
    }

    private RelOptTable.ToRelContext createToRelContext() {
        return new ToRelContextImpl();
    }

    /**
     * 创建sql校验器
     */
    private EasyCalciteSqlValidator getOrCreateSqlValidator() {
        if (validator == null) {
            CalciteCatalogReader calciteCatalogReader = catalogReaderSupplier.apply(false);
            validator = createSqlValidator(calciteCatalogReader);
        }
        return validator;
    }

    private EasyCalciteSqlValidator createSqlValidator(CalciteCatalogReader catalogReader) {
        return new EasyCalciteSqlValidator(operatorTable,
                catalogReader,
                typeFactory,
                SqlValidator.Config.DEFAULT
                        .withIdentifierExpansion(true)
                        .withDefaultNullCollation(NullCollation.LOW)
                        .withTypeCoercionEnabled(false));
    }

    public class ToRelContextImpl implements RelOptTable.ToRelContext {

        @Override
        public RelOptCluster getCluster() {
            return cluster;
        }

        @Override
        public List<RelHint> getTableHints() {
            return ImmutableList.of();
        }

        @Override
        public RelRoot expandView(RelDataType rowType,
                                  String queryString,
                                  List<String> schemaPath,
                                  List<String> viewPath) {
            SqlNode parsed = parser.parse(queryString);

            CalciteCatalogReader originalReader = catalogReaderSupplier.apply(false);

            List<List<String>> defaultSchemas = new ArrayList<>();
            defaultSchemas.add(schemaPath);
            defaultSchemas.add(schemaPath.subList(0, 1));
            EasySqlCalciteCatalogReader readerWithPathAdjusted = new EasySqlCalciteCatalogReader(originalReader.getRootSchema(),
                    defaultSchemas, originalReader.getTypeFactory(), originalReader.getConfig());

            EasyCalciteSqlValidator validator = createSqlValidator(readerWithPathAdjusted);
            return rel(validate(parsed, validator));
        }
    }

}
