package com.sinosoft.ops.cimp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * @ClassName:  CryptoUtil
 * @Description: 加解密工具类
 * <p>AES密钥的长度支持128位、192位、256位（192和256需替换local_policy.jar和US_export_policy.jar）</p>
 * @Author:        Nil
 * @Date:            2017年8月1日 上午10:20:51
 * @Version        1.0.0
 */
public class CryptoUtil {

    private static char[] hexChar;
    static {
        hexChar = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    }

    /**
     * 使用AES加密
     * @param plaintext 明文
     * @param password 密码
     * @return 密文
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     * @throws InvalidKeyException 
     */
    public static byte[] encryptAes(final byte[] plaintext,final byte[] password) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException{
        Cipher cipher=Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(password,"AES"));
        return cipher.doFinal(plaintext);
    }

    /**
     * 使用AES解密
     * @param ciphertext 密文
     * @param password 密码
     * @return 明文
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     * @throws InvalidKeyException  
     */
    public static byte[] decryptAes(final byte[] ciphertext,final byte[] password) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException{
        Cipher cipher=Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(password,"AES"));
        return cipher.doFinal(ciphertext);
    }
    
    /**
     * 使用AES加密文件
     * @param plainFile 明文文件
     * @param cipherFile 密文文件
     * @param password 密码
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     */
    public static void encryptFileAes(final File plainFile,final File cipherFile,final byte[] password) throws FileNotFoundException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
        int length=0;
        byte[] buffer=new byte[4096];
        Cipher cipher=Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(password,"AES"));
        try(FileInputStream is= new FileInputStream(plainFile);
                FileOutputStream os = new FileOutputStream(cipherFile);){
            try(CipherOutputStream cos=new CipherOutputStream(os,cipher)){
                while((length=is.read(buffer))>0){
                    cos.write(buffer,0,length);
                }
                cos.flush();
            }
        }
    }
    
    /**
     * 使用AES解密文件
     * @param cipherFile 密文文件
     * @param plainFile 明文文件
     * @param password 密码
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     */
    public static void decryptFileAes(final File cipherFile,final File plainFile,final byte[] password) throws FileNotFoundException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
        int length=0;
        byte[] buffer=new byte[4096];
        Cipher cipher=Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(password,"AES"));
        try(FileInputStream is= new FileInputStream(cipherFile);
                FileOutputStream os = new FileOutputStream(plainFile);){
            try(CipherOutputStream cos=new CipherOutputStream(os,cipher)){
                while((length=is.read(buffer))>0){
                    cos.write(buffer,0,length);
                }
                cos.flush();
            }
        }
    }
    
    /**
     * 获取字符串的MD5消息摘要
     * @param input 字符串
     * @return 十六进制的消息摘要字符串
     * @throws NoSuchAlgorithmException
     */ 
    public static String getStringMd5(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        return ConvertUtil.bytes2HexString(CryptoUtil.getMessageDigest(input.getBytes("UTF-8"),"MD5"));
    }
    
    /**
     * 获取文件的MD5消息摘要
     * @param file 文件名
     * @return 十六进制的消息摘要字符串
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */ 
    public static String getFileMd5(File file) throws NoSuchAlgorithmException, IOException{
        return ConvertUtil.bytes2HexString(CryptoUtil.getMessageDigest(file,"MD5"));
    }
    
    /**
     * 获取字符串的SHA-256消息摘要
     * @param input 字符串
     * @return 十六进制的消息摘要字符串
     * @throws NoSuchAlgorithmException
     */ 
    public static String getStringSha256(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        return ConvertUtil.bytes2HexString(CryptoUtil.getMessageDigest(input.getBytes("UTF-8"),"SHA-256"));
    }
    
    /**
     * 获取文件的SHA-256消息摘要
     * @param file 文件名
     * @return 十六进制的消息摘要字符串
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */ 
    public static String getFileSha256(File file) throws NoSuchAlgorithmException, IOException{
        return ConvertUtil.bytes2HexString(CryptoUtil.getMessageDigest(file,"SHA-256"));
    }
    
    /**
     * 获取消息字节流的消息摘要
     * @param input 输入字节数组
     * @param algorithm 摘要算法（MD2、MD5、SHA-1、SHA-224、SHA-256、SHA-384、SHA-512）
     * @return 消息摘要
     * @throws NoSuchAlgorithmException
     */
    public static byte[] getMessageDigest(byte[] input,String algorithm) throws NoSuchAlgorithmException{
        return MessageDigest.getInstance(algorithm).digest(input);
    }
    
    /**
     * 获取文件的消息摘要
     * @param file 文件名
     * @param algorithm 摘要算法（MD2、MD5、SHA-1、SHA-224、SHA-256、SHA-384、SHA-512）
     * @return 消息摘要
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static byte[] getMessageDigest(File file,String algorithm) throws NoSuchAlgorithmException, IOException{
        int length = 0;
        byte[] buffer = new byte[4096];
        
        try(FileInputStream fis = new FileInputStream(file)){
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            while ((length = fis.read(buffer)) != -1) {
                messageDigest.update(buffer, 0, length);
            }
            return messageDigest.digest();
        }
    }
    
    public static String getStringMd5OfBiz(String str){
        return getDigestHash(str, "MD5");
    }

    public static String getDigestHash(String str, String hashType) {
        if (str == null) {
            return null;
        } else {
            try {
                MessageDigest messagedigest = MessageDigest.getInstance(hashType);
                byte[] strBytes = str.getBytes();
                messagedigest.update(strBytes);
                byte[] digestBytes = messagedigest.digest();
                int k = 0;
                char[] digestChars = new char[digestBytes.length * 2];

                for(int l = 0; l < digestBytes.length; ++l) {
                    int i1 = digestBytes[l];
                    if (i1 < 0) {
                        i1 = 127 + i1 * -1;
                    }
                    encodeInt(i1, k, digestChars);
                    k += 2;
                }

                return new String(digestChars, 0, digestChars.length);
            } catch (Exception var9) {
                return str;
            }
        }
    }
    public static char[] encodeInt(int i, int j, char[] digestChars) {
        if (i < 16) {
            digestChars[j] = '0';
        }
        ++j;
        do {
            digestChars[j--] = hexChar[i & 15];
            i >>>= 4;
        } while(i != 0);

        return digestChars;
    }
}
