/**
 * @Project:      IIMP
 * @Title:          ConvertUtil.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;

import java.io.IOException;

/**
 * @ClassName:  ConvertUtil
 * @Description: 转换工具类
 * @Author:        Nil
 * @Date:            2017年8月1日 上午10:20:03
 * @Version        1.0.0
 */
public class ConvertUtil {
    /**
     * 将字节数组转化为十六进制字符串
     * @return 十六进制字符串
     */
    public static String bytes2HexString(byte [] data) {
        return new String(Hex.encodeHex(data));
    }

    /**
     * 将十六进制字符串转化为字节数组
     * @param hexString 十六进制字符串
     * @return 字节数组
     * @throws DecoderException
     */
    public static byte[] hexString2Bytes(String hexString) throws DecoderException {
        return Hex.decodeHex(hexString.toCharArray());
    }
    
    /**
     * 将字节数组转换为Base64字符串
     * @param input 字节数组
     * @return Base64字符串
     */
    public static String bytes2Base64String(final byte[] input){
        return StringUtils.newStringUtf8(Base64.encodeBase64(input, false));

    }
    
    /**
     * 将Base64字符串转换为字节数组
     * @param input Base64字符串
     * @return 字节数组
     */
    public static byte[] base64String2Bytes(final String input) throws IOException, DecoderException {
        return (byte[]) new Base64().decode(input);
    }
}
