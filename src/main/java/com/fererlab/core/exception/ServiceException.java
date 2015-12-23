package com.fererlab.core.exception;

import javax.ws.rs.InternalServerErrorException;

public class ServiceException extends InternalServerErrorException {

    private static final long serialVersionUID = 7222943416265668449L;

    private String message = "";
    private ErrorCode errorCode = ErrorCode.SERVER;

    public ServiceException(Exception e) {
        if (e instanceof InternalServerErrorException) {
            InternalServerErrorException internalServerErrorException = (InternalServerErrorException) e;
            this.message = internalServerErrorException.getResponse().getHeaderString("ErrorMessage");
            this.errorCode = ErrorCode.valueOf(internalServerErrorException.getResponse().getHeaderString("ErrorCode"));
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
