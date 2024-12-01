package com.example.chatapp.repository.models;

import android.text.format.DateUtils;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.util.Date;

public class User {
    String id;
    String username;
    String profilePhotoUrl;
    String description;
    Timestamp createdTimestamp;
    String email;
    Timestamp lastActive;
    String fcmToken;

    public User() {
    }

    public User(String username, String description, String email) {
        this.username = username;
        this.description = description;
        this.createdTimestamp = new Timestamp(new Date());
        this.email = email;
        this.lastActive = new Timestamp(new Date());;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    // TODO: Decide whether this function stays or not
    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getLastActive() {
        return lastActive;
    }

    public void setLastActive(Timestamp lastActive) {
        this.lastActive = lastActive;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    @Exclude
    public String getLastActiveTimeString(){

        return DateUtils.getRelativeTimeSpanString(
                lastActive.getSeconds()*1000
        ).toString();
    }

    @Exclude
    public String getDateCreatedTimeString(){

        return DateUtils.getRelativeTimeSpanString(
                createdTimestamp.getSeconds()*1000
        ).toString();
    }

}
