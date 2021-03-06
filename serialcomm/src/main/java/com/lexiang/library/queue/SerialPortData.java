package com.lexiang.library.queue;


abstract public class SerialPortData<T> implements Comparable<SerialPortData<T>> {
    private final String TAG = this.getClass().getSimpleName();
    protected T data;
    protected DataHandleListener dataHandleListener;

    public DataHandleListener getDataHandleListener() {
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

    /**
     * must override by subclass for object serial data
     * @return the byte[] of data
     */
    public abstract byte[] getDataBytes();

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
