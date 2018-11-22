package com.lexiang.library.utils;

import com.lexiang.library.app_serialport_api.SerialPortReaderListener;
import com.lexiang.library.queue.DataHandleListener;
import com.lexiang.library.queue.SerialPortData;

/**
 * Created by hudafei on 11/10/2017.
 */

public class ByteSerialPortReaderListener implements SerialPortReaderListener {

    @Override
    public void onDataChanged(byte[] data, int realSize) {
        SerialPortData serialPortData = new ByteSerialPortData(data, new DataHandleListener() {
            @Override
            public void onSucceed(String str) {
                //ignored
            }

            @Override
            public void onFailed(String str) {
                //ignored
            }
        });
    }
}
