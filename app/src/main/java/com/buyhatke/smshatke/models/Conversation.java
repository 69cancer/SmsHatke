package com.buyhatke.smshatke.models;

/**
 * Created by ssaxena on 7/29/16.
 */
public class Conversation {
    private String thread_id = "";
    private String snippet = "";
    private String message_count = "";
    private String address = "";

    public Conversation() {
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getMessage_count() {
        return message_count;
    }

    public void setMessage_count(String message_count) {
        this.message_count = message_count;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
