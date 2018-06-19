package com.lexiang.library.utils;

public class ByteUtil {

    public static void printBytes(byte[] bytes, String thread) {
        if (null != thread) {
            System.out.println("thread: " + thread);
        }

        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            // System.out.print(v);
            System.out.print(decToHex(v >>> 4));
            System.out.print(decToHex(v & 0x0F));
            System.out.print(" ");
        }
        System.out.println();
    }

    public static String toDisplayString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            sb.append(decToHex(v >>> 4));
            sb.append(decToHex(v & 0x0F));
            sb.append(' ');
        }
        return sb.toString();
    }


    private static char decToHex(int dec) {
        return (char) (dec < 10 ? (48 + dec) : (97 + dec - 10));
    }

    private static int hexCharToInt(char c) {
        int res = c - '0';
        if (res >= 0 && res <= 9) {
            return res;
        } else {
            return c - 'a' + 10;
        }
    }
}
