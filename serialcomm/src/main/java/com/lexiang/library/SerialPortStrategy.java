package com.lexiang.library;

/**
 * Created by hudafei on 11/10/2017.
 */

public class SerialPortStrategy {
    //Command head
    public static int dataHead = 0xFC;
    //Command tail
    public static int dataTail = 0xFD;
    //Input stream byte size
    public int inputStreamSizeInByte = 16;
    //Serial port path
    public String serialPortPath = "/dev/ttyS2";

    public static int getDataHead() {
        return dataHead;
    }

    public static void setDataHead(int dataHead) {
        SerialPortStrategy.dataHead = dataHead;
    }

    public static int getDataTail() {
        return dataTail;
    }

    public static void setDataTail(int dataTail) {
        SerialPortStrategy.dataTail = dataTail;
    }

    public int getInputStreamSizeInByte() {
        return inputStreamSizeInByte;
    }

    public void setInputStreamSizeInByte(int inputStreamSizeInByte) {
        this.inputStreamSizeInByte = inputStreamSizeInByte;
    }

    public String getSerialPortPath() {
        return serialPortPath;
    }

    public void setSerialPortPath(String serialPortPath) {
        this.serialPortPath = serialPortPath;
    }
}
