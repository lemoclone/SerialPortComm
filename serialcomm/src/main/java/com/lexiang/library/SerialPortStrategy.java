package com.lexiang.library;

import com.lexiang.library.app_serialport_api.Constants;

/**
 * Created by hudafei on 11/10/2017.
 */

public class SerialPortStrategy {
    //Input stream byte size
    private int inputStreamSizeInByte = 16;
    //Serial port path
    private String serialPortPath = "/dev/ttyS2";
    //Serial port baud rate
    private int serialPortBaudRate = Constants.SERIAL_BAUD_RATE_9600;
    //Serial port data bits
    private int serialPortDataBits = Constants.DATABITS_8;
    //Serial port stop bits
    private int serialPortStopBits = Constants.STOPBITS_1;
    //Serial port parity
    private int serialPortParity = Constants.PARITY_NONE;
    //Serial port reader time interval,i.e. read the port in every 100ms
    private int serialPortReaderIntervalTimeInMillis = 100;

    public int getInputStreamSizeInByte() {
        return inputStreamSizeInByte;
    }

    public void setInputStreamSizeInByte(int inputStreamSizeInByte) {
        this.inputStreamSizeInByte = inputStreamSizeInByte;
    }

    public String getSerialPortPath() {
        return serialPortPath;
    }

    public void setSerialPortPath(String serialPortPath) {
        this.serialPortPath = serialPortPath;
    }

    public int getSerialPortDataBits() {
        return serialPortDataBits;
    }

    public void setSerialPortDataBits(int serialPortDataBits) {
        this.serialPortDataBits = serialPortDataBits;
    }

    public int getSerialPortStopBits() {
        return serialPortStopBits;
    }

    public void setSerialPortStopBits(int serialPortStopBits) {
        this.serialPortStopBits = serialPortStopBits;
    }

    public int getSerialPortParity() {
        return serialPortParity;
    }

    public void setSerialPortParity(int serialPortParity) {
        this.serialPortParity = serialPortParity;
    }

    public int getSerialPortBaudRate() {
        return serialPortBaudRate;
    }

    public void setSerialPortBaudRate(int serialPortBaudRate) {
        this.serialPortBaudRate = serialPortBaudRate;
    }

    public int getSerialPortReaderIntervalTimeInMillis() {
        return serialPortReaderIntervalTimeInMillis;
    }

    public void setSerialPortReaderIntervalTimeInMillis(int serialPortReaderIntervalTimeInMillis) {
        this.serialPortReaderIntervalTimeInMillis = serialPortReaderIntervalTimeInMillis;
    }
}

