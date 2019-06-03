/**
 * @project: IIMP
 * @title: Constants.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.common.model;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * @version 1.0.0
 * @ClassName: Constants
 * @description: 常量
 * @author: Nil
 * @date: 2017年10月17日 下午8:17:00
 */
public class Constants {

    /***会话中登录用户的标识*/
    public static final String SESSION_LOGIN_USER = "_LoginUser";

    /***缺省的每页记录数*/
    public static int DEFAULT_PAGE_SIZE = 20;

    /*** 零值UUID */
    public static UUID UUID_ZERO = UUID.fromString("000000000000-0000-0000-0000-00000000");

    /*** 最小日期 */
    public static Date MINIMUM_DATE;

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.getActualMinimum(Calendar.YEAR),
                calendar.getActualMinimum(Calendar.MONTH),
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH),
                calendar.getActualMinimum(Calendar.HOUR),
                calendar.getActualMinimum(Calendar.MINUTE),
                calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
        MINIMUM_DATE = calendar.getTime();
    }

    /*** 最大日期 */
    public static Date MAXIMUM_DATE;

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.getActualMaximum(Calendar.YEAR),
                calendar.getActualMaximum(Calendar.MONTH),
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH),
                calendar.getActualMaximum(Calendar.HOUR),
                calendar.getActualMaximum(Calendar.MINUTE),
                calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));
        MAXIMUM_DATE = calendar.getTime();
    }
}
