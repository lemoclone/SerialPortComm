package com.lexiang.library.queue;

/**
 * the queue consist read data from serial port
 */
public class SerialPortReader2 {
    private DataQueue mDataQueue;

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

    public SerialPortReader2(DataHandler incomeDataHandler) {
        mDataQueue = new DataQueue(incomeDataHandler, "ReaderThread");
        mDataQueue.start();
    }
}
