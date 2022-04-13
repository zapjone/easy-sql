package com.easy.sql.parser.type;

import org.apache.calcite.sql.SqlBasicTypeNameSpec;
import org.apache.calcite.sql.SqlTypeNameSpec;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.util.Litmus;

import java.util.Objects;

/**
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlTimestampLtzTypeNameSpec extends SqlBasicTypeNameSpec {

    private final String typeAlias;
    private final SqlTypeName sqlTypeName;
    private final int precision;

    public SqlTimestampLtzTypeNameSpec(String typeAlias,
                                       SqlTypeName typeName,
                                       int precision,
                                       SqlParserPos pos) {
        super(typeName, precision, pos);
        this.typeAlias = typeAlias;
        this.sqlTypeName = typeName;
        this.precision = precision;
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword(typeAlias);
        if (sqlTypeName.allowsPrec() && (precision >= 0)) {
            SqlWriter.Frame frame = writer.startList(SqlWriter.FrameTypeEnum.FUN_CALL, "(", ")");
            writer.print(precision);
            writer.endList(frame);
        }
    }

    @Override
    public boolean equalsDeep(SqlTypeNameSpec node, Litmus litmus) {
        if (!(node instanceof SqlTimestampLtzTypeNameSpec)) {
            return litmus.fail("{} != {}", this, node);
        }
        SqlTimestampLtzTypeNameSpec that = (SqlTimestampLtzTypeNameSpec) node;
        if (!Objects.equals(this.typeAlias, that.typeAlias)) {
            return litmus.fail("{} != {}", this, node);
        }
        return super.equalsDeep(node, litmus);
    }

}
