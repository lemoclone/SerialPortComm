package com.lexiang.library.queue;

public class SignalSerialPortData extends SerialPortData<Signal> {
    public SignalSerialPortData(Signal data, DataHandleListener requestHandleListener) {
        this.data = data;
        dataHandleListener = requestHandleListener;
    }
}
