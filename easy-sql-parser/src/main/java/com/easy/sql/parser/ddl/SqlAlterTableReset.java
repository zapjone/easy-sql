package com.easy.sql.parser.ddl;

import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.util.ImmutableNullableList;
import org.apache.calcite.util.NlsString;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * ALTER TABLE [[catalogName.] dataBasesName].tableName RESET ( 'key1' [, 'key2']*).
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlAlterTableReset extends SqlAlterTable {
    private final SqlNodeList propertyKeyList;

    public SqlAlterTableReset(
            SqlParserPos pos, SqlIdentifier tableName, SqlNodeList propertyKeyList) {
        super(pos, tableName, null);
        this.propertyKeyList =
                requireNonNull(propertyKeyList, "propertyKeyList should not be null");
    }

    @Override
    public List<SqlNode> getOperandList() {
        return ImmutableNullableList.of(tableIdentifier, propertyKeyList);
    }

    public SqlNodeList getPropertyKeyList() {
        return propertyKeyList;
    }

    public Set<String> getResetKeys() {
        return propertyKeyList.getList().stream()
                .map(key -> ((NlsString) SqlLiteral.value(key)).getValue())
                .collect(Collectors.toSet());
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        super.unparse(writer, leftPrec, rightPrec);
        writer.keyword("RESET");
        SqlWriter.Frame withFrame = writer.startList("(", ")");
        for (SqlNode property : propertyKeyList) {
            printIndent(writer);
            property.unparse(writer, leftPrec, rightPrec);
        }
        writer.newlineAndIndent();
        writer.endList(withFrame);
    }

    protected void printIndent(SqlWriter writer) {
        writer.sep(",", false);
        writer.newlineAndIndent();
        writer.print("  ");
    }

    @Override
    public String[] fullTableName() {
        return tableIdentifier.names.toArray(new String[0]);
    }
}
