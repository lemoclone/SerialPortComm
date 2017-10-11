package com.lexiang.library.queue;


public interface DataHandleListener {
    void onSucceed(String str);
    void onFailed(String str);
}
