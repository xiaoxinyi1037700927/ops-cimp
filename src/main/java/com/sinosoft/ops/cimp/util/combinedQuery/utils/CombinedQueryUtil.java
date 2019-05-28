package com.sinosoft.ops.cimp.util.combinedQuery.utils;

import com.sinosoft.ops.cimp.util.combinedQuery.enums.Type;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CombinedQueryUtil {
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 获取值的类型
     *
     * @param value
     * @return
     */
    public static int getValueType(String value) {
        if (isNumber(value)) {
            return Type.NUMBER.getCode();
        } /*else if (isDate(value)) {
            return Type.DATE.getCode();
        }*/

        return Type.STRING.getCode();
    }

    /**
     * 判断值是否是数字
     *
     * @param value
     * @return
     */
    public static boolean isNumber(String value) {
        try {
            new BigDecimal(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断值是否是日期
     *
     * @param value
     * @return
     */
    public static boolean isDate(String value) {
        try {
            LocalDate.parse(value, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取类型数量
     *
     * @param type
     * @return
     */
    public static int getTypeNum(int type) {
        int count = 0;
        while (type != 0) {
            type &= type - 1;
            count++;
        }

        return count;
    }

}
