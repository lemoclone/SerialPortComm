package com.lexiang.library.log;


import android.util.Log;

import com.lexiang.library.utils.Util;
import com.lexiang.library.exception.SerialException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Logger {

    private static final int capacity_size = 100;
    private static ArrayBlockingQueue<String> abq = new ArrayBlockingQueue<String>(capacity_size, true);
    private static LogcatThread logcatThread = null;
    private static final int TIMEOUT = 200;

    private String logDir = "/mnt/sdcard/serial_port_log/";
    private String infoDir = logDir + "info/";
    private String errorDir = logDir + "error/";

    private static final String WARN = "warn | ";
    private static final String ERROR = "error | ";
    private static final String INFO = "info | ";
    private static final String separator = "_SEPARATOR_";

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss:SSS");

    public static boolean flag = true;

    protected Logger() {
    }

    public void info(String str) {
        if(!flag){
            return;
        }
        putMessage(infoDir + separator + infoStr(str));
    }

    public void error(SerialException ex) {
        StringBuffer sb = new StringBuffer();
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();
        String result = writer.toString();
        sb.append(result);
        putMessage(errorDir + separator + errorStr(String.valueOf(sb)));
    }

    public void warn(String str) {
        putMessage(infoDir + separator + warnStr(str));
    }

    private String infoStr(String msg) {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        StringBuffer toStringBuffer = new StringBuffer("[").append(INFO).append(
                traceElement.getFileName()).append(" | ").append(
                traceElement.getLineNumber()).append(" | ").append(
                traceElement.getMethodName()).append("()").append("]  ").append(msg);
        return String.valueOf(toStringBuffer);
    }

    private String warnStr(String msg) {

        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        StringBuffer toStringBuffer = new StringBuffer("[").append(WARN).append(
                traceElement.getFileName()).append(" | ").append(
                traceElement.getLineNumber()).append(" | ").append(
                traceElement.getMethodName()).append("()").append("] ").append(msg);

        return String.valueOf(toStringBuffer);

    }

    private String errorStr(String msg) {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        StringBuffer toStringBuffer = new StringBuffer("[").append(ERROR).append(
                traceElement.getFileName()).append(" | ").append(
                traceElement.getLineNumber()).append(" | ").append(
                traceElement.getMethodName()).append("()").append("]  ").append(msg);

        return String.valueOf(toStringBuffer);
    }


    private void log(String path, String info) {
        Date currentTime = new Date();
        String currentDate = dateFormat.format(currentTime);
        String time = timeFormat.format(currentTime);
        path = path + currentDate + ".log";
        info = time + " " + info;
        Util.writeFile(path, info);
        Log.i(Thread.currentThread().toString(),info);
    }

    private void putMessage(String logInfo) {
        try {
            abq.offer(logInfo, TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (logcatThread == null || !logcatThread.isAlive()) {
            startLogcat();
        }
    }

    private synchronized void startLogcat() {
        if (logcatThread == null || !logcatThread.isAlive()) {
            if (logcatThread != null) {
                logcatThread.interrupt();
            }
            logcatThread = new LogcatThread();
            logcatThread.start();
        }
    }

    class LogcatThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    String logInfo = abq.poll(600, TimeUnit.MILLISECONDS);
                    if (logInfo != null) {
                        String dir = logInfo.split(separator)[0];
                        String log = logInfo.split(separator)[1];
                        log(dir, log);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }


}
