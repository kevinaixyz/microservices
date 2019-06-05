package com.prototype.microservice.commons.json;

public class ResponseJson extends BaseJson {

    private static final long serialVersionUID = -2359220278185028742L;
    public final static String JSON_RESP_CODE_OK = "OK";
    public final static String JSON_RESP_CODE_ERROR = "ERROR";

    private String respCode;
    private String respSubCode;
    private String respMsg;
    private String respTimestamp;

    public ResponseJson() {
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespSubCode() {
        return respSubCode;
    }

    public void setRespSubCode(String respSubCode) {
        this.respSubCode = respSubCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public String getRespTimestamp() {
        return respTimestamp;
    }

    public void setRespTimestamp(String respTimestamp) {
        this.respTimestamp = respTimestamp;
    }

}
