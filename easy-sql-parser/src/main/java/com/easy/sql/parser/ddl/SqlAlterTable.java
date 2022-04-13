package com.easy.sql.parser.ddl;

import com.easy.sql.parser.SqlPartitionUtils;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.SqlSpecialOperator;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;

import javax.annotation.Nullable;

import java.util.LinkedHashMap;

import static java.util.Objects.requireNonNull;

/**
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public abstract class SqlAlterTable extends SqlCall {

    public static final SqlSpecialOperator OPERATOR =
            new SqlSpecialOperator("ALTER TABLE", SqlKind.ALTER_TABLE);

    protected final SqlIdentifier tableIdentifier;
    protected final SqlNodeList partitionSpec;

    public SqlAlterTable(
            SqlParserPos pos, SqlIdentifier tableName, @Nullable SqlNodeList partitionSpec) {
        super(pos);
        this.tableIdentifier = requireNonNull(tableName, "tableName should not be null");
        this.partitionSpec = partitionSpec;
    }

    public SqlAlterTable(SqlParserPos pos, SqlIdentifier tableName) {
        this(pos, tableName, null);
    }

    @Override
    public SqlOperator getOperator() {
        return OPERATOR;
    }

    public SqlIdentifier getTableName() {
        return tableIdentifier;
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword("ALTER TABLE");
        tableIdentifier.unparse(writer, leftPrec, rightPrec);
        SqlNodeList partitionSpec = getPartitionSpec();
        if (partitionSpec != null && partitionSpec.size() > 0) {
            writer.keyword("PARTITION");
            partitionSpec.unparse(
                    writer, getOperator().getLeftPrec(), getOperator().getRightPrec());
        }
    }

    public String[] fullTableName() {
        return tableIdentifier.names.toArray(new String[0]);
    }

    /**
     * Returns the partition spec if the ALTER should be applied to partitions, and null otherwise.
     */
    public SqlNodeList getPartitionSpec() {
        return partitionSpec;
    }

    /**
     * Get partition spec as key-value strings.
     */
    public LinkedHashMap<String, String> getPartitionKVs() {
        return SqlPartitionUtils.getPartitionKVs(getPartitionSpec());
    }

}
