package com.buyhatke.smshatke.models;

/**
 * Created by ssaxena on 7/31/16.
 */
public class SearchData {

    protected String threadId;

    public SearchData() {
    }


    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    protected String body;
    protected String date;
    protected String rowId;
    protected String address;


}
