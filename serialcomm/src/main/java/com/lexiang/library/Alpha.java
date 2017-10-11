package com.lexiang.library;

import android.util.Log;

import com.lexiang.library.Util.ByteUtil;
import com.lexiang.library.app_serialport_api.SerialPortClient;
import com.lexiang.library.queue.ByteSerialPortData;
import com.lexiang.library.queue.DataHandleListener;
import com.lexiang.library.queue.DataHandler;
import com.lexiang.library.queue.SerialPortData;
import com.lexiang.library.queue.SerialPortReader;
import com.lexiang.library.queue.SerialPortWriter;
import com.lexiang.library.queue.TRMError;

/**
 * Created by hudafei on 11/10/2017.
 */

public class Alpha {
    private static final String TAG = "Alpha";
    public static void init() {
        SerialPortStrategy serialPortStrategy = new SerialPortStrategy();
        serialPortStrategy.setDataHead(0xFC);
        serialPortStrategy.setDataTail(0xFC);
        serialPortStrategy.setInputStreamSizeInByte(16);
        serialPortStrategy.setSerialPortPath("/dev/ttyS2");

        //init read handler
        DataHandler readerHandler = new DataHandler<byte[]>() {
            @Override
            public void handleData(SerialPortData<byte[]> serialPortData) throws TRMError {
                if (serialPortData.getData() != null) {
                    Log.i("IncomeDataHandler2", ByteUtil.toDisplayString(serialPortData.getData()));
                }
            }
        };

        DataHandler writerHandler = new DataHandler<byte[]>() {
            @Override
            public void handleData(SerialPortData<byte[]> serialPortData) throws TRMError {
                if (SerialPortClient.getInstance().writeData(serialPortData.getData()) > -1) {
                    if (serialPortData.getMessageHandleListener() != null) {
                        serialPortData.getMessageHandleListener().onSucceed(ByteUtil.toDisplayString(serialPortData.getData()));
                    }
                } else {
                    if (serialPortData.getMessageHandleListener() != null) {
                        serialPortData.getMessageHandleListener().onFailed(ByteUtil.toDisplayString(serialPortData.getData()));
                    }
                }
            }
        };
        SerialPortReader.init(readerHandler);
        SerialPortWriter.init(writerHandler);
        SerialPortClient.getInstance().start(serialPortStrategy);

        byte[] bytes = new byte[10];
        SerialPortData<byte[]> byteSerialPortData = new ByteSerialPortData(bytes, new DataHandleListener() {
            @Override
            public void onSucceed(String str) {
                Log.i(TAG,"onSucceed: " + str);
            }

            @Override
            public void onFailed(String str) {
                Log.i(TAG,"onFailed: " + str);
            }
        });
        SerialPortWriter.addWriteRequest(byteSerialPortData);
    }
}
