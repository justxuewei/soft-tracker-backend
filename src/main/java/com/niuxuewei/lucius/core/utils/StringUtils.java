package com.niuxuewei.lucius.core.utils;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class StringUtils {

    public static String random(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获取文件后缀名
     * @param fileName 文件名
     * @return 123.txt返回txt
     */
    public static String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 获取无后缀的文件名
     * @param fileName 文件名
     * @return
     */
    public static String getFileNameWithoutSuffix(String fileName) {
        if ((fileName != null) && (fileName.length() > 0)) {
            int dot = fileName.lastIndexOf('.');
            if ((dot >-1) && (dot < (fileName.length()))) {
                return fileName.substring(0, dot);
            }
        }
        return null;
    }

    public static String md5(byte[] bytes) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            return DatatypeConverter.printHexBinary(messageDigest.digest(bytes)).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String md5(String text) {
        return md5(text.getBytes());
    }

    public static String booleanToString(boolean bool) {
        return bool? "true" : "false";
    }

}
