package top.baogutang.common.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author developer
 * 采用MD5加密
 */
public class MD5Utils {
    /*** 
     * MD5加密 生成32位md5码
     * @param inStr 待加密字符串
     * @return 返回32位md5码
     */
    public static String md5Encode(String inStr) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            return "";
        }

        byte[] byteArray = inStr.getBytes(StandardCharsets.UTF_8);
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = (md5Byte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
