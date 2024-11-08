package com.example.chatapp.repository.models;

import android.text.format.DateUtils;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

public class UserGroup {
    String groupId;
    String groupName;
    String lastMessage;
    String lastMessageSenderId;
    String lastMessageSenderName;
    Timestamp lastMessageSentTime;

    public UserGroup() {
    }

    public UserGroup(String groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.lastMessage = "";
        this.lastMessageSenderId = "";
        this.lastMessageSenderName = "";
        lastMessageSentTime = Timestamp.now();
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageSenderId() {
        return lastMessageSenderId;
    }

    public void setLastMessageSenderId(String lastMessageSenderId) {
        this.lastMessageSenderId = lastMessageSenderId;
    }

    public String getLastMessageSenderName() {
        return lastMessageSenderName;
    }

    public void setLastMessageSenderName(String lastMessageSenderName) {
        this.lastMessageSenderName = lastMessageSenderName;
    }

    public Timestamp getLastMessageSentTime() {
        return lastMessageSentTime;
    }

    public void setLastMessageSentTime(Timestamp lastMessageSentTime) {
        this.lastMessageSentTime = lastMessageSentTime;
    }

    @Exclude
    public String convertTime(){
        if (lastMessageSentTime == null) return null;

        return DateUtils.getRelativeTimeSpanString(
                lastMessageSentTime.getSeconds()*1000
        ).toString();
    }

}
