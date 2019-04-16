package com.sinosoft.ops.cimp.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.*;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rain chen on 2017/8/7.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public final class StringUtil {

    private StringUtil() {

    }

    //深拷贝2:序列化|反序列化方法
    public static List listDeepCopy(List src) throws IOException, ClassNotFoundException {
        if (src == null || src.size() == 0) {
            return new ArrayList();
        }
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        List dest = (List) in.readObject();
        return dest;
    }

    /**
     * @param target 需要转化的对象
     * @return 转化结果 :如果为null，空字符串，不为空则toString()
     */
    public static String obj2Str(Object target) {
        return obj2Str(target, "");
    }

    /**
     * @param target       需要转化的对象
     * @param defaultValue 缺省值
     * @return 转化结果:如果为null，则返回缺省值，否则，返回toString()
     */
    public static String obj2Str(Object target, String defaultValue) {
        String value = defaultValue;
        if (target != null) {
            if (target instanceof Double) {
                DecimalFormat decimalFormat = new DecimalFormat("#");
                decimalFormat.setMaximumFractionDigits(8);
                value = decimalFormat.format(target);
            } else {
                value = String.valueOf(target);
            }
        }
        return value;

    }

    public static String date2Str(Object dateTime) {
        if (dateTime != null && dateTime instanceof Date) {
            return new DateTime(dateTime).toString("yyyy.MM");
        }
        return "";
    }

    /**
     * 把字符串中的特殊字符替换为空
     *
     * @param str
     * @param format
     */
    public static String replaceSpeStr(String str, String format) {
        if (StringUtils.isNotEmpty(str)) {
            return str.replace(format, "");
        }
        return str;
    }

    /**
     * 验证对象不为空也不为null
     *
     * @param str 验证对象
     * @return 处理结果 空/Null:true,否则:false
     */
    public static boolean isNotEmptyOrNull(Object str) {
        return !"".equals(str) && null != str;
    }


    public static String clob2Str(Clob clob) throws SQLException, IOException {
        String result;
        Reader reader = clob.getCharacterStream();
        BufferedReader bufferedReader = new BufferedReader(reader);
        String s = bufferedReader.readLine();
        StringBuilder buffer = new StringBuilder();
        while (s != null) {
            buffer.append(s);
            s = bufferedReader.readLine();
        }
        result = buffer.toString();
        return result;
    }


    /**
     * 将blob转化为byte[],可以转化二进制流的
     *
     * @param blob
     * @return
     */
    public static byte[] blobToBytes(Blob blob) {
        InputStream is = null;
        byte[] b = null;
        try {
            is = blob.getBinaryStream();
            b = new byte[(int) blob.length()];
            is.read(b);
            return b;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                is = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    public static boolean isEmptyOrNull(String str) {
        return !isNotEmptyOrNull(str);
    }

    public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
}
