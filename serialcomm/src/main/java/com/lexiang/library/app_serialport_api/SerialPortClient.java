package com.lexiang.library.app_serialport_api;


import com.lexiang.library.SerialPortStrategy;
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

    private Logger logger = LoggerFactory.getLog(SerialPortClient.class);

    public int inputStreamSizeInByte = 16;
    public String serialPortPath = "/dev/ttyS2";

    /**
     * 输入流，来自串口
     */
    private FileInputStream inputStream;

    /**
     * 输出流，流向串口
     */
    private FileOutputStream outputStream;

    /**
     * 服务执行者
     */
    private ExecutorService executorService = Executors.newFixedThreadPool(2, new CustomThreadFactory("12"));

    private Connector connector;

    private SerialPortReaderListener serialPortReaderListener;

    private static SerialPortClient serialPortClient;

    public static SerialPortClient getInstance() {
        if (serialPortClient == null) {
            synchronized (SerialPortClient.class) {
                if (serialPortClient == null) {
                    serialPortClient = new SerialPortClient();
                }
            }
        }
        return serialPortClient;
    }


    public void start(SerialPortStrategy serialPortStrategy,
                      SerialPortReaderListener serialPortReaderListener) {
        serialPortClient.inputStreamSizeInByte = serialPortStrategy.inputStreamSizeInByte;
        serialPortClient.serialPortPath = serialPortStrategy.serialPortPath;
        serialPortClient.serialPortReaderListener = serialPortReaderListener;
        serialPortClient.setListener(serialPortReaderListener);
    }

    /**
     * add serial port listener and try to open port
     *
     * @param serialPortReaderListener
     */
    private void setListener(SerialPortReaderListener serialPortReaderListener) {
        this.serialPortReaderListener = serialPortReaderListener;
        connect();
        Util.sleep(1000);
        startListenSerialPort();
    }

    /**
     * close serial port
     */
    public void close() {
        try {
            this.connector.close();
            this.executorService.shutdownNow();
        } catch (Exception e) {
            logger.error(new SerialException(e));
        }
    }

    public int writeData(byte[] data) {
        if (data.length <= 0) return -1;
        if (outputStream == null) {
            return -1;
        }
        try {
            outputStream.write(data);
            outputStream.flush();
            return data.length;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(new SerialException(e));
        }
        return -1;
    }

    public byte[] readData() {
        if (null == inputStream) {
            return null;
        }
        int len;
        byte[] buffer = new byte[64];
        try {
            len = inputStream.read(buffer);
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
            if (connector != null) {
                close();
            }
            connector = new Connector(serialPortPath, Connector.SERIAL_BAUD_RATE_4800, Connector.DATABITS_8, Connector.STOPBITS_1, Connector.PARITY_NONE);
            connector.open();

            inputStream = connector.getInputStream();
            outputStream = connector.getOutputStream();
            logger.info("setListener inputStream:" + inputStream + " outputStream:" + outputStream);

        } catch (SecurityException e) {
            logger.error(new SerialException(e));
        }
    }

    private void startListenSerialPort() {
        executorService.submit(new Runnable() {
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
                if (inputStream == null) {
                    continue;
                }
                byte[] buffer = new byte[inputStreamSizeInByte];
                int len = inputStream.read(buffer);
                if (len > 0) {
                    //logger.info("readDataFromSerialPort length is:" + len + ", data is:" + new String(buffer, 0, len));
                    //LogUtil.i("XhSerialPort", "收到串口数据: " + ByteUtil.toDisplayString(buffer));
                    serialPortReaderListener.onSucceed(buffer);
                }
            } catch (Exception e) {
                logger.error(new SerialException(e));
            }
        }
    }
}
