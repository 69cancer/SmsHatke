package com.buyhatke.smshatke.models;

/**
 * Created by ssaxena on 7/29/16.
 */
public class Message {

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Message() {
    }

    private String body = "";
    private String date = "";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type = "";

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String address = "";


}
