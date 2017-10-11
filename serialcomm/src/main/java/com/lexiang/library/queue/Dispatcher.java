package com.lexiang.library.queue;


import android.util.Log;

import com.lexiang.library.log.Logger;
import com.lexiang.library.log.LoggerFactory;

import java.util.concurrent.BlockingQueue;

class Dispatcher extends Thread {
    private String tag = "";
    private final BlockingQueue<SerialPortData<?>> queue;
    private volatile boolean quit = false;
    private DataHandler dataHandler;

    private Logger logger = LoggerFactory.getLog(this.getClass());

    public Dispatcher(BlockingQueue<SerialPortData<?>> queue, DataHandler messageHandler, String tag) {
        this.queue = queue;
        dataHandler = messageHandler;
        this.tag = tag;
    }

    public void quit() {
        quit = true;
        interrupt();
    }

    @Override
    public void run() {
        log(tag + "开始运行");
        while (true) {
            if (quit) {
                log(tag + "结束运行");
                return;
            }
            SerialPortData<?> request = null;
            try {
                request = queue.take();
                dataHandler.handleData(request);
            } catch (InterruptedException e) {
                log(tag + "结束运行");
            } catch (TRMError vmcError) {
                log(tag + "出现异常,结束运行" + vmcError.toString());
            }
        }
    }

    private void log(String str) {
        Log.i(tag, str);
        logger.info(str);
    }
}
