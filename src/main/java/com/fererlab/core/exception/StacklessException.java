package com.fererlab.core.exception;

public class StacklessException extends Exception {

    private static final long serialVersionUID = -1279610549694153953L;

    public StacklessException() {
    }

    public StacklessException(String message) {
        super(message);
    }

    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

}
