package com.lexiang.library.Util;

import android.util.Log;

import com.lexiang.library.app_serialport_api.SerialPortReaderListener;
import com.lexiang.library.queue.DataHandleListener;
import com.lexiang.library.queue.SerialPortData;
import com.lexiang.library.queue.SerialPortReader;

/**
 * Created by hudafei on 11/10/2017.
 */

public class ByteSerialPortReaderListener implements SerialPortReaderListener {
    @Override
    public void onFailed() {
        Log.w("SerialPortListener", "serial port read fail");
    }

    @Override
    public void onSucceed(byte[] data) {
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
        SerialPortReader.addRequest(serialPortData);
    }
}
