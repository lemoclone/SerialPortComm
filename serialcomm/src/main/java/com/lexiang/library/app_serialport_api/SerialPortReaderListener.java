package com.lexiang.library.app_serialport_api;


public interface SerialPortReaderListener {
    void onFailed();

    void onSucceed(byte[] data);
}