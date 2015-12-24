package com.fererlab.user.exception;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserDatabaseException extends Exception {

    private static final long serialVersionUID = 8128020310772314218L;

    public UserDatabaseException() {
    }

    public UserDatabaseException(String message) {
        super(message);
    }
}
