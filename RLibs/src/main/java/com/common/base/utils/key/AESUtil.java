package com.common.base.utils.key;

import com.common.base.utils.base.ZipUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
//import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    static String ivalue = "abcdefgh98765432";

    /**
     * 加密
     *
     * @param content 待加密内容
     * @param key     加密的密钥
     * @return
     */
    public static String encrypt(String content, String key)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException,
            InvalidAlgorithmParameterException, BadPaddingException,
            UnsupportedEncodingException, IOException {
        // /key处理
        //KeyGenerator kgen = KeyGenerator.getInstance("AES");
        //kgen.init(128, new SecureRandom(key.getBytes("UTF-8")));
        //SecretKey secretKey = kgen.generateKey();
        //SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        //byte[] enCodeFormat = secretKey.getEncoded();
        byte[] enCodeFormat = key.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
        // content处理
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//		Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
        if (content.length() >= 5120) {
            content = ZipUtil.compress(content);
        }
        byte[] byteContent = content.getBytes("GBK");
        AlgorithmParameterSpec iv = new IvParameterSpec(
                ivalue.getBytes("UTF-8"));
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
        byte[] byteResult = cipher.doFinal(byteContent);
        String res = byte2hex(byteResult);
        if (res.length() >= 10240) {// 二次压缩
            res = ZipUtil.compress(res);
        }
        return res;
    }

    /**
     * 解密
     *
     * @param content 待解密内容
     * @param key     解密的密钥
     * @return
     */
    public static String decrypt(String content, String key)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException,
            InvalidAlgorithmParameterException, BadPaddingException,
            IOException {
        /**
         * 当密文为空，或者小于2位,防止异常
         */
        if (content == null || content.length() < 2)
            return null;
        if (key == null) {
            key = "";
        }
        if (content.startsWith("1F8B08000000000000")) {
            content = ZipUtil.unCompress(content);
        }
        byte[] byteRresult = hex2byte(content);// hex2byte(content);
        //KeyGenerator kgen = KeyGenerator.getInstance("AES");
        //kgen.init(128, new SecureRandom(key.getBytes("UTF-8")));
        //SecretKey secretKey = kgen.generateKey();
        //byte[] enCodeFormat = secretKey.getEncoded();
        //SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        AlgorithmParameterSpec iv = new IvParameterSpec(ivalue.getBytes("UTF-8"));
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
        byte[] result = cipher.doFinal(byteRresult);

        String res = new String(result, "GBK");
        if (res.startsWith("1F8B08000000000000")) {
            res = ZipUtil.unCompress(res);
        }
        return res;
    }

    private static void printbyte(byte[] b) {
        for (int i = 0; i < b.length; i++) {
            System.out.print(b[i]);
        }
        System.out.println("");
    }

    private static String byte2Str(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append((char) b[i]);
        }
        return sb.toString();
    }

    private static byte[] str2byte(String src) {
        int l = src.length();
        byte[] b = new byte[l];
        for (int i = 0; i < l; i++) {
            b[i] = (byte) src.charAt(i);
        }
        return b;
    }

    /**
     * byte转为16进制字符串
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    public static String hex2Str(String hexSrc) {
        return byte2Str(hex2byte(hexSrc));
    }

    /**
     * 16进制字符串转为byte数组
     *
     * @param src
     * @return
     */
    public static byte[] hex2byte(String src) {
        int m = 0;
        int n = 0;
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            m = i * 2 + 1;
            n = m + 1;
            ret[i] = uniteBytes(src.substring(i * 2, m), src.substring(m, n));
        }
        return ret;
    }

    /**
     * 将16进制字符２位转换为一个byte。
     */
    private static byte uniteBytes(String src0, String src1) {
        byte b0 = Byte.decode("0x" + src0);
        b0 = (byte) (b0 << 4);
        byte b1 = Byte.decode("0x" + src1);
        byte ret = (byte) (b0 | b1);
        return ret;
    }

    /**
     * 获得字符串编码格式
     **/
    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }
}
