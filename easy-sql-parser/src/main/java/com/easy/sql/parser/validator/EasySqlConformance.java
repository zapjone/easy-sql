package com.easy.sql.parser.validator;

import org.apache.calcite.sql.validate.SqlConformance;

/**
 * 自定义方言，可以再此基础上扩展其他的方言
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public enum EasySqlConformance implements SqlConformance {
    /**
     * Calcite's default SQL behavior.
     */
    DEFAULT;

    @Override
    public boolean isLiberal() {
        return false;
    }

    @Override
    public boolean allowCharLiteralAlias() {
        return false;
    }

    @Override
    public boolean isGroupByAlias() {
        return false;
    }

    @Override
    public boolean isGroupByOrdinal() {
        return false;
    }

    @Override
    public boolean isHavingAlias() {
        return false;
    }

    @Override
    public boolean isSortByOrdinal() {
        return this == DEFAULT;
    }

    @Override
    public boolean isSortByAlias() {
        return this == DEFAULT;
    }

    @Override
    public boolean isSortByAliasObscures() {
        return false;
    }

    @Override
    public boolean isFromRequired() {
        return false;
    }

    @Override
    public boolean splitQuotedTableName() {
        return false;
    }

    @Override
    public boolean allowHyphenInUnquotedTableName() {
        return false;
    }

    @Override
    public boolean isBangEqualAllowed() {
        return false;
    }

    @Override
    public boolean isPercentRemainderAllowed() {
        return true;
    }

    @Override
    public boolean isMinusAllowed() {
        return false;
    }

    @Override
    public boolean isApplyAllowed() {
        return false;
    }

    @Override
    public boolean isInsertSubsetColumnsAllowed() {
        return false;
    }

    @Override
    public boolean allowAliasUnnestItems() {
        return false;
    }

    @Override
    public boolean allowNiladicParentheses() {
        return false;
    }

    @Override
    public boolean allowExplicitRowValueConstructor() {
        return this == DEFAULT;
    }

    @Override
    public boolean allowExtend() {
        return false;
    }

    @Override
    public boolean isLimitStartCountAllowed() {
        return false;
    }

    @Override
    public boolean allowGeometry() {
        return false;
    }

    @Override
    public boolean shouldConvertRaggedUnionTypesToVarying() {
        return false;
    }

    @Override
    public boolean allowExtendedTrim() {
        return false;
    }

    @Override
    public boolean allowPluralTimeUnits() {
        return false;
    }

    @Override
    public boolean allowQualifyingCommonColumn() {
        return true;
    }

}
