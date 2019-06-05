package com.prototype.microservice.commons.message;

/**
 * JMS message POJO for async report events.
 */
public class AsyncReportMessage {

    private final Long reportBatchID;
    private final String reportBatchStatus;
    private final String message;
    private final String messageTimestamp;

    public AsyncReportMessage(Long reportBatchID, String reportBatchStatus, String message, String messageTimestamp) {
        this.reportBatchID = reportBatchID;
        this.reportBatchStatus = reportBatchStatus;
        this.message = message;
        this.messageTimestamp = messageTimestamp;
    }

    public Long getReportBatchID() {
        return reportBatchID;
    }

    public String getReportBatchStatus() {
        return reportBatchStatus;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageTimestamp() {
        return messageTimestamp;
    }

    @Override
    public String toString() {
        return "AsyncReportMessage [reportBatchID=" + reportBatchID + ", reportBatchStatus=" + reportBatchStatus
                + ", message=" + message + ", messageTimestamp=" + messageTimestamp + "]";
    }

}
