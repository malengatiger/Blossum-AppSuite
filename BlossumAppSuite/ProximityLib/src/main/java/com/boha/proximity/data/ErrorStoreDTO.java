package com.boha.proximity.data;

import java.io.Serializable;

/**
 * Created by aubreyM on 2014/07/30.
 */
public class ErrorStoreDTO implements Serializable {
    private Integer errorStoreID;
    private int statusCode;
    private String message, origin;
    private long dateOccured;

    public Integer getErrorStoreID() {
        return errorStoreID;
    }

    public void setErrorStoreID(Integer errorStoreID) {
        this.errorStoreID = errorStoreID;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public long getDateOccured() {
        return dateOccured;
    }

    public void setDateOccured(long dateOccured) {
        this.dateOccured = dateOccured;
    }


}
