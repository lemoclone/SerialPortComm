package com.lexiang.library.app_serialport_api;

/**
 * listener used to callback from serial port reader
 */
public interface SerialPortReaderListener {
    /**
     * event when read data from serial port
     * @param data byte[] data
     * @param realSize valid data size
     */
    void onDataChanged(byte[] data,int realSize);
}