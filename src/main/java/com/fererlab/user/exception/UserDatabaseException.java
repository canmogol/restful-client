package com.fererlab.user.exception;


import com.fererlab.core.exception.StacklessException;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserDatabaseException extends StacklessException {

    private static final long serialVersionUID = 8128020310772314218L;

    public UserDatabaseException() {
    }

    public UserDatabaseException(String message) {
        super(message);
    }

}
