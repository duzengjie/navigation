package com.duzj.navigation.exceptions;

import lombok.Data;

@Data
public class SystemUserException extends RuntimeException {

    private String code;
    private String msg;

    public SystemUserException() {
        super();
    }


    public SystemUserException(String message) {
        super(message);
    }

    public SystemUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemUserException(Throwable cause) {
        super(cause);
    }

    protected SystemUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
