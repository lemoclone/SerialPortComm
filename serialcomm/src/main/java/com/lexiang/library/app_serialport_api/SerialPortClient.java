package com.lexiang.library.app_serialport_api;


import com.lexiang.library.SerialPortParams;
import com.lexiang.library.exception.SerialException;
import com.lexiang.library.log.Logger;
import com.lexiang.library.log.LoggerFactory;
import com.lexiang.library.utils.Util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Serial port client maintain for input and output
 */
public class SerialPortClient {

    private Logger logger = LoggerFactory.getLog(SerialPortClient.class);
    /**
     * read data from serial port
     */
    private FileInputStream inputStream;
    /**
     * write data into serial port
     */
    private FileOutputStream outputStream;
    /**
     * Connect to the physical serial port
     */
    private SerialPortConnector serialPortConnector;
    /**
     * Callback event after read data from serial port
     */
    private SerialPortReaderListener serialPortReaderListener;
    /**
     * instance of serial port for management of  connection
     */
    private static SerialPortClient serialPortClient;
    /**
     * wait for 500ms after serial port connect
     */
    private static final int TIME_FOR_WAIT_OPEN_IN_MILLIS = 500;
    /**
     * serial port reader debugFlag
     */
    private static volatile boolean isReaderThreadStopped = false;
    /**
     * serial port reader thread
     */
    private ReaderThread readerThread;

    /**
     * add serial port reader listener and try to connect the port
     *
     * @param serialPortStrategy       parameters of serial port
     * @param serialPortReaderListener callback after read data
     */
    public int start(SerialPortParams serialPortStrategy,
                     SerialPortReaderListener serialPortReaderListener) {
        this.serialPortReaderListener = serialPortReaderListener;
        int res = openSerialPort(serialPortStrategy);
        Util.sleep(TIME_FOR_WAIT_OPEN_IN_MILLIS);
        startListenSerialPort(serialPortStrategy);
        return res;
    }

    /**
     * write data to serial port
     *
     * @param data in byte[]
     * @return -1 if write failed, the length of data if write succeed
     */
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

    /**
     * open serial port
     *
     * @param strategy
     */
    private int openSerialPort(SerialPortParams strategy) {
        try {
            if (serialPortConnector != null) {
                close();
            }
            serialPortConnector = new SerialPortConnector(strategy.getSerialPortPath(),
                    strategy.getSerialPortBaudRate(),
                    strategy.getSerialPortDataBits(),
                    strategy.getSerialPortStopBits(),
                    strategy.getSerialPortParity());
            serialPortConnector.connect();
            inputStream = serialPortConnector.getFileInputStream();
            outputStream = serialPortConnector.getOutputStream();
            if (inputStream == null || outputStream == null) {
                return -1;
            }
            return 1;
        } catch (SecurityException e) {
            logger.error(new SerialException(e));
        }
        return -1;
    }

    /**
     * close serial port, should be called in the onDestroy() method
     */
    public void close() {
        try {
            if (serialPortClient == null) {
                return;
            }
            readerThread.interrupt();
            isReaderThreadStopped = true;
            serialPortConnector.close();
        } catch (Exception e) {
            logger.error(new SerialException(e));
        }
    }

    /**
     * keep reading the serial port
     */
    private void startListenSerialPort(final SerialPortParams strategy) {
        readerThread = new ReaderThread(strategy);
        readerThread.start();
    }

    /**
     * prevent init
     */
    public SerialPortClient() {
    }

    private class ReaderThread extends Thread {
        int bufferLength = 64;
        long durationInMillisSeconds = 10;

        public ReaderThread(SerialPortParams serialPortParams) {
            bufferLength = serialPortParams.getInputStreamSizeInByte();
            durationInMillisSeconds = serialPortParams.getSerialPortReaderIntervalTimeInMillis();
        }

        public void run() {
            while (true) {
                if (isReaderThreadStopped) {
                    logger.info("serial port reader quited");
                    return;
                }

                int len;
                try {
                    byte[] buffer = new byte[bufferLength];
                    if (inputStream == null) {
                        logger.info("serial port reader quited,cause: inputStream == null");
                        return;
                    }
                    len = inputStream.read(buffer);
                    if (len > 0) {
                        serialPortReaderListener.onDataChanged(buffer,len);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(durationInMillisSeconds);
                } catch (InterruptedException e) {
                    logger.info("serial port reader quited");
                    return;
                }
            }
        }
    }
}