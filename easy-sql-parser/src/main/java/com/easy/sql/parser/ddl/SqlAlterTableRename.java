package com.easy.sql.parser.ddl;

import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.util.ImmutableNullableList;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * ALTER TABLE [[catalogName.] dataBasesName].tableName RENAME TO [[catalogName.]
 * dataBasesName].newTableName.
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlAlterTableRename extends SqlAlterTable {


    private final SqlIdentifier newTableIdentifier;

    public SqlAlterTableRename(
            SqlParserPos pos, SqlIdentifier tableName, SqlIdentifier newTableName) {
        super(pos, tableName);
        this.newTableIdentifier = requireNonNull(newTableName, "new tableName should not be null");
    }

    @Override
    public List<SqlNode> getOperandList() {
        return ImmutableNullableList.of(tableIdentifier, newTableIdentifier);
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        super.unparse(writer, leftPrec, rightPrec);
        writer.keyword("RENAME TO");
        newTableIdentifier.unparse(writer, leftPrec, rightPrec);
    }

    public String[] fullNewTableName() {
        return newTableIdentifier.names.toArray(new String[0]);
    }

}
