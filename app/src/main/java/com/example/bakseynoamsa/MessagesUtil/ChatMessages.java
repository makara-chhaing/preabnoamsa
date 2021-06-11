package com.example.bakseynoamsa.MessagesUtil;

public class ChatMessages {
    public ChatMessages() {
        this.id = "";
        this.text = "";
        this.fromId = "";
        this.toId = "";
        this.timeStamp = -1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    private String id;
    private String text;
    private String fromId;
    private String toId;
    private long timeStamp;

    public ChatMessages(String id, String text, String fromId, String toId, long timeStamp) {
        this.id = id;
        this.text = text;
        this.fromId = fromId;
        this.toId = toId;
        this.timeStamp = timeStamp;
    }
}
