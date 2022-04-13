package com.easy.sql.parser.type;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.sql.SqlCharStringLiteral;
import org.apache.calcite.sql.SqlDataTypeSpec;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlRowTypeNameSpec;
import org.apache.calcite.sql.SqlTypeNameSpec;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.util.Litmus;
import org.apache.calcite.util.Pair;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class ExtendedSqlRowTypeNameSpec extends SqlTypeNameSpec {

    private final List<SqlIdentifier> fieldNames;
    private final List<SqlDataTypeSpec> fieldTypes;
    private final List<SqlCharStringLiteral> comments;

    private final boolean unparseAsStandard;

    public ExtendedSqlRowTypeNameSpec(SqlParserPos pos,
                                      List<SqlIdentifier> fieldNames,
                                      List<SqlDataTypeSpec> fieldTypes,
                                      List<SqlCharStringLiteral> comments,
                                      boolean unparseAsStandard) {
        super(new SqlIdentifier(SqlTypeName.ROW.getName(), pos), pos);
        this.fieldNames = fieldNames;
        this.fieldTypes = fieldTypes;
        this.comments = comments;
        this.unparseAsStandard = unparseAsStandard;
    }

    public List<SqlIdentifier> getFieldNames() {
        return fieldNames;
    }

    public List<SqlDataTypeSpec> getFieldTypes() {
        return fieldTypes;
    }

    public List<SqlCharStringLiteral> getComments() {
        return comments;
    }

    public boolean unparseAsStandard() {
        return unparseAsStandard;
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.print("ROW");
        if (getFieldNames().size() == 0) {
            if (unparseAsStandard) {
                writer.print("()");
            } else {
                writer.print("<>");
            }
        } else {
            SqlWriter.Frame frame;
            if (unparseAsStandard) {
                frame = writer.startList(SqlWriter.FrameTypeEnum.FUN_CALL, "(", ")");
            } else {
                frame = writer.startList(SqlWriter.FrameTypeEnum.FUN_CALL, "<", ">");
            }
            int i = 0;
            for (Pair<SqlIdentifier, SqlDataTypeSpec> p :
                    Pair.zip(this.fieldNames, this.fieldTypes)) {
                writer.sep(",", false);
                p.left.unparse(writer, 0, 0);
                p.right.unparse(writer, leftPrec, rightPrec);
                if (p.right.getNullable() != null && !p.right.getNullable()) {
                    writer.keyword("NOT NULL");
                }
                if (comments.get(i) != null) {
                    comments.get(i).unparse(writer, leftPrec, rightPrec);
                }
                i += 1;
            }
            writer.endList(frame);
        }
    }

    @Override
    public boolean equalsDeep(SqlTypeNameSpec node, Litmus litmus) {
        if (!(node instanceof SqlRowTypeNameSpec)) {
            return litmus.fail("{} != {}", this, node);
        }
        ExtendedSqlRowTypeNameSpec that = (ExtendedSqlRowTypeNameSpec) node;
        if (this.fieldNames.size() != that.fieldNames.size()) {
            return litmus.fail("{} != {}", this, node);
        }
        for (int i = 0; i < fieldNames.size(); i++) {
            if (!this.fieldNames.get(i).equalsDeep(that.fieldNames.get(i), litmus)) {
                return litmus.fail("{} != {}", this, node);
            }
        }
        if (this.fieldTypes.size() != that.fieldTypes.size()) {
            return litmus.fail("{} != {}", this, node);
        }
        for (int i = 0; i < fieldTypes.size(); i++) {
            if (!this.fieldTypes.get(i).equals(that.fieldTypes.get(i))) {
                return litmus.fail("{} != {}", this, node);
            }
        }
        return litmus.succeed();
    }

    @Override
    public RelDataType deriveType(SqlValidator sqlValidator) {
        final RelDataTypeFactory typeFactory = sqlValidator.getTypeFactory();
        return typeFactory.createStructType(
                fieldTypes.stream()
                        .map(dt -> dt.deriveType(sqlValidator))
                        .collect(Collectors.toList()),
                fieldNames.stream().map(SqlIdentifier::toString).collect(Collectors.toList()));
    }
}
