package com.lexiang.library.queue;

/**
 * the queue consist write data into serial port
 */
public class SerialPortWriter {
    private DataQueue mDataQueue;
    private SerialPortData previousRequest;

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

    public SerialPortWriter(DataHandler dataHandler) {
        mDataQueue = new DataQueue(dataHandler, "SerialPortWriter");
        mDataQueue.start();
    }
}
