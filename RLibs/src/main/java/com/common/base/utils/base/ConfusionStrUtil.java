package com.common.base.utils.base;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HSK° on 2018/9/10.
 * --function: 混淆及解密二进制串
 */
public class ConfusionStrUtil {
    public static int keyLength = 16;
    public static long maxTimeoutSecond = 30 * 60;

    /**
     * 混淆身份信息和内容信息
     */
    public static String encryptionStrs(String key, String text)
             {
        String result = "";
        if (key != null && key.length() == keyLength) {
            if (text == null || text.length() == 0) {
                result = key;
            } else if (text.length() < keyLength) {
                char[] keyChars = key.toCharArray();
                char[] textChars = text.toCharArray();
                char[] resultChars = new char[keyChars.length
                        + textChars.length];
                int i = 0;
                int j = 0;
                boolean flag = true;
                boolean first = true;

                while (i < keyChars.length + textChars.length) {
                    if (flag) {
                        resultChars[i] = keyChars[j];
                        flag = false;
                        i += 1;
                    } else if (!flag && j < textChars.length) {
                        resultChars[i] = textChars[j];
                        flag = true;
                        i += 1;
                        j += 1;
                    } else {
                        if (first) {
                            j += 1;
                            first = false;
                        }
                        resultChars[i] = keyChars[j];
                        i += 1;
                        j += 1;
                    }
                }
                result = String.valueOf(resultChars);
            } else {
                int rate = text.length() / keyLength;
                char[] keyChars = key.toCharArray();
                char[] textChars = text.toCharArray();
                char[] resultChars = new char[keyChars.length
                        + textChars.length];
                int i = 0;
                int j = 0;
                int k = 0;
                int cu = 0;
                boolean flag = true;

                while (i < resultChars.length) {
                    if (flag && j < keyLength) {
                        resultChars[i] = keyChars[j];
                        flag = false;
                        i += 1;
                        j += 1;
                    } else if (!flag && k < keyLength * rate) {
                        resultChars[i] = textChars[k];
                        i += 1;
                        k += 1;
                        cu += 1;
                        if (cu == rate) {
                            flag = true;
                            cu = 0;
                        }
                    } else {
                        resultChars[i] = textChars[k];
                        i += 1;
                        k += 1;
                    }
                }
                result = String.valueOf(resultChars);
            }
        } else {
            throw new IllegalArgumentException("key length must be equal to "
                    + keyLength);
        }
        return result;
    }

    /**
     * 解开身份信息和内容信息,
     */
    public static List<String> decryptionStr(String str)
             {
        List<String> list = new ArrayList<String>();
        String key = "", text = "";
        if (str != null && str.length() >= keyLength) {
            if (str.length() == keyLength) {
                key = str;
            } else if (str.length() < keyLength * 2) {
                char[] strs = str.toCharArray();
                int j = str.length() - keyLength;
                char[] keyChars = new char[keyLength];
                char[] textChars = new char[j];
                boolean flag = true;
                int k = 0;
                int m = 0;

                for (int i = 0; i < str.length(); i++) {
                    if (flag) {
                        keyChars[m] = strs[i];
                        m += 1;
                        flag = false;
                    } else if (!flag && k < j) {
                        textChars[k] = strs[i];
                        k += 1;
                        flag = true;
                    } else {
                        keyChars[m] = strs[i];
                        m += 1;
                    }
                }
                key = String.valueOf(keyChars);
                text = String.valueOf(textChars);
            } else {
                char[] strs = str.toCharArray();
                int j = str.length() - keyLength;
                char[] keyChars = new char[keyLength];
                char[] textChars = new char[j];
                boolean flag = true;
                int m = 0;
                int k = 0;
                int cu = 0;
                int rate = (str.length() - keyLength) / keyLength;

                for (int i = 0; i < str.length(); i++) {
                    if (flag && m < keyLength) {
                        keyChars[m] = strs[i];
                        m += 1;
                        flag = false;
                    } else if (!flag && k < keyLength * rate) {
                        textChars[k] = strs[i];
                        k += 1;
                        cu += 1;
                        if (cu == rate) {
                            flag = true;
                            cu = 0;
                        }
                    } else {
                        textChars[k] = strs[i];
                        k += 1;
                    }
                }
                key = String.valueOf(keyChars);
                text = String.valueOf(textChars);
            }
            list.add(key);
            list.add(text);
        } else {
            throw new IllegalArgumentException("str length must be greater than "
                    + keyLength);
        }
        return list;
    }
}

