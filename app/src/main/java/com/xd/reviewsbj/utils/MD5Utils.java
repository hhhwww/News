package com.xd.reviewsbj.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hhhhwei on 16/1/14.
 */
public class MD5Utils {
    public static String encouder(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = messageDigest.digest(password.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : bytes) {
                int i = b & 0xff;
                stringBuilder.append(Integer.toHexString(i));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMd5ByFile(String sourceDir) {
        StringBuilder stringBuilder = new StringBuilder();
        File file = new File(sourceDir);
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len = -1;
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            while ((len = fileInputStream.read(bytes)) != -1) {
                messageDigest.update(bytes, 0, len);
            }

            byte[] digest = messageDigest.digest();

            for (byte b : digest) {
                int i = b & 0xff;
                String hexString = Integer.toHexString(i);
                if (hexString.length() == 1)
                    hexString = '0' + hexString;
                stringBuilder.append(hexString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stringBuilder.toString();
    }

}
