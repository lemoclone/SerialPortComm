package com.lexiang.library.Util;

import com.lexiang.library.queue.DataHandleListener;
import com.lexiang.library.queue.SerialPortData;

public class ByteSerialPortData extends SerialPortData<byte[]> {

    public ByteSerialPortData(byte[] data, DataHandleListener requestHandleListener) {
        this.data = data;
        dataHandleListener = requestHandleListener;
    }

    @Override
    public byte[] getDataBytes() {
        return data;
    }

    @Override
    public String toString() {
        return ByteUtil.toDisplayString(data);
    }
}