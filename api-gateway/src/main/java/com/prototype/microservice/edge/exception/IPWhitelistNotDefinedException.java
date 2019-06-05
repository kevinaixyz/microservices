package com.prototype.microservice.edge.exception;

public class IPWhitelistNotDefinedException extends Exception {

    private static final long serialVersionUID = 1211817069149647540L;

    public IPWhitelistNotDefinedException(String msg) {
        super(msg);
    }

}
