package com.lexiang.library.app_serialport_api;


import com.lexiang.library.exception.SerialException;
import com.lexiang.library.log.Logger;
import com.lexiang.library.log.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android_serialport_api.SerialPort;

public class Connector {

    private Logger log = LoggerFactory.getLog(this.getClass());

    /**
     * 串口波特率：4800,9600,14400...
     */
    public static final int SERIAL_BAUD_RATE_4800 = 4800;
    public static final int SERIAL_BAUD_RATE_9600 = 9600;
    public static final int SERIAL_BAUD_RATE_14400 = 14400;
    public static final int SERIAL_BAUD_RATE_19200 = 19200;
    public static final int SERIAL_BAUD_RATE_38400 = 38400;
    public static final int SERIAL_BAUD_RATE_56000 = 56000;
    public static final int SERIAL_BAUD_RATE_57600 = 57600;
    public static final int SERIAL_BAUD_RATE_115200 = 115200;
    public static final int SERIAL_BAUD_RATE_128000 = 128000;
    public static final int SERIAL_BAUD_RATE_256000 = 256000;

    public static final int DATABITS_8 = 8;
    public static final int DATABITS_7 = 7;
    public static final int STOPBITS_2 = 2;
    public static final int STOPBITS_1 = 1;
    public static final int PARITY_NONE = 'n';
    public static final int PARITY_ODD = 'o';
    public static final int PARITY_EVEN = 'e';

    private SerialPort serialPort;
    private FileInputStream in;
    private FileOutputStream out;


    // 串口设置
    private final String COM;
    private final int baudRate;
    private final int dataBits;
    private final int stopBits;
    private final int parity;


    public Connector(String com, int baudRate, int dataBits, int stopBits, int parity) {
        this.COM = com;
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
    }

    public void open() {
        try {
            log.info("serialPort open com:" + COM + " baudRate:" + baudRate + " dataBits:" + dataBits + " stopBits:" + stopBits + " parity:" + parity);
            serialPort = new SerialPort(new File(COM), baudRate, 0, dataBits, stopBits, parity);
            in = serialPort.getInputStream();
            out = serialPort.getOutputStream();
            log.info("serialPort open inputsrtream:" + in + " outputStream:" + out);
        } catch (SecurityException e) {
            log.error(new SerialException(e));
        } catch (IOException e) {
            log.error(new SerialException(e));
        }
    }

    public void close() {
        try {
            if (out != null) {
                out.close();
                out = null;
            }
            if (in != null) {
                in.close();
                in = null;
            }
            if (serialPort != null) {
                serialPort.close();
                serialPort = null;
            }
        } catch (IOException e) {
            log.error(new SerialException(e));
        } catch (Throwable e) {
            log.error(new SerialException(e));
        }
    }

    public FileInputStream getInputStream() {
        return in;
    }

    public FileOutputStream getOutputStream() {
        return out;
    }
}
