package com.easy.sql.parser.type;

import com.easy.sql.parser.table.calcite.ExtendedRelTypeFactory;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlTypeNameSpec;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.util.Litmus;
import org.apache.calcite.util.NlsString;

import java.util.Objects;

/**
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlRawTypeNameSpec extends SqlTypeNameSpec {

    private static final String RAW_TYPE_NAME = "RAW";

    private final SqlNode className;

    private final SqlNode serializerString;

    public SqlRawTypeNameSpec(SqlNode className, SqlNode serializerString, SqlParserPos pos) {
        super(new SqlIdentifier(RAW_TYPE_NAME, pos), pos);
        this.className = className;
        this.serializerString = serializerString;
    }

    @Override
    public RelDataType deriveType(SqlValidator validator) {
        return ((ExtendedRelTypeFactory) validator.getTypeFactory())
                .createRawType(
                        ((NlsString) SqlLiteral.value(className)).getValue(),
                        ((NlsString) SqlLiteral.value(serializerString)).getValue());
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword(RAW_TYPE_NAME);
        final SqlWriter.Frame frame = writer.startList(SqlWriter.FrameTypeEnum.FUN_CALL, "(", ")");
        writer.sep(",");
        className.unparse(writer, leftPrec, rightPrec);
        writer.sep(",");
        serializerString.unparse(writer, leftPrec, rightPrec);
        writer.endList(frame);
    }

    @Override
    public boolean equalsDeep(SqlTypeNameSpec spec, Litmus litmus) {
        if (!(spec instanceof SqlRawTypeNameSpec)) {
            return litmus.fail("{} != {}", this, spec);
        }
        SqlRawTypeNameSpec that = (SqlRawTypeNameSpec) spec;
        if (!Objects.equals(this.className, that.className)) {
            return litmus.fail("{} != {}", this, spec);
        }
        if (!Objects.equals(this.serializerString, that.serializerString)) {
            return litmus.fail("{} != {}", this, spec);
        }
        return litmus.succeed();
    }
}
