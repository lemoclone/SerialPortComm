package com.lexiang.library.app_serialport_api;


public interface SerialPortListener {
    void onFailed();

    void onSucceed(byte[] data);
}