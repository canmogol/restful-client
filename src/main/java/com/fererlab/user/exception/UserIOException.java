package com.fererlab.user.exception;

import com.fererlab.core.exception.StacklessException;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserIOException extends StacklessException {

    private static final long serialVersionUID = -1937408154210601055L;

    public UserIOException() {
    }

    public UserIOException(String message) {
        super(message);
    }
}
