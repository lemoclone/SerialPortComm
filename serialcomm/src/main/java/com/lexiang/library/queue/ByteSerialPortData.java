package com.lexiang.library.queue;

import com.lexiang.library.Util.ByteUtil;

public class ByteSerialPortData extends SerialPortData<byte[]> {

    public ByteSerialPortData(byte[] data, DataHandleListener requestHandleListener) {
        this.data = data;
        dataHandleListener = requestHandleListener;
    }

    @Override
    public String toString() {
        return ByteUtil.toDisplayString(data);
    }
}