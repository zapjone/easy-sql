package com.easy.sql.core.configuration;

import com.easy.sql.core.common.SqlDialect;
import com.google.common.base.Preconditions;

import java.time.ZoneId;

import static java.time.ZoneId.SHORT_IDS;

/**
 * @author zhangap
 * @version 1.0, 2022/4/19
 */
public class EasySqlConfig {

    private Boolean nullCheck = true;
    private final Configuration configuration = new Configuration();

    public Configuration getConfiguration() {
        return configuration;
    }

    public void addConfiguration(Configuration configuration) {
        Preconditions.checkNotNull(configuration);
        this.configuration.addAll(configuration);
    }

    public SqlDialect getSqlDialect() {
        return SqlDialect.valueOf(
                getConfiguration().getString(EasySqlConfigOptions.EASY_SQL_DIALECT).toUpperCase());
    }

    /**
     * Sets the current SQL dialect to parse a SQL query. EasySql's SQL behavior by default.
     */
    public void setSqlDialect(SqlDialect sqlDialect) {
        getConfiguration()
                .setString(EasySqlConfigOptions.EASY_SQL_DIALECT, sqlDialect.name().toLowerCase());
    }

    public ZoneId getLocalTimeZone() {
        String zone = configuration.getString(EasySqlConfigOptions.LOCAL_TIME_ZONE);
        validateTimeZone(zone);
        return EasySqlConfigOptions.LOCAL_TIME_ZONE.defaultValue().equals(zone)
                ? ZoneId.systemDefault()
                : ZoneId.of(zone);
    }

    public void setLocalTimeZone(ZoneId zoneId) {
        validateTimeZone(zoneId.toString());
        configuration.setString(EasySqlConfigOptions.LOCAL_TIME_ZONE, zoneId.toString());
    }

    public Boolean getNullCheck() {
        return nullCheck;
    }

    /**
     * Sets the NULL check. If enabled, all fields need to be checked for NULL first.
     */
    public void setNullCheck(Boolean nullCheck) {
        this.nullCheck = Preconditions.checkNotNull(nullCheck);
    }

    public Integer getMaxGeneratedCodeLength() {
        return this.configuration.getInteger(EasySqlConfigOptions.MAX_LENGTH_GENERATED_CODE);
    }

    public void setMaxGeneratedCodeLength(Integer maxGeneratedCodeLength) {
        this.configuration.setInteger(
                EasySqlConfigOptions.MAX_LENGTH_GENERATED_CODE, maxGeneratedCodeLength);
    }


    /**
     * Validates user configured time zone.
     */
    private void validateTimeZone(String zone) {
        final String zoneId = zone.toUpperCase();
        if (zoneId.startsWith("UTC+")
                || zoneId.startsWith("UTC-")
                || SHORT_IDS.containsKey(zoneId)) {
            throw new IllegalArgumentException(
                    String.format(
                            "The supported Zone ID is either a full name such as 'America/Los_Angeles',"
                                    + " or a custom timezone id such as 'GMT-08:00', but configured Zone ID is '%s'.",
                            zone));
        }
    }

}
