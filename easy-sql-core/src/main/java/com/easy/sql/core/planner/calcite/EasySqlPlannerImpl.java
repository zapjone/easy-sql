package com.easy.sql.core.planner.calcite;

import com.easy.sql.core.exceptions.ValidationException;
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
import org.apache.calcite.config.NullCollation;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.prepare.CalciteCatalogReader;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperatorTable;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.tools.FrameworkConfig;

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

        operatorTable = config.getOperatorTable();
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
     * 创建sql校验器
     */
    private EasyCalciteSqlValidator getOrCreateSqlValidator() {
        if (validator == null) {
            CalciteCatalogReader calciteCatalogReader = catalogReaderSupplier.apply(false);
            validator = new EasyCalciteSqlValidator(operatorTable,
                    calciteCatalogReader,
                    typeFactory,
                    SqlValidator.Config.DEFAULT
                            .withIdentifierExpansion(true)
                            .withDefaultNullCollation(NullCollation.LOW)
                            .withTypeCoercionEnabled(false));
        }
        return validator;
    }

}
