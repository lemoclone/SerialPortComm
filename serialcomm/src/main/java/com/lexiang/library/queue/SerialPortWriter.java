package com.lexiang.library.queue;

/**
 * the queue consist write data into serial port
 */
public class SerialPortWriter {
    private static DataQueue mDataQueue;

    public static void init(DataHandler dataHandler) {
        if (null == mDataQueue) {
            synchronized (SerialPortWriter.class) {
                if (null == mDataQueue) {
                    mDataQueue = new DataQueue(dataHandler, "SerialPortWriter");
                    mDataQueue.start();
                }
            }
        }
    }

    public static void addWriteRequest(SerialPortData<?> request) {
        if (mDataQueue == null) {
            return;
        }
        mDataQueue.add(request);
    }

    public static void quit() {
        if (mDataQueue != null) {
            mDataQueue.stop();
        }
    }

    private SerialPortWriter() {
    }
}
