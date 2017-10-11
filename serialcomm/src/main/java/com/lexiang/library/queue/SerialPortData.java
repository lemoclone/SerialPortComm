package com.lexiang.library.queue;


public class SerialPortData<T> implements Comparable<SerialPortData<T>> {
    private final String TAG = this.getClass().getSimpleName();
    protected T data;
    protected DataHandleListener dataHandleListener;

    public DataHandleListener getMessageHandleListener() {
        return dataHandleListener;
    }

    public SerialPortData() {
    }

    public SerialPortData(T data) {
        this.data = data;
    }

    public SerialPortData(T data, DataHandleListener requestHandleListener) {
        this.data = data;
        this.dataHandleListener = requestHandleListener;

    }

    public T getData() {
        return data;
    }

    @Override
    public int compareTo(SerialPortData<T> other) {
        if (other == null) {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "";
    }
}
