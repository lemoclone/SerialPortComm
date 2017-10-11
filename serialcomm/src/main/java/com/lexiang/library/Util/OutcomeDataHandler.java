package com.lexiang.library.Util;

import com.lexiang.library.Util.ByteUtil;
import com.lexiang.library.app_serialport_api.SerialPortClient;
import com.lexiang.library.queue.DataHandler;
import com.lexiang.library.queue.SerialPortData;
import com.lexiang.library.queue.TRMError;

class OutcomeDataHandler implements DataHandler {

    @Override
    public void handleData(SerialPortData serialPortData) throws TRMError {
        /*if (SerialPortClient.getInstance().writeData((byte[]) serialPortData.data) > -1) {
            if (serialPortData.getDataHandleListener() != null) {
                serialPortData.getDataHandleListener().onSucceed(ByteUtil.toDisplayString((byte[]) serialPortData.getData()));
            }
        } else {
            if (serialPortData.getDataHandleListener() != null) {
                serialPortData.getDataHandleListener().onFailed(ByteUtil.toDisplayString((byte[]) serialPortData.getData()));
            }
        }*/
    }
}
