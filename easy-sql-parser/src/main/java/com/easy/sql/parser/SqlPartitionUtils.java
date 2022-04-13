package com.easy.sql.parser;

import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.util.NlsString;

import java.util.LinkedHashMap;

/**
 * @author zhangap
 * @version 1.0, 2022/4/13
 */
public class SqlPartitionUtils {

    /**
     * Sql分区操作工具方法
     */
    public static LinkedHashMap<String, String> getPartitionKVs(SqlNodeList partitionSpec) {
        if (partitionSpec == null) {
            return null;
        }
        LinkedHashMap<String, String> ret = new LinkedHashMap<>();
        if (partitionSpec.size() == 0) {
            return ret;
        }
        for (SqlNode node : partitionSpec.getList()) {
            SqlProperty sqlProperty = (SqlProperty) node;
            Comparable<?> comparable = SqlLiteral.value(sqlProperty.getValue());
            String value =
                    comparable instanceof NlsString
                            ? ((NlsString) comparable).getValue()
                            : comparable.toString();
            ret.put(sqlProperty.getKey().getSimple(), value);
        }
        return ret;
    }

}
