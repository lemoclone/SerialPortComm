package com.lexiang.serialportcomm;

public class Signal {
    // 1 byte
    protected int command;
    // 1 byte 数据包序号，从0~255递增
    protected int serialNumber;

    public Signal(int command, int serialNumber) {
        this.command = command;
        this.serialNumber = serialNumber;
    }

    public Signal() {

    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public byte[] toBytes(){
        return new byte[1];
    }
}