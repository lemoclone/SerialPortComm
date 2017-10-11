package com.lexiang.library.queue;

public interface DataHandler<T> {
    void handleData(SerialPortData<T> serialPortData) throws TRMError;
}
