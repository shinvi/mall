package com.shinvi.mall.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    public MD5Util() {
    }

    public static boolean isNotNull(String value) {
        return value != null && !"".equalsIgnoreCase(value.trim()) && !"null".equalsIgnoreCase(value.trim()) && value.length() > 0;
    }

    public static String getMD5(String val) {
        MessageDigest md5 = null;

        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var4) {
            ;
        }

        md5.reset();
        md5.update(val.getBytes());
        byte[] m = md5.digest();
        String result = toHexString(m, "");
        return result;
    }

    private static String toHexString(byte[] bytes, String separator) {
        StringBuilder hexString = new StringBuilder();
        byte[] var4 = bytes;
        int var5 = bytes.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            byte b = var4[var6];
            String hexstring = Integer.toHexString(255 & b);
            if (hexstring.length() < 2) {
                hexString.append("0");
            }

            hexString.append(Integer.toHexString(255 & b)).append(separator);
        }

        return hexString.toString();
    }

    public static String formatVoucherF(String voucher) {
        if (voucher != null && !voucher.trim().equals("")) {
            StringBuilder sBuilder = new StringBuilder();
            int pos = 0;

            do {
                if (sBuilder.length() == 0) {
                    sBuilder.append(voucher.substring(pos, pos + 4));
                } else {
                    sBuilder.append(String.format("  %s", voucher.substring(pos, pos + 4)));
                }

                pos += 4;
            } while(pos < voucher.length());

            return sBuilder.toString();
        } else {
            return "";
        }
    }
}
