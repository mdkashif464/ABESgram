package com.example.kashif.abesgram.CommentsActivityFiles;

import java.util.Date;

/**
 * Created by kashif on 14/4/18.
 */

public class Comments {

    private String message, user_id;
    private Date timestamp;

    public Comments(){

    }

    public Comments(String message, String user_id, Date timestamp) {
        this.message = message;
        this.user_id = user_id;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
