package com.easy.sql.parser.type;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.sql.SqlDataTypeSpec;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlTypeNameSpec;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.util.Litmus;

/**
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlMapTypeNameSpec extends SqlTypeNameSpec {

    private final SqlDataTypeSpec keyType;
    private final SqlDataTypeSpec valueType;

    /**
     * Creates a {@code SqlTypeNameSpec}.
     */
    public SqlMapTypeNameSpec(SqlDataTypeSpec keyType,
                              SqlDataTypeSpec valueType,
                              SqlParserPos pos) {
        super(new SqlIdentifier(SqlTypeName.MAP.getName(), pos), pos);
        this.keyType = keyType;
        this.valueType = valueType;
    }

    public SqlDataTypeSpec getKeyType() {
        return keyType;
    }

    public SqlDataTypeSpec getValType() {
        return valueType;
    }

    @Override
    public RelDataType deriveType(SqlValidator validator) {
        return validator.getTypeFactory()
                .createMapType(keyType.deriveType(validator), valueType.deriveType(validator));
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword("MAP");
        SqlWriter.Frame frame = writer.startList(SqlWriter.FrameTypeEnum.FUN_CALL, "<", ">");
        writer.sep(",");
        keyType.unparse(writer, leftPrec, rightPrec);
        //default is nullable
        if (keyType.getNullable() != null && !keyType.getNullable()) {
            writer.keyword("NOT NULL");
        }
        writer.sep(",");
        valueType.unparse(writer, leftPrec, rightPrec);
        //default is nullable
        if (valueType.getNullable() != null && !valueType.getNullable()) {
            writer.keyword("NOT NULL");
        }
        writer.endList(frame);
    }

    @Override
    public boolean equalsDeep(SqlTypeNameSpec spec, Litmus litmus) {
        if (!(spec instanceof SqlMapTypeNameSpec)) {
            return litmus.fail("{} != {}", this, spec);
        }
        SqlMapTypeNameSpec that = (SqlMapTypeNameSpec) spec;
        if (!this.keyType.equalsDeep(that.keyType, litmus)) {
            return litmus.fail("{} != {}", this, spec);
        }
        if (!this.valueType.equalsDeep(that.valueType, litmus)) {
            return litmus.fail("{} != {}", this, spec);
        }
        return litmus.succeed();
    }
}
