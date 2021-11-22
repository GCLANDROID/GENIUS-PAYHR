package com.genius.hrms.activity.chat.data;

public class Chats {
    private String receiver;
    private String sender;
    private String message;
    private String timestamp;
    private boolean seen;

    public Chats(String receiverId, String senderId, String message, String timestamp, boolean seen) {
        this.receiver = receiverId;
        this.sender = senderId;
        this.message = message;
        this.timestamp = timestamp;
        this.seen = seen;
    }

    public Chats() {

    }


    public boolean getSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen= seen;
    }


    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
