package com.common.base.utils.base;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by HSK° on 2018/9/10.
 * --function: md5工具
 */
public class MD5_SC {
    private final static String[] hexDigits = {
            "0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"
    };

    public static String byteArrayToHexString(byte[] b)
    {
        StringBuilder resultSb = new StringBuilder();
        for (int i=0;i<b.length;i++)
        {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static Object byteToHexString(byte b)
    {
        int n=b;
        if (n<0)
            n = 256 + n;
        int d1 = n/16;
        int d2 = n%16;

        return hexDigits[d1] + hexDigits[d2];
    }

    public static String compile(String origin)
    {
        String resultString = null;

        MessageDigest md;
        try
        {
            resultString = origin;
            md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return resultString;
    }

}