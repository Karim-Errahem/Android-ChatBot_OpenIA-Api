package com.example.bot;

public class Message {
    public static String send_by_me ="me";
    public static String send_by_bot = "bot";

    public String getSendby() {
        return sendby;
    }

    public void setSendby(String sendby) {
        this.sendby = sendby;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String message;

    public Message(String message, String sendby) {
        this.message = message;
        this.sendby = sendby;
    }

    String sendby;
}
