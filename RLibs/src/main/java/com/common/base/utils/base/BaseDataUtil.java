package com.common.base.utils.base;

/**
 * Created by HSK° on 2018/9/10.
 * --function:
 */
public class BaseDataUtil {

    public static String GBK2Unicode(String str) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char chr1 = (char) str.charAt(i);

            if (!isNeedConvert(chr1)) {
                result.append(chr1);
                continue;
            }

            result.append("\\u").append(Integer.toHexString((int) chr1));
        }

        return result.toString();
    }






    private static boolean isNeedConvert(char para) {
        return ((para & (0x00FF)) != para);
    }


    private static boolean isEmojiCharacter(char codePoint) {

        return (codePoint == 0x0) ||

                (codePoint == 0x9) ||

                (codePoint == 0xA) ||

                (codePoint == 0xD) ||

                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||

                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||

                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));

    }

    /**
     *
     * 过滤emoji 或者 其他非文字类型的字符
     *
     * @param source
     *
     * @return
     */

    public static String filterEmoji(String source) {

        if (!containsEmoji(source)) {

            return source;// 如果不包含，直接返回

        }

        // 到这里铁定包含

        StringBuilder buf = null;

        int len = source.length();

        for (int i = 0; i < len; i++) {

            char codePoint = source.charAt(i);

            if (isEmojiCharacter(codePoint)) {

                if (buf == null) {

                    buf = new StringBuilder(source.length());

                }

                buf.append(codePoint);

            } else {

            }

        }

        if (buf == null) {

            return source;// 如果没有找到 emoji表情，则返回源字符串

        } else {

            if (buf.length() == len) {// 这里的意义在于尽可能少的toString，因为会重新生成字符串

                buf = null;

                return source;

            } else {

                return buf.toString();

            }

        }

    }

    /**
     *
     * 检测是否有emoji字符
     *
     * @param source
     *
     * @return 一旦含有就抛出
     */

    public static boolean containsEmoji(String source) {

        int len = source.length();

        for (int i = 0; i < len; i++) {

            char codePoint = source.charAt(i);

            if (isEmojiCharacter(codePoint)) {

                // do nothing，判断到了这里表明，确认有表情字符

                return true;

            }

        }

        return false;

    }

}
