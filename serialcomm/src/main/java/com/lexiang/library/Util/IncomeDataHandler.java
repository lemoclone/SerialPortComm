package com.lexiang.library.Util;


import android.util.Log;

import com.lexiang.library.Util.ByteUtil;
import com.lexiang.library.queue.DataHandler;
import com.lexiang.library.queue.SerialPortData;
import com.lexiang.library.queue.TRMError;

public class IncomeDataHandler implements DataHandler {

    @Override
    public void handleData(SerialPortData serialPortData) throws TRMError {
       /* if (serialPortData.getData() != null) {
            Log.i("IncomeDataHandler", ByteUtil.toDisplayString((byte[]) serialPortData.getData()));
        }*/
    }
}
