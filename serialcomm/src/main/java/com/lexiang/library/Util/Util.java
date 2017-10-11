package com.lexiang.library.Util;

import com.lexiang.library.SerialPortStrategy;
import com.lexiang.library.exception.SerialException;
import com.lexiang.library.log.Logger;
import com.lexiang.library.log.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Util {
    private static Logger log = LoggerFactory.getLog(Util.class);

    /**
     * 逐行写入
     *
     * @param path
     * @param content
     */
    public static void writeFile(String path, String content) {
        writeFile(path, content, true);
    }

    /**
     * 休眠,单位毫秒
     * @param secondInMillis 毫秒
     */
    public static void sleep(long secondInMillis){
        try {
            Thread.sleep(secondInMillis);
        } catch (InterruptedException e) {
            log.error(new SerialException(e));
        }
    }


    public static byte[] readInput(byte[] buffer) {
        int start = -1;
        int end = -1;
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] == SerialPortStrategy.getDataHead()) {
                start = i;
            }
            if (buffer[i] == SerialPortStrategy.getDataTail()) {
                end = i;
            }
            if (start != -1 && end != -1) {
                byte[] res = new byte[end - start + 1];
                System.arraycopy(buffer, start, res, 0, end - start + 1);
                return res;
            }
        }
        return null;
    }

    private static void writeFile(String path, String content, boolean append) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
            }
            FileWriter fw = new FileWriter(path, append);
            String c = content + "\r\n";
            fw.write(c);
            fw.close();
        } catch (IOException e) {
            log.error(new SerialException(e));
        }
    }

}
