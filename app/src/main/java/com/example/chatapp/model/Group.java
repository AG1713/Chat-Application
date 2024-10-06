package com.example.chatapp.model;

import android.text.format.DateUtils;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

public class Group {
    String id;
    String chatRoomId;
    String groupName;
    String description;
    Timestamp creationTimestamp;


    public Group() {
    }

    public Group(String id, String groupName, String description, Timestamp creationTimestamp) {
        this.id = id;
        this.chatRoomId = id;
        this.groupName = groupName;
        this.description = description;
        this.creationTimestamp = creationTimestamp;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreationTimestamp(Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    @Exclude
    public String getDateCreatedTimestamp(){

        return DateUtils.getRelativeTimeSpanString(
                creationTimestamp.getSeconds()*1000
        ).toString();
    }

}
