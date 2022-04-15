package com.easy.sql.parser.ddl;

import com.easy.sql.parser.ExtendedSqlNode;
import com.easy.sql.parser.error.SqlValidateException;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.SqlSpecialOperator;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Collections.singletonList;

/**
 * <pre>{@code
 * CREATE TABLE base_table_1 (
 *     id BIGINT,
 *     name STRING,
 *     tstmp TIMESTAMP,
 *     PRIMARY KEY(id)
 * ) WITH (
 *     ‘connector’: ‘kafka’,
 *     ‘connector.starting-offset’: ‘12345’,
 *     ‘format’: ‘json’
 * )
 *
 * }</pre>
 *
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlTableLike extends SqlCall implements ExtendedSqlNode {

    public enum MergingStrategy {
        INCLUDING,
        EXCLUDING,
        OVERWRITING
    }

    public enum FeatureOption {
        ALL,
        CONSTRAINTS,
        GENERATED,
        METADATA,
        OPTIONS,
        PARTITIONS
    }

    private final SqlIdentifier sourceTable;
    private final List<SqlTableLikeOption> options;

    private static final SqlSpecialOperator OPERATOR =
            new SqlSpecialOperator("LIKE TABLE", SqlKind.OTHER);

    public SqlTableLike(
            SqlParserPos pos, SqlIdentifier sourceTable, List<SqlTableLikeOption> options) {
        super(pos);
        this.sourceTable = sourceTable;
        this.options = options;
    }

    @Nonnull
    @Override
    public SqlOperator getOperator() {
        return OPERATOR;
    }

    @Nonnull
    @Override
    public List<SqlNode> getOperandList() {
        return singletonList(sourceTable);
    }

    public SqlIdentifier getSourceTable() {
        return sourceTable;
    }

    public List<SqlTableLikeOption> getOptions() {
        return options;
    }

    private static final Map<FeatureOption, List<MergingStrategy>> invalidCombinations =
            new HashMap<>();

    static {
        invalidCombinations.put(FeatureOption.ALL, singletonList(MergingStrategy.OVERWRITING));
        invalidCombinations.put(
                FeatureOption.PARTITIONS, singletonList(MergingStrategy.OVERWRITING));
        invalidCombinations.put(
                FeatureOption.CONSTRAINTS, singletonList(MergingStrategy.OVERWRITING));
    }

    @Override
    public void validate() throws SqlValidateException {
        long distinctFeatures =
                options.stream().map(SqlTableLikeOption::getFeatureOption).distinct().count();
        if (distinctFeatures != options.size()) {
            throw new SqlValidateException(
                    pos, "Each like option feature can be declared only once.");
        }

        for (SqlTableLikeOption option : options) {
            if (invalidCombinations
                    .getOrDefault(option.featureOption, Collections.emptyList())
                    .contains(option.mergingStrategy)) {
                throw new SqlValidateException(
                        pos,
                        String.format(
                                "Illegal merging strategy '%s' for '%s' option.",
                                option.getMergingStrategy(), option.getFeatureOption()));
            }
        }
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword("LIKE");
        sourceTable.unparse(writer, leftPrec, rightPrec);
        SqlWriter.Frame frame = writer.startList("(", ")");
        for (SqlTableLikeOption option : options) {
            writer.newlineAndIndent();
            writer.print("  ");
            writer.keyword(option.mergingStrategy.toString());
            writer.keyword(option.featureOption.toString());
        }
        writer.newlineAndIndent();
        writer.endList(frame);
    }

    /**
     * A pair of {@link MergingStrategy} and {@link FeatureOption}.
     *
     * @see MergingStrategy
     * @see FeatureOption
     */
    public static class SqlTableLikeOption {
        private final MergingStrategy mergingStrategy;
        private final FeatureOption featureOption;

        public SqlTableLikeOption(MergingStrategy mergingStrategy, FeatureOption featureOption) {
            this.mergingStrategy = mergingStrategy;
            this.featureOption = featureOption;
        }

        public MergingStrategy getMergingStrategy() {
            return mergingStrategy;
        }

        public FeatureOption getFeatureOption() {
            return featureOption;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            SqlTableLikeOption that = (SqlTableLikeOption) o;
            return mergingStrategy == that.mergingStrategy && featureOption == that.featureOption;
        }

        @Override
        public int hashCode() {
            return Objects.hash(mergingStrategy, featureOption);
        }

        @Override
        public String toString() {
            return String.format("%s %s", mergingStrategy, featureOption);
        }
    }

}
