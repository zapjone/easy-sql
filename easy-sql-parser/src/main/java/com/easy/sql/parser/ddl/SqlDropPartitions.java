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
 * ALTER TABLE DDL to drop partitions of a table
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlDropPartitions extends SqlAlterTable {


    private final boolean ifExists;
    private final List<SqlNodeList> partSpecs;

    public SqlDropPartitions(
            SqlParserPos pos,
            SqlIdentifier tableName,
            boolean ifExists,
            List<SqlNodeList> partSpecs) {
        super(pos, tableName);
        this.ifExists = ifExists;
        this.partSpecs = partSpecs;
    }

    public boolean ifExists() {
        return ifExists;
    }

    public List<SqlNodeList> getPartSpecs() {
        return partSpecs;
    }

    public LinkedHashMap<String, String> getPartitionKVs(int i) {
        return SqlPartitionUtils.getPartitionKVs(getPartSpecs().get(i));
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        super.unparse(writer, leftPrec, rightPrec);
        writer.newlineAndIndent();
        writer.keyword("DROP");
        if (ifExists) {
            writer.keyword("IF EXISTS");
        }
        int opLeftPrec = getOperator().getLeftPrec();
        int opRightPrec = getOperator().getRightPrec();
        final SqlWriter.Frame frame = writer.startList("", "");
        for (SqlNodeList partSpec : partSpecs) {
            writer.sep(",");
            writer.newlineAndIndent();
            writer.keyword("PARTITION");
            partSpec.unparse(writer, opLeftPrec, opRightPrec);
        }
        writer.endList(frame);
    }

    @Nonnull
    @Override
    public List<SqlNode> getOperandList() {
        List<SqlNode> operands = new ArrayList<>();
        operands.add(tableIdentifier);
        operands.addAll(partSpecs);
        return operands;
    }
}
