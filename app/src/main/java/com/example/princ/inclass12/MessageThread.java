package com.example.princ.inclass12;

import java.io.Serializable;

public class MessageThread implements Serializable{
    public String title;
    public String user_id;
    public String user_name;
    public String thread_id;

    public MessageThread(String title, String user_id,String user_name) {
        this.title = title;
        this.user_id = user_id;
        this.user_name=user_name;
    }

    public MessageThread() {
    }

    @Override
    public String toString() {
        return "MessageThread{" +
                "title='" + title + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", thread_id='" + thread_id + '\'' +
                '}';
    }
}
