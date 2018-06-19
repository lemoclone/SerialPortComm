package com.lexiang.serialportcomm;

import com.lexiang.library.utils.ByteUtil;
import com.lexiang.library.queue.DataHandleListener;
import com.lexiang.library.queue.SerialPortData;

public class CustomSerialPortData extends SerialPortData<Signal> {

    public CustomSerialPortData(Signal data, DataHandleListener requestHandleListener) {
        this.data = data;
        dataHandleListener = requestHandleListener;
    }

    @Override
    public byte[] getDataBytes() {
        return data.toBytes();
    }

    @Override
    public String toString() {
        return ByteUtil.toDisplayString(getDataBytes());
    }
}