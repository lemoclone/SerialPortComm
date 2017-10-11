package com.lexiang.serialportcomm;

import android.util.Log;

import com.lexiang.library.log.Logger;
import com.lexiang.library.log.LoggerFactory;
import com.lexiang.library.Util.ByteSerialPortData;
import com.lexiang.library.queue.DataHandleListener;
import com.lexiang.library.queue.SerialPortWriter;

class OutComeSignalDaemon extends Thread {
    private Logger logger = LoggerFactory.getLog(this.getClass());
    private static final String TAG = OutComeSignalDaemon.class.getSimpleName();
    private volatile boolean mQuit = false;
    private volatile boolean mPause = false;

    @Override
    public void run() {
        logInfo("<---跑步机消息轮询发送开始--->");

        while (true) {
            if (mQuit) {
                logInfo("<---跑步机消息轮询发送结束--->");
                return;
            }
            try {
                while (true) {
                    if (mQuit) {
                        logInfo("<---跑步机消息轮询发送结束--->");
                        return;
                    }
                    if (mPause) {
                        Thread.sleep(200);
                    } else {
                        break;
                    }
                }
                Thread.sleep(20000);

                byte[] bytes = new byte[10];

                bytes[0] = (byte) 0xfc;
                bytes[1] = (byte) 0x01;
                bytes[2] = (byte) 0x00;
                bytes[3] = (byte) 0x00;
                bytes[4] = (byte) 0x00;
                bytes[5] = (byte) 0x00;
                bytes[6] = (byte) 0x00;
                bytes[7] = (byte) 0x00;
                bytes[8] = (byte) 0x01;
                bytes[9] = (byte) 0xfd;

                ByteSerialPortData byteSerialPortData = new ByteSerialPortData(bytes, new DataHandleListener() {
                    @Override
                    public void onSucceed(String str) {
                        Log.i(TAG,"onSucceed: " + str);
                    }

                    @Override
                    public void onFailed(String str) {
                        Log.i(TAG,"onFailed: " + str);
                    }
                });

                SerialPortWriter.addWriteRequest(byteSerialPortData);
            } catch (InterruptedException e) {
                logInfo("<---跑步机消息轮询发送结束--->");
                return;
            }
        }
    }

    public void pauseSend() {
        mPause = true;
        logInfo("<---智能跑步暂停--->");
    }

    public void resumeSend() {
        logInfo("<---智能跑步恢复--->");
        mPause = false;
    }

    public void quit() {
        mQuit = true;
        interrupt();
    }

    private void logInfo(String str) {
        Log.i(TAG, str);
        logger.info(str);
    }
}
