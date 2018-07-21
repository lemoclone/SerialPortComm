package com.lexiang.library.app_serialport_api;


import com.lexiang.library.SerialPortParams;
import com.lexiang.library.utils.ByteSerialPortReaderListener;
import com.lexiang.library.utils.Util;
import com.lexiang.library.exception.SerialException;
import com.lexiang.library.log.Logger;
import com.lexiang.library.log.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

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
     * Execute the read event of serial port
     */
    private ExecutorService serialPortReaderExecutorService = Executors.newFixedThreadPool(2, new SerialPortReaderThreadFactory());
    /**
     * instance of serial port for management of  connection
     */
    private static SerialPortClient serialPortClient;
    /**
     * wait for 500ms after serial port connect
     */
    private static final int TIME_FOR_WAIT_OPEN_IN_MILLIS = 500;

    /**
     * get the single instance
     *
     * @return SerialPortClient instance
     */
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

    /**
     * add serial port reader listener and try to connect the port
     *
     * @param serialPortStrategy parameters of serial port
     */
    public void start(SerialPortParams serialPortStrategy) {
        this.start(serialPortStrategy, new ByteSerialPortReaderListener());
    }

    /**
     * add serial port reader listener and try to connect the port
     *
     * @param serialPortStrategy       parameters of serial port
     * @param serialPortReaderListener callback after read data
     */
    public void start(SerialPortParams serialPortStrategy,
                      SerialPortReaderListener serialPortReaderListener) {
        this.serialPortReaderListener = serialPortReaderListener;
        openSerialPort(serialPortStrategy);
        Util.sleep(TIME_FOR_WAIT_OPEN_IN_MILLIS);
        startListenSerialPort(serialPortStrategy);
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
    private void openSerialPort(SerialPortParams strategy) {
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
        } catch (SecurityException e) {
            logger.error(new SerialException(e));
        }
    }

    /**
     * close serial port
     */
    public void close() {
        try {
            if (serialPortClient == null) {
                return;
            }
            serialPortConnector.close();
            serialPortReaderExecutorService.shutdownNow();
        } catch (Exception e) {
            logger.error(new SerialException(e));
        }
    }

    /**
     * keep reading the serial port
     */
    private void startListenSerialPort(final SerialPortParams strategy) {
        serialPortReaderExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                readDataFromSerialPort(strategy);
            }
        });
    }

    /**
     * read data from serial port
     *
     * @param strategy
     */
    private void readDataFromSerialPort(SerialPortParams strategy) {
        while (true) {
            try {
                Util.sleep(strategy.getSerialPortReaderIntervalTimeInMillis());
                if (inputStream == null) {
                    continue;
                }
                byte[] buffer = new byte[strategy.getInputStreamSizeInByte()];
                int len = inputStream.read(buffer);
                if (len > 0) {
                    serialPortReaderListener.onDataChanged(buffer);
                }
            } catch (Exception e) {
                logger.error(new SerialException(e));
            }
        }
    }

    /**
     * prevent init
     */
    private SerialPortClient() {
    }

    private static class SerialPortReaderThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public SerialPortReaderThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "SerialPortReader-pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix
                    + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
