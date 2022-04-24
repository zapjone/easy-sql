package com.easy.sql.core.planner.service;

import com.google.common.base.Preconditions;

import java.time.ZoneId;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * SQL执行结果
 *
 * @author zhangap
 * @version 1.0, 2022/4/20
 */
public class EasySqlResult {
    public static final EasySqlResult RESULT_OK =
            EasySqlResult.builder()
                    .resultKind(ResultKind.SUCCESS)
                    .data(Collections.singletonList("OK"))
                    .build();

    private final String jobId;
    private final ResultKind resultKind;
    private final Iterator<Object> data;
    private final PrintStyle printStyle;
    private final ZoneId sessionTimeZone;

    public EasySqlResult(String jobId,
                         ResultKind resultKind,
                         Iterator<Object> data,
                         PrintStyle printStyle,
                         ZoneId sessionTimeZone) {
        this.jobId = jobId;
        this.resultKind = Preconditions.checkNotNull(resultKind, "resultKind should not be null");
        Preconditions.checkNotNull(data, "data should not be null");
        this.data = data;
        this.printStyle = Preconditions.checkNotNull(printStyle, "printStyle should not be null");
        this.sessionTimeZone =
                Preconditions.checkNotNull(sessionTimeZone, "sessionTimeZone should not be null");

    }

    public void print() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public interface PrintStyle {
        static PrintStyle tableau(
                int maxColumnWidth,
                String nullColumn,
                boolean deriveColumnWidthByType,
                boolean printRowKind) {
            return new TableauStyle(
                    maxColumnWidth, nullColumn, deriveColumnWidthByType, printRowKind);
        }
    }

    private static final class TableauStyle implements PrintStyle {
        private final boolean deriveColumnWidthByType;

        private final int maxColumnWidth;
        private final String nullColumn;
        /**
         * A flag to indicate whether print row kind info.
         */
        private final boolean printRowKind;

        private TableauStyle(
                int maxColumnWidth,
                String nullColumn,
                boolean deriveColumnWidthByType,
                boolean printRowKind) {
            this.deriveColumnWidthByType = deriveColumnWidthByType;
            this.maxColumnWidth = maxColumnWidth;
            this.nullColumn = nullColumn;
            this.printRowKind = printRowKind;
        }

        public boolean isDeriveColumnWidthByType() {
            return deriveColumnWidthByType;
        }

        int getMaxColumnWidth() {
            return maxColumnWidth;
        }

        String getNullColumn() {
            return nullColumn;
        }

        public boolean isPrintRowKind() {
            return printRowKind;
        }
    }

    public static class Builder {
        private String jobId;
        private ResultKind resultKind;
        private Iterator<Object> data = null;
        private PrintStyle printStyle =
                PrintStyle.tableau(Integer.MAX_VALUE, "(NULL)", false, false);
        private ZoneId sessionTimeZone = ZoneId.of("UTC+8");

        private Builder() {
        }

        public Builder jobId(String jobId) {
            this.jobId = jobId;
            return this;
        }

        public Builder resultKind(ResultKind kind) {
            this.resultKind = kind;
            return this;
        }

        public Builder data(List<Object> list) {
            this.data = list.iterator();
            return this;
        }

        public Builder setPrintStyle(PrintStyle printStyle) {
            Preconditions.checkNotNull(printStyle, "printStyle should not be null");
            this.printStyle = printStyle;
            return this;
        }

        public Builder setSessionTimeZone(ZoneId sessionTimeZone) {
            this.sessionTimeZone = sessionTimeZone;
            return this;
        }

        public EasySqlResult build() {
            return new EasySqlResult(jobId, resultKind, data, printStyle, sessionTimeZone);
        }

    }

}
