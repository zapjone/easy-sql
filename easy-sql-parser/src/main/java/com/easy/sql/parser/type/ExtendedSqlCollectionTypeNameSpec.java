package com.easy.sql.parser.type;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.sql.SqlCollectionTypeNameSpec;
import org.apache.calcite.sql.SqlTypeNameSpec;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.util.Litmus;
import org.apache.calcite.util.Util;

/**
 * 扩展的列表类型
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class ExtendedSqlCollectionTypeNameSpec extends SqlCollectionTypeNameSpec {
    private final boolean elementNullable;
    private final SqlTypeName collectionTypeName;
    private final boolean unparseAsStandard;

    /**
     * Creates a {@code SqlCollectionTypeNameSpec}.
     *
     * @param elementTypeName    Type of the collection element
     * @param collectionTypeName Collection type name
     * @param pos                Parser position, must not be null
     */
    public ExtendedSqlCollectionTypeNameSpec(SqlTypeNameSpec elementTypeName,
                                             boolean elementNullable,
                                             SqlTypeName collectionTypeName,
                                             boolean unparseAsStandard,
                                             SqlParserPos pos) {
        super(elementTypeName, collectionTypeName, pos);
        this.elementNullable = elementNullable;
        this.collectionTypeName = collectionTypeName;
        this.unparseAsStandard = unparseAsStandard;
    }

    public boolean elementNullable() {
        return elementNullable;
    }

    public SqlTypeName getCollectionTypeName() {
        return collectionTypeName;
    }

    public boolean unparseAsStandard() {
        return unparseAsStandard;
    }

    @Override
    public RelDataType deriveType(SqlValidator validator) {
        RelDataType elementType = getElementTypeName().deriveType(validator);
        elementType =
                validator.getTypeFactory().createTypeWithNullability(elementType, elementNullable);
        return createCollectionType(elementType, validator.getTypeFactory());
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        if (unparseAsStandard) {
            this.getElementTypeName().unparse(writer, leftPrec, rightPrec);
            // Default is nullable.
            if (!elementNullable) {
                writer.keyword("NOT NULL");
            }
            writer.keyword(collectionTypeName.name());
        } else {
            writer.keyword(collectionTypeName.name());
            SqlWriter.Frame frame = writer.startList(SqlWriter.FrameTypeEnum.FUN_CALL, "<", ">");

            getElementTypeName().unparse(writer, leftPrec, rightPrec);
            // Default is nullable.
            if (!elementNullable) {
                writer.keyword("NOT NULL");
            }
            writer.endList(frame);
        }
    }

    @Override
    public boolean equalsDeep(SqlTypeNameSpec spec, Litmus litmus) {
        if (!(spec instanceof ExtendedSqlCollectionTypeNameSpec)) {
            return litmus.fail("{} != {}", this, spec);
        }
        ExtendedSqlCollectionTypeNameSpec that = (ExtendedSqlCollectionTypeNameSpec) spec;
        if (this.elementNullable != that.elementNullable) {
            return litmus.fail("{} != {}", this, spec);
        }
        return super.equalsDeep(spec, litmus);
    }

    private RelDataType createCollectionType(
            RelDataType elementType, RelDataTypeFactory typeFactory) {
        switch (collectionTypeName) {
            case MULTISET:
                return typeFactory.createMultisetType(elementType, -1);
            case ARRAY:
                return typeFactory.createArrayType(elementType, -1);

            default:
                throw Util.unexpected(collectionTypeName);
        }
    }

}
