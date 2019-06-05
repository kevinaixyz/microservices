package com.prototype.microservice.edge.constant;

public final class EdgeServerAppConstant {

    private EdgeServerAppConstant() {
    }

    public final static String JSON_RESP_CODE_OK = "OK";
    public final static String JSON_RESP_CODE_ERROR = "ERROR";

    public final static String TIMESTAMP_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZZZ";

    public final static String ZUUL_FILTER_TYPE_PRE = "pre";
    public final static String ZUUL_FILTER_TYPE_ROUTING = "routing";
    public final static String ZUUL_FILTER_TYPE_POST = "post";

}
