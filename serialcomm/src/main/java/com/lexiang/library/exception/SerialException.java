package com.lexiang.library.exception;

public class SerialException extends RuntimeException{

    public SerialException() {
        super();
    }

    public SerialException(String message) {
        super(message);
    }

    public SerialException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerialException(Throwable cause) {
        super(cause);
    }

    public static SerialException wrap(String message) {
        return new SerialException(message);
    }

    public static SerialException wrap(String message, Throwable cause) {
        return new SerialException(message, cause);
    }

    public static SerialException wrap(Throwable cause) {
        if (cause == null)
            throw wrap(new NullPointerException());

        return (SerialException.class.isInstance(cause)) ?
                SerialException.class.cast(cause)
                : new SerialException(cause);

    }

}
