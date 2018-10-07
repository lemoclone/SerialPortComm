package com.lexiang.library;

import android.util.Log;

import com.lexiang.library.app_serialport_api.Constants;
import com.lexiang.library.app_serialport_api.SerialPortClient;
import com.lexiang.library.queue.DataHandleListener;
import com.lexiang.library.queue.DataHandler;
import com.lexiang.library.queue.SerialPortData;
import com.lexiang.library.queue.SerialPortReader;
import com.lexiang.library.queue.SerialPortWriter;
import com.lexiang.library.utils.ByteSerialPortData;
import com.lexiang.library.utils.ByteUtil;

/**
 * Created by hudafei on 11/10/2017.
 * serial port demo
 */

public class Alpha {
    private static final String TAG = "Alpha";

    public static void init() {
        SerialPortParams serialPortParams = new SerialPortParams.Builder()
                .serialPortBaudRate(Constants.SERIAL_BAUD_RATE_4800)
                .serialPortDataBits(Constants.DATABITS_8)
                .serialPortParity(Constants.PARITY_NONE)
                .serialPortStopBits(Constants.STOPBITS_1)
                .serialPortReaderIntervalTimeInMillis(10)  //set the read interval time
                .inputStreamSizeInByte(128)  //set the read buffer size
                .serialPortPath("/dev/ttyO3").build(); //set the device port path

        final SerialPortClient serialPortClient = new SerialPortClient();


        DataHandler readerHandler = new DataHandler() {
            @Override
            public void handleData(SerialPortData serialPortData) {
                if (serialPortData.getDataBytes() != null) {
                    Log.i("IncomeDataHandler2", ByteUtil.toDisplayString(serialPortData.getDataBytes()));
                }
            }
        };

        DataHandler writerHandler = new DataHandler() {
            @Override
            public void handleData(SerialPortData serialPortData) {
                if (serialPortClient.writeData(serialPortData.getDataBytes()) > -1) {
                    if (serialPortData.getDataHandleListener() != null) {
                        serialPortData.getDataHandleListener().onSucceed(ByteUtil.toDisplayString(serialPortData.getDataBytes()));
                    }
                } else {
                    if (serialPortData.getDataHandleListener() != null) {
                        serialPortData.getDataHandleListener().onFailed(ByteUtil.toDisplayString(serialPortData.getDataBytes()));
                    }
                }
            }
        };

        SerialPortReader serialPortReader = new SerialPortReader(readerHandler);
        SerialPortWriter serialPortWriter = new SerialPortWriter(writerHandler);


        //i.e. write a empty byte array with length of 10 into serial port
        byte[] bytes = new byte[10];
        SerialPortData<byte[]> byteSerialPortData = new ByteSerialPortData(bytes, new DataHandleListener() {
            @Override
            public void onSucceed(String str) {
                Log.i(TAG, "onDataChanged: " + str);
            }

            @Override
            public void onFailed(String str) {
                Log.i(TAG, "onFailed: " + str);
            }
        });

        serialPortWriter.addWriteRequest(byteSerialPortData);
    }
}
