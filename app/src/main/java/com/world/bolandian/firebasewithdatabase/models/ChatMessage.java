package com.world.bolandian.firebasewithdatabase.models;

import org.joda.time.LocalDateTime;

/**
 * Created by Bolandian on 20/06/2017.
 */

public class ChatMessage {
    private String message;
    private String profileImage;
    private String sender;
    private String time;

    public ChatMessage(){
    }

    public ChatMessage(User user, String message) {
        this.message = message;
        this.profileImage = user.getProfileImage();
        this.sender = user.getDisplayName();
        this.time = LocalDateTime.now().toString();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "message='" + message + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", sender='" + sender + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
