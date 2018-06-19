package com.lexiang.library.queue;

import java.util.concurrent.PriorityBlockingQueue;

class DataQueue {
    private String mTag = "";
    private final PriorityBlockingQueue<SerialPortData<?>> mSignalRequestQueue = new PriorityBlockingQueue<>();
    private Dispatcher mDispatcher;
    private DataHandler mMessageHandler;

    DataQueue(DataHandler messageHandler, String tag) {
        mMessageHandler = messageHandler;
        mTag = tag;
    }

    public void start() {
        stop();
        mDispatcher = new Dispatcher(mSignalRequestQueue, mMessageHandler,mTag);
        mDispatcher.start();
    }

    public void stop() {
        if (mDispatcher != null) {
            mDispatcher.quit();
        }
    }

    public <T> SerialPortData<T> add(SerialPortData<T> request) {
        synchronized (mSignalRequestQueue) {
            mSignalRequestQueue.add(request);
        }
        return request;
    }
}
