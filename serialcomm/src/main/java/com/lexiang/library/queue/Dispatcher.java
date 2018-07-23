package com.lexiang.library.queue;


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
        log(tag + "start run");
        while (true) {
            if (quit) {
                log(tag + "stop run");
                return;
            }
            SerialPortData<?> request = null;
            try {
                request = queue.take();
                dataHandler.handleData(request);
            } catch (InterruptedException e) {
                log(tag + "stop run");
            } catch (Exception e) {

            }
        }
    }

    private void log(String str) {
        logger.info(str);
    }
}
