package com.lexiang.library.queue;

/**
 * the queue consist write data into serial port
 */
public class SerialPortWriter {
    private static DataQueue mDataQueue;
    private static SerialPortData previousRequest;

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
        previousRequest = request;
        mDataQueue.add(request);
    }

    public static void quit() {
        if (mDataQueue != null) {
            mDataQueue.stop();
        }
    }

    public static void reSent() {
        mDataQueue.add(previousRequest);
    }

    private SerialPortWriter() {
    }
}
