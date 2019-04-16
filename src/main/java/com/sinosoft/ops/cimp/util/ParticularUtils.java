package com.sinosoft.ops.cimp.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.script.ScriptEngineManager;

/**
 * 工具类
 *
 * @author xiangqian
 */
public class ParticularUtils {

    /**
     * @param target
     * @param defaultValue
     * @return
     * @author xiangqian
     */
    public static int toNumber(Object target, int defaultValue) {

        if (target == null) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(target.toString().trim());
        } catch (Exception e) {
        }

        try {
            return Float.valueOf(target.toString().trim()).intValue();
        } catch (Exception e) {
        }

        return defaultValue;
    }

    /**
     * @param target
     * @param defaultValue
     * @return
     * @author xiangqian
     */
    public static float toFloat(Object target, float defaultValue) {
        try {
            return target == null ? defaultValue : Float.valueOf(target.toString().trim());
        } catch (Exception e) {
        }
        return defaultValue;
    }


    /**
     * @param target
     * @param defaultValue
     * @return
     * @author xiangqian
     */
    public static String trim(Object target, String defaultValue) {
        return target == null ? defaultValue : target.toString().trim();
    }

    /**
     * @param obj
     * @param defaultValue
     * @return
     * @author xiangqian
     */
    public static boolean convertToBoolean(Object obj, boolean defaultValue) {
        return obj == null ? defaultValue : ("true".equalsIgnoreCase(obj.toString().trim()) ? true : ("false".equalsIgnoreCase(obj.toString().trim()) ? false : defaultValue));
    }

    /**
     * 序列化和反序列化
     *
     * @param t
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @author xiangqian
     */
    @SuppressWarnings("unchecked")
    public static <T> T objectCopy(T t) throws IOException, ClassNotFoundException {
        if (t == null) {
            throw new RuntimeException("[The incoming parameter is empty!]");
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ObjectOutputStream(byteArrayOutputStream).writeObject(t);
        return (T) (new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())).readObject());
    }

    public static String objectToByteArrayStr(Object obj) throws IOException {
        if (obj == null) {
            throw new RuntimeException("[The incoming parameter is empty!]");
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ObjectOutputStream(byteArrayOutputStream).writeObject(obj);

        // byte[]
        StringBuffer buffer = new StringBuffer();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        for (int i = 0; i < byteArray.length; i++) {
            if (i == 0) {
                buffer.append(byteArray[i]);
            } else {
                buffer.append(",").append(byteArray[i]);
            }
        }
        return buffer.toString();
    }

    public static Object byteArrayStrToObject(String byteArrayStr) throws ClassNotFoundException, IOException {
        byteArrayStr = trim(byteArrayStr, "");
        if ("".equals(byteArrayStr)) {
            return null;
        }

        String[] array = byteArrayStr.split(",");
        byte[] byteArray = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            byteArray[i] = (byte) Integer.parseInt(array[i]);
        }
        return new ObjectInputStream(new ByteArrayInputStream(byteArray)).readObject();
    }

    /**
     * 获取 Object Field
     *
     * @param object
     * @param fieldName
     * @param isAccessible
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @author xiangqian
     */
    public static Object getFieldValue(Object object, String fieldName, boolean isAccessible) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = null;
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                field = superClass.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                //Field 不在当前类定义, 继续向上转型
            }
        }

        if (field == null) {
            throw new NoSuchFieldException("Could not find field [" + fieldName + "] on object [" + object + "]");
        }

        if (!Modifier.isPublic(field.getModifiers())) {
            field.setAccessible(isAccessible);
        }
        return field.get(object);
    }

    /**
     * 根据逻辑运算式计算
     *
     * @param object
     * @param logicExpression
     * @return
     * @throws Exception
     * @author xiangqian
     */
    public static Object calculateLogicExpression(Object object, String logicExpression) throws Exception {
        if (logicExpression == null) {
            return "";
        }
        StringBuffer build = new StringBuffer("");
        if (object != null) {
            int beginIndex = 0;
            int endIndex = 0;
            while ((beginIndex = logicExpression.indexOf("#{", beginIndex)) >= 0) {
                build.append(logicExpression.substring(endIndex, beginIndex));
                if ((endIndex = logicExpression.indexOf("}", beginIndex)) > (beginIndex + 2)) {
                    build.append(getFieldValue(object, logicExpression.substring(beginIndex + 2, endIndex), true));
                }
                beginIndex += 2;
                ++endIndex;
            }
            build.append(logicExpression.substring(endIndex, logicExpression.length()));
        } else {
            build.append(logicExpression);
        }
        return new ScriptEngineManager().getEngineByName("js").eval(build.toString());
    }

    /**
     * @param orginal
     * @return
     * @author xiangqian
     */
    public static boolean isRealNumber(String orginal) {
        String[] regexs = new String[]{"[+-]{0,1}0", "^\\+{0,1}[1-9]\\d*", "^-[1-9]\\d*",
                "[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+"};
        boolean flag = false;
        for (String regex : regexs) {
            flag = Pattern.compile(regex).matcher(orginal).matches();
            if (flag) {
                return flag;
            }
        }
        return flag;
    }

    /**
     * 文件拷贝
     *
     * @param is
     * @param isCloseIS    : 是否关闭 InputStream
     * @param os
     * @param isCloseOS    : 是否关闭 OutputStream
     * @param multipleByte
     */
    public static void copyFile(InputStream is, boolean isCloseIS, OutputStream os, boolean isCloseOS, int multipleByte) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(os);
            if (multipleByte <= 0) {
                multipleByte = 1;
            }
            byte[] b = new byte[1024 * multipleByte];
            int len;
            while ((len = bis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (isCloseIS) {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (isCloseOS) {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    /**
     * Unix/Windows
     * 获取本机网络地址ip
     *
     * @throws SocketException
     */
    public static String getNetworkIp() throws SocketException {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress inetAddress = null;
            NetworkInterface netInterface = null;
            Enumeration<InetAddress> addresses = null;
            String ip = null;
            if (allNetInterfaces != null) {
                while (allNetInterfaces.hasMoreElements()) {
                    netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                    addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        inetAddress = addresses.nextElement();
                        if (inetAddress != null && (inetAddress instanceof Inet4Address) && inetAddress.isSiteLocalAddress()) {
                            ip = inetAddress.getHostAddress();
                            return ip;
                        }
                    }
                }
            }
            return null;
        } catch (SocketException e) {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * @see #getDataFormat(String)
     **/
    public static final String PATTERN = "yyyyMMddHHmmss";
    public static final String PATTERN2 = "yyyy-MM-dd HH:mm:ss";

    /**
     * @param pattern
     * @return
     * @see #PATTERN1
     */
    public static String getDataFormat(Date source, String pattern) {
        try {
            if (source == null || pattern == null) {
                return null;
            }
            return new SimpleDateFormat(pattern).format(source);
        } catch (Exception e) {
        }
        return null;
    }

    public static Date getDateParse(String source, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(source);
        } catch (Exception e) {
        }
        return null;
    }

    public static Date getDateParse(String source, String currentPattern, String targetPattern) {
        try {
            if (currentPattern == null || "".equals(currentPattern) || targetPattern == null || "".equals(targetPattern)) {
                return null;
            }
            if (currentPattern.equals(targetPattern)) {
                return new SimpleDateFormat(targetPattern).parse(source);
            } else {
                Date currentDate = new SimpleDateFormat(currentPattern).parse(source);
                String targetSource = new SimpleDateFormat(targetPattern).format(currentDate);
                return getDateParse(targetSource, targetPattern);
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 压缩字符串
     *
     * @param targetStr
     * @return
     * @throws IOException
     */
    public static String compress(String targetStr) throws IOException {
        if (targetStr == null || targetStr.length() == 0) {
            return targetStr;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(targetStr.getBytes());
        gzip.close();
        return out.toString("ISO-8859-1");
    }

    /**
     * 解压字符串
     *
     * @param compressStr
     * @return
     * @throws IOException
     */
    public static String uncompress(String compressStr) throws IOException {
        if (compressStr == null || compressStr.length() == 0) {
            return compressStr;
        }
        GZIPInputStream gunzip = null;
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(compressStr.getBytes("ISO-8859-1"));
            gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            // toString()使用平台默认编码("GBK")
            return out.toString();
        } catch (Exception e) {
        } finally {
            if (gunzip != null) {
                gunzip.close();
            }
            if (out != null) {
                out.close();
            }
        }
        return null;
    }

    /**
     * to_date string类型的日期，方便拼接sql
     *
     * @param str 类型的日期
     * @return
     * @throws IOException
     */

    public static String FormatDataStr(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return "to_date('" + format.format(format.parse(str.toString())) + "','yyyy-MM-dd')";
        } catch (ParseException e) {
            return str;
        }
    }


    /**
     * 判断是不是日期型数据
     *
     * @param str 类型的日期
     * @return
     * @throws IOException
     */
    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            format.parse(str);
        } catch (ParseException e) {
            convertSuccess = false;
        }
        return convertSuccess;
    }

    /**
     * 比较日期型数据大小
     *
     * @param str 类型的日期
     * @return result>0 说明参数date1>date2
     * @throws IOException
     */
    public static int comparetwodate(String date1, String date2) {
        int result = date1.compareTo(date2);
        return result;
    }

    /**
     * 将数组转成list 然后再比较 只有完全包含是才返回true 例如cid_815与815 返回为true cid_815与81 返回为false
     *
     * @param breaksign :分隔符
     * @return boolean
     * @throws IOException
     */
    public static boolean arraystolist(String data, String date2, String breaksign) {

        return Arrays.asList(data.split(breaksign)).contains(date2);
    }

}
