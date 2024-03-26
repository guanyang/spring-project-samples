package org.gy.demo.webflux;

import org.apache.commons.lang3.StringUtils;

public class ConversionUtil {

    /**
     * 初始化 62 进制数据，索引位置代表字符的数值，比如 A代表10，z代表61等
     */
    private static final String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int scale = 62;

    public static String encodeNew(long num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            int index = (int) (num % scale);
            sb.insert(0, chars.charAt(index));
            num = num / scale;
        }
        return sb.toString();
    }

    public static long decodeNew(String str) {
        long num = 0;
        for (int i = 0; i < str.length(); i++) {
            int index = chars.indexOf(str.charAt(i));
            num += (long) (index * Math.pow(scale, str.length() - 1 - i));
        }
        return num;
    }

    /**
     * 将数字转为62进制
     *
     * @param num Long 型数字
     * @param length 转换后的字符串长度，不足则左侧补0
     * @return 62进制字符串
     */
    public static String encode(long num, int length) {
        StringBuilder sb = new StringBuilder();

        while (num > 0) {
            sb.insert(0, chars.charAt((int) (num % scale)));
            num = num / scale;
        }
        return StringUtils.leftPad(sb.toString(), length, '0');
    }

    /**
     * 62进制字符串转为数字
     *
     * @param str 编码后的62进制字符串
     * @return 解码后的 10 进制字符串
     */
    public static long decode(String str) {
        /**
         * 将 0 开头的字符串进行替换
         */
        str = str.replace("^0*", "");
        long num = 0;
        int index = 0;
        for (int i = 0; i < str.length(); i++) {
            /**
             * 查找字符的索引位置
             */
            index = chars.indexOf(str.charAt(i));
            /**
             * 索引位置代表字符的数值
             */
            num += (long) (index * (Math.pow(scale, str.length() - i - 1)));
        }

        return num;
    }


    /**
     *
     */
    public static void main(String[] args) {
        System.out.println("62进制：" + encode(2576460752303423487L, 11));

        System.out.println("10进制：" + decode("34KDUNMZwuV"));

        System.out.println("62进制：" + encodeNew(2576460752303423487L));
        System.out.println("10进制：" + decodeNew("34KDUNMZwuV"));
    }

}
