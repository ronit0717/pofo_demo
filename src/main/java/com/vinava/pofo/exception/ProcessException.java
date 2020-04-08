package com.vinava.pofo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ProcessException extends RuntimeException {
    private String processName;
    private String failureReason;

    public ProcessException(String processName, String failureReason) {
        super(String.format("Process %s failed with reason: %s", processName, failureReason));
        this.processName = processName;
        this.failureReason = failureReason;
    }

    public String getProcessName() {
        return this.processName;
    }

    public String getFailureReason() {
        return this.failureReason;
    }

}
