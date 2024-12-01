package com.example.chatapp.repository.models;

import android.text.format.DateUtils;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

public class Group {
    String id;
    String chatRoomId;
    String groupName;
    String groupPhotoUrl;
    String description;
    Timestamp creationTimestamp;


    public Group() {
    }

    public Group(String id, String chatRoomId,String groupName, String description) {
        this.id = id;
        this.chatRoomId = id;
        this.groupName = groupName;
        this.description = description;
        this.creationTimestamp = Timestamp.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupPhotoUrl() {
        return groupPhotoUrl;
    }

    public void setGroupPhotoUrl(String groupPhotoUrl) {
        this.groupPhotoUrl = groupPhotoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    @Exclude
    public String getDateCreatedTimestamp(){

        return DateUtils.getRelativeTimeSpanString(
                creationTimestamp.getSeconds()*1000
        ).toString();
    }

    @Exclude
    public String convertTime(){
        if (creationTimestamp == null) return null;

        return DateUtils.getRelativeTimeSpanString(
                creationTimestamp.getSeconds()*1000
        ).toString();
    }

}
