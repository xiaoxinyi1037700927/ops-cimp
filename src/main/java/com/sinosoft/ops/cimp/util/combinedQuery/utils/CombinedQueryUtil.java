package com.sinosoft.ops.cimp.util.combinedQuery.utils;

import com.sinosoft.ops.cimp.util.IdUtil;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.Param;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Type;
import com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes.ValueNodeProcessor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CombinedQueryUtil {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String EMPTY_PARAM = "''";
    public static final Pattern PATTERN_EMPTY_STRING = Pattern.compile("^'\\s*'$");

    /**
     * 获取值的类型
     *
     * @param value
     * @return
     */
    public static int getValueType(String value) {
        if (isNumber(value)) {
            return Type.NUMBER.getCode();
        } else if (isDate(value)) {
            return Type.DATE.getCode();
        }

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

    /**
     * 解析表达式,如果是码值的话返回码值
     *
     * @param expr
     * @return 码值code(多个时以逗号分隔)
     */
    public static String getCodes(String expr) {
        try {
            List<String> list = Arrays.asList(expr.substring(expr.indexOf("'") + 1, expr.lastIndexOf("'")).split("'\\s*,\\s*'"));

            List<String> codes = new ArrayList<>();
            Matcher matcher;
            for (String s : list) {
                matcher = ValueNodeProcessor.PATTERN_CODE.matcher(s);
                if (!matcher.matches()) {
                    return null;
                }
                codes.add(matcher.group(1));
            }
            return String.join(",", codes);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取空的参数
     *
     * @return
     */
    public static Param getEmptyParam() {
        return new Param(IdUtil.uuid(), EMPTY_PARAM);
    }

    /**
     * 获取时间距当前时间的跨度
     *
     * @param time
     * @return
     */
    public static String getTimeDesc(Date time) {
        int days = getDaysUntilNow(time);

        if (days < 0) {
            return "";
        } else if (days == 0) {
            return "今天";
        } else if (days == 1) {
            return "昨天";
        } else if (days < 7) {
            return "一周内";
        } else if (days < 30) {
            return "一个月内";
        } else if (days < 180) {
            return "超过一个月";
        } else if (days < 360) {
            return "超过六个月";
        } else {
            return "超过一年";
        }
    }

    /**
     * 获取日期至今的天数差
     *
     * @param time
     * @return
     */
    public static int getDaysUntilNow(Date time) {
        if (time == null) {
            return -1;
        }

        LocalDate ldt = LocalDateTime.ofInstant(time.toInstant(), ZoneId.systemDefault()).toLocalDate();
        Period period = ldt.until(LocalDate.now());
        return period.getDays();
    }

}
