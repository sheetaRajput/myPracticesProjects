package com.leavemanagement.error;

public class LeaveNotFoundException extends Exception{

    public LeaveNotFoundException() {
        super();
    }

    public LeaveNotFoundException(String message) {
        super(message);
    }

    public LeaveNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public LeaveNotFoundException(Throwable cause) {
        super(cause);
    }

    protected LeaveNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}