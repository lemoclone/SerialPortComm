package com.lexiang.library.queue;

/**
 * the queue consist read data from serial port
 */
public class SerialPortReader {
    private static DataQueue mDataQueue;

    public static void init(DataHandler incomeDataHandler) {
        if (null == mDataQueue) {
            synchronized (SerialPortWriter.class) {
                if(null == mDataQueue) {
                    mDataQueue = new DataQueue(incomeDataHandler, "SerialPortReader");
                    mDataQueue.start();
                }
            }
        }
    }

    public static void addRequest(SerialPortData<?> request) {
        if (null == mDataQueue) {
            return;
        }
        mDataQueue.add(request);
    }

    public static void quit() {
        if (mDataQueue != null) {
            mDataQueue.stop();
        }
    }

    private SerialPortReader() {
    }
}
