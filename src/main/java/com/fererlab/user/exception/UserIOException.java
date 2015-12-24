package com.fererlab.user.exception;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserIOException extends Exception {

    private static final long serialVersionUID = -1937408154210601055L;

    public UserIOException() {
    }

    public UserIOException(String message) {
        super(message);
    }
}
