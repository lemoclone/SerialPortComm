package com.lexiang.library.app_serialport_api;


import com.lexiang.library.SerialPortStrategy;
import com.lexiang.library.Util.DefaultSerialPortListener;
import com.lexiang.library.Util.Util;
import com.lexiang.library.exception.SerialException;
import com.lexiang.library.log.Logger;
import com.lexiang.library.log.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SerialPortClient {

    private Logger log = LoggerFactory.getLog(SerialPortClient.class);

    public int mInputStreamSizeInByte = 16;
    public String mSerialPortPath = "/dev/ttyS2";

    /**
     * 输入流，来自串口
     */
    private FileInputStream mInputStream;

    /**
     * 输出流，流向串口
     */
    private FileOutputStream mOutputStream;

    /**
     * 服务执行者
     */
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(2, new CustomThreadFactory("12"));

    private Connector mConnector;

    private SerialPortListener mSerialPortListener;

    private static SerialPortClient mSerialPortClient;

    public static SerialPortClient getInstance(){
        if (mSerialPortClient == null) {
            synchronized (SerialPortClient.class) {
                if (mSerialPortClient == null) {
                    mSerialPortClient = new SerialPortClient();
                }
            }
        }
        return mSerialPortClient;
    }

    public void start(SerialPortStrategy serialPortStrategy){
        start(serialPortStrategy,null);
    }

    public void start(SerialPortStrategy serialPortStrategy,
                             SerialPortListener serialPortListener){
        mSerialPortClient.mInputStreamSizeInByte = serialPortStrategy.inputStreamSizeInByte;
        mSerialPortClient.mSerialPortPath = serialPortStrategy.serialPortPath;
        if(serialPortListener == null) {
            serialPortListener = new DefaultSerialPortListener();
        }
        mSerialPortClient.setListener(serialPortListener);
    }

    /**
     * add serial port listener and try to open port
     * @param serialPortListener
     */
    private void setListener(SerialPortListener serialPortListener) {
        this.mSerialPortListener = serialPortListener;
        connect();
        Util.sleep(1000);
        startListenSerialPort();
    }

    /**
     * close serial port
     */
    public void close() {
        try {
            this.mConnector.close();
            this.mExecutorService.shutdownNow();
        } catch (Exception e) {
            log.error(new SerialException(e));
        }
    }

    public int writeData(byte[] data) {
        if (data.length <= 0) return -1;
        if (mOutputStream == null) {
            return -1;
        }
        try {
            mOutputStream.write(data);
            mOutputStream.flush();
            return data.length;
        } catch (IOException e) {
            e.printStackTrace();
            log.error(new SerialException(e));
        }
        return -1;
    }

    public byte[] readData() {
        if (null == mInputStream) {
            return null;
        }
        int len;
        byte[] buffer = new byte[64];
        try {
            len = mInputStream.read(buffer);
            if (len > 0) {
                byte[] bytes = Util.readInput(buffer);
                return bytes;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void connect() {
        try {
            if (mConnector != null) {
                close();
            }
            mConnector = new Connector(mSerialPortPath, Connector.SERIAL_BAUD_RATE_4800, Connector.DATABITS_8, Connector.STOPBITS_1, Connector.PARITY_NONE);
            mConnector.open();

            mInputStream = mConnector.getInputStream();
            mOutputStream = mConnector.getOutputStream();
            log.info("setListener mInputStream:" + mInputStream + " mOutputStream:" + mOutputStream);

        } catch (SecurityException e) {
            log.error(new SerialException(e));
        }
    }

    private void startListenSerialPort() {
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                readDataFromSerialPort();
            }
        });
    }

    private void readDataFromSerialPort() {
        while (true) {
            try {
                Util.sleep(100);
                if (mInputStream == null) {
                    continue;
                }
                byte[] buffer = new byte[mInputStreamSizeInByte];
                int len = mInputStream.read(buffer);
                if (len > 0) {
                    //log.info("readDataFromSerialPort length is:" + len + ", data is:" + new String(buffer, 0, len));
                    //LogUtil.i("XhSerialPort", "收到串口数据: " + ByteUtil.toDisplayString(buffer));
                    mSerialPortListener.onSucceed(buffer);
                }
            } catch (Exception e) {
                log.error(new SerialException(e));
            }
        }
    }
}
