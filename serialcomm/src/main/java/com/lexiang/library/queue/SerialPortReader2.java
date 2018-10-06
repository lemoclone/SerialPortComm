package com.lexiang.library.queue;

/**
 * the queue consist read data from serial port
 */
public class SerialPortReader2 {
    private DataQueue mDataQueue;

    public void init(DataHandler incomeDataHandler) {
        if (null == mDataQueue) {
            synchronized (SerialPortWriter.class) {
                if(null == mDataQueue) {
                    mDataQueue = new DataQueue(incomeDataHandler, "ReaderThread");
                    mDataQueue.start();
                }
            }
        }
    }

    public void addRequest(SerialPortData<?> request) {
        if (null == mDataQueue) {
            return;
        }
        mDataQueue.add(request);
    }

    public void quit() {
        if (mDataQueue != null) {
            mDataQueue.stop();
        }
    }

    private SerialPortReader2() {
    }
}
