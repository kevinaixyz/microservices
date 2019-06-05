package com.prototype.microservice.edge.exception;

public class WhitelistNotDefinedException extends Exception {

    private static final long serialVersionUID = 7988903515295870675L;

    public WhitelistNotDefinedException(String msg) {
        super(msg);
    }

}
