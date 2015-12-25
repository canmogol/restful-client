package com.fererlab.user.exception;

import com.fererlab.core.exception.StacklessException;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserException extends StacklessException {

    private static final long serialVersionUID = -5785178524623443924L;

    public UserException() {
    }

    public UserException(String message) {
        super(message);
    }
}
