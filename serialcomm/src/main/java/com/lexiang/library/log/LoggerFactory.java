package com.lexiang.library.log;

public class LoggerFactory {
    private static Logger log = new Logger();
    public static Logger getLog(Class tagClass){
        return log;
    }
}
