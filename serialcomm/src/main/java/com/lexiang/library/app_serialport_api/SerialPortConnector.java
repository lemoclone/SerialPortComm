package com.lexiang.library.app_serialport_api;


import com.lexiang.library.exception.SerialException;
import com.lexiang.library.log.Logger;
import com.lexiang.library.log.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android_serialport_api.SerialPort;

class SerialPortConnector {

    private Logger logger = LoggerFactory.getLog(this.getClass());

    private SerialPort serialPort;
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;

    private final String serialPortPath;
    private final int baudRate;
    private final int dataBits;
    private final int stopBits;
    private final int parity;

    public SerialPortConnector(String serialPortPath, int baudRate, int dataBits, int stopBits, int parity) {
        this.serialPortPath = serialPortPath;
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
    }

    /**
     * open the port
     */
    public void connect() {
        try {
            logger.info("serial port connector info: " + this.toString());
            serialPort = new SerialPort(new File(serialPortPath), baudRate, 0, dataBits, stopBits, parity);
            fileInputStream = serialPort.getInputStream();
            fileOutputStream = serialPort.getOutputStream();
            logger.info("serial port connector inputstream:" + fileInputStream + " outputStream:" + fileOutputStream);
        } catch (SecurityException e) {
            logger.error(new SerialException(e));
        } catch (IOException e) {
            logger.error(new SerialException(e));
        }
    }

    public void close() {
        try {
            if (fileOutputStream != null) {
                fileOutputStream.close();
                fileOutputStream = null;
            }
            if (fileInputStream != null) {
                fileInputStream.close();
                fileInputStream = null;
            }
            if (serialPort != null) {
                serialPort.close();
                serialPort = null;
            }
        } catch (IOException e) {
            logger.error(new SerialException(e));
        } catch (Throwable e) {
            logger.error(new SerialException(e));
        }
    }

    public FileInputStream getFileInputStream() {
        return fileInputStream;
    }

    public FileOutputStream getOutputStream() {
        return fileOutputStream;
    }

    @Override
    public String toString() {
        return "SerialPortConnector{" +
                "serialPortPath='" + serialPortPath + '\'' +
                ", baudRate=" + baudRate +
                ", dataBits=" + dataBits +
                ", stopBits=" + stopBits +
                ", parity=" + parity +
                '}';
    }
}
