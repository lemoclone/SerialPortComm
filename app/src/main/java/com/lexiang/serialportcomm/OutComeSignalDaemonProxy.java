package com.lexiang.serialportcomm;

public class OutComeSignalDaemonProxy {
    private static volatile OutComeSignalDaemonProxy defaultInstance;
    private static OutComeSignalDaemon outComeSignalDaemon;

    private OutComeSignalDaemonProxy() {
    }

    public static OutComeSignalDaemonProxy getDefault() {
        if (defaultInstance == null) {
            synchronized (OutComeSignalDaemonProxy.class) {
                if (defaultInstance == null) {
                    defaultInstance = new OutComeSignalDaemonProxy();
                    outComeSignalDaemon = new OutComeSignalDaemon();
                }
            }
        }
        return defaultInstance;
    }

    public static void startOrResume() {
        if (defaultInstance == null) {
            getDefault().start();
        } else {
            getDefault().resume();
        }
    }

    private void start() {
        if (outComeSignalDaemon != null && !outComeSignalDaemon.isAlive()) {
            outComeSignalDaemon.start();
        } else {
            outComeSignalDaemon = new OutComeSignalDaemon();
            outComeSignalDaemon.start();
        }
    }

    public void quit() {
        if (outComeSignalDaemon != null) {
            outComeSignalDaemon.quit();
        }
    }

    public void pause() {
        if (outComeSignalDaemon != null) {
            outComeSignalDaemon.pauseSend();
        }
    }

    public void resume() {
        if (outComeSignalDaemon != null) {
            outComeSignalDaemon.resumeSend();
        }
    }
}
