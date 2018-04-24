package com.example.princ.inclass12;

public class Message {
    public String message_id,message, user_id, user_name, created_time;

    public Message(String message, String user_id, String user_name, String created_time) {
        this.message = message;
        this.user_id = user_id;
        this.user_name = user_name;
        this.created_time = created_time;
    }

    public Message() {
    }

    @Override
    public String toString() {
        return "Message{" +
                "message_id='" + message_id + '\'' +
                ", message='" + message + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", created_time='" + created_time + '\'' +
                '}';
    }
}
