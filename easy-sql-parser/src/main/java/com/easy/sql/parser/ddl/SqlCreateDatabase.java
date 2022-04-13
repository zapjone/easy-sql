package com.easy.sql.parser.ddl;

import org.apache.calcite.sql.SqlCharStringLiteral;
import org.apache.calcite.sql.SqlCreate;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.SqlSpecialOperator;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.util.ImmutableNullableList;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * CREATE Database DDL sql call.
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlCreateDatabase extends SqlCreate {

    public static final SqlSpecialOperator OPERATOR =
            new SqlSpecialOperator("CREATE DATABASE", SqlKind.OTHER_DDL);

    private final SqlIdentifier databaseName;

    private final SqlNodeList propertyList;

    @Nullable
    private final SqlCharStringLiteral comment;

    public SqlCreateDatabase(
            SqlParserPos pos,
            SqlIdentifier databaseName,
            SqlNodeList propertyList,
            SqlCharStringLiteral comment,
            boolean ifNotExists) {
        super(OPERATOR, pos, false, ifNotExists);
        this.databaseName = requireNonNull(databaseName, "databaseName should not be null");
        this.propertyList = requireNonNull(propertyList, "propertyList should not be null");
        this.comment = comment;
    }

    @Override
    public SqlOperator getOperator() {
        return OPERATOR;
    }

    @Override
    public List<SqlNode> getOperandList() {
        return ImmutableNullableList.of(databaseName, propertyList, comment);
    }

    public SqlIdentifier getDatabaseName() {
        return databaseName;
    }

    public SqlNodeList getPropertyList() {
        return propertyList;
    }

    public Optional<SqlCharStringLiteral> getComment() {
        return Optional.ofNullable(comment);
    }

    public boolean isIfNotExists() {
        return ifNotExists;
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword("CREATE DATABASE");
        if (isIfNotExists()) {
            writer.keyword("IF NOT EXISTS");
        }
        databaseName.unparse(writer, leftPrec, rightPrec);

        if (comment != null) {
            writer.newlineAndIndent();
            writer.keyword("COMMENT");
            comment.unparse(writer, leftPrec, rightPrec);
        }

        if (this.propertyList.size() > 0) {
            writer.keyword("WITH");
            SqlWriter.Frame withFrame = writer.startList("(", ")");
            for (SqlNode property : propertyList) {
                printIndent(writer);
                property.unparse(writer, leftPrec, rightPrec);
            }
            writer.newlineAndIndent();
            writer.endList(withFrame);
        }
    }

    protected void printIndent(SqlWriter writer) {
        writer.sep(",", false);
        writer.newlineAndIndent();
        writer.print("  ");
    }

    public String[] fullDatabaseName() {
        return databaseName.names.toArray(new String[0]);
    }

}
