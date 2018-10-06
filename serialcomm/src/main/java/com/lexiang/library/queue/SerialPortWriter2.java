package com.lexiang.library.queue;

/**
 * the queue consist write data into serial port
 */
public class SerialPortWriter2 {
    private DataQueue mDataQueue;
    private SerialPortData previousRequest;

    public void init(DataHandler dataHandler) {
        if (null == mDataQueue) {
            synchronized (SerialPortWriter2.class) {
                if (null == mDataQueue) {
                    mDataQueue = new DataQueue(dataHandler, "SerialPortWriter");
                    mDataQueue.start();
                }
            }
        }
    }

    public void addWriteRequest(SerialPortData<?> request) {
        if (mDataQueue == null) {
            return;
        }
        previousRequest = request;
        mDataQueue.add(request);
    }

    public void quit() {
        if (mDataQueue != null) {
            mDataQueue.stop();
        }
    }

    public void reSent() {
        mDataQueue.add(previousRequest);
    }

    private SerialPortWriter2() {
    }
}
