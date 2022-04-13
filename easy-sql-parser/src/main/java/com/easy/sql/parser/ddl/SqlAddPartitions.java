package com.easy.sql.parser.ddl;

import com.easy.sql.parser.SqlPartitionUtils;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * ALTER TABLE DDL to add partitions to a table.
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlAddPartitions extends SqlAlterTable {

    private final boolean ifNotExists;
    private final List<SqlNodeList> partSpecs;
    private final List<SqlNodeList> partProps;

    public SqlAddPartitions(
            SqlParserPos pos,
            SqlIdentifier tableName,
            boolean ifNotExists,
            List<SqlNodeList> partSpecs,
            List<SqlNodeList> partProps) {
        super(pos, tableName);
        this.ifNotExists = ifNotExists;
        this.partSpecs = partSpecs;
        this.partProps = partProps;
    }

    public boolean ifNotExists() {
        return ifNotExists;
    }

    public List<SqlNodeList> getPartSpecs() {
        return partSpecs;
    }

    public LinkedHashMap<String, String> getPartitionKVs(int i) {
        return SqlPartitionUtils.getPartitionKVs(getPartSpecs().get(i));
    }

    public List<SqlNodeList> getPartProps() {
        return partProps;
    }

    @Nonnull
    @Override
    public List<SqlNode> getOperandList() {
        List<SqlNode> operands = new ArrayList<>();
        operands.add(tableIdentifier);
        operands.addAll(partSpecs);
        operands.addAll(partProps);
        return operands;
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        super.unparse(writer, leftPrec, rightPrec);
        writer.newlineAndIndent();
        writer.keyword("ADD");
        if (ifNotExists) {
            writer.keyword("IF NOT EXISTS");
        }
        int opLeftPrec = getOperator().getLeftPrec();
        int opRightPrec = getOperator().getRightPrec();
        for (int i = 0; i < partSpecs.size(); i++) {
            writer.newlineAndIndent();
            SqlNodeList partSpec = partSpecs.get(i);
            SqlNodeList partProp = partProps.get(i);
            writer.keyword("PARTITION");
            partSpec.unparse(writer, opLeftPrec, opRightPrec);
            if (partProp != null) {
                writer.keyword("WITH");
                partProp.unparse(writer, opLeftPrec, opRightPrec);
            }
        }
    }

}
