package com.eldhose.noteapp;

import com.google.firebase.Timestamp;

public class notes {
    private String text;
    private Timestamp dateCreated;
    private Boolean completed;
    private String userId;


    public notes(String text, Timestamp dateCreated, Boolean completed, String userId) {
        this.text = text;
        this.dateCreated = dateCreated;
        this.completed = completed;
        this.userId = userId;
    }

    public notes() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "notes{" +
                "text='" + text + '\'' +
                ", dateCreated=" + dateCreated +
                ", completed=" + completed +
                ", userId='" + userId + '\'' +
                '}';
    }
}


