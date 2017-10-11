package com.lexiang.library.Util;

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

    public static void printBytes(byte[] bytes) {
        printBytes(bytes, null);
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

    public static int arrayToInt(char[] chars) {
        int res = 0;
        for (int i = 0; i < chars.length; i++) {
            res = res * 16 + hexCharToInt(chars[i]);
        }
        return res;
    }

    public static String intToByteChar(int i) {
        char[] chars = new char[2];
        chars[0] = decToHex((i & 0xFF) >>> 4);
        chars[1] = decToHex(i & 0x0F);
        return chars[0] + "" + chars[1];

    }

    /**
     * |开始 | 序号 | 长度 | 命令编号 |  数据   | 校验和 | 结束符 |
     * |  1  |  1  |  1  |  1      |  变长   |   1   |    1  |
     * <p>
     * 校验和
     * 除了命令开始字符和结束符外，其它字节都参与校验和计算，校验和由相关字节异或而得，如：
     * 校验和 =  DATA1^DATA2^DATA3^...
     */
    public static int calValidSum(byte[] buffer, int start, int end) {
        if (start < 0 || end > buffer.length - 1) {
            return 0;
        }
        int res = buffer[start];
        for (int i = start + 1; i <= end; i++) {
            res = res ^ buffer[i];
        }
        return res;
    }
}
