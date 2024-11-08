package com.example.chatapp.repository.models;

import android.text.format.DateUtils;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

public class UserChat {
    String chatRoomId;
    String otherUserId;
    String otherUserName;
    String lastMessage;
    String lastMessageSenderId;
    String lastMessageSenderName;
    Timestamp lastMessageSentTime;

    public UserChat() {
    }

    public UserChat(String groupId, String otherUserId, String otherUserName) {
        this.chatRoomId = groupId;
        this.otherUserId = otherUserId;
        this.otherUserName = otherUserName;
        this.lastMessage = "";
        this.lastMessageSenderId = "";
        this.lastMessageSenderName = "";
        this.lastMessageSentTime = Timestamp.now();
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
    }

    public String getOtherUserName() {
        return otherUserName;
    }

    public void setOtherUserName(String otherUserName) {
        this.otherUserName = otherUserName;
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

        return DateUtils.getRelativeTimeSpanString(
                lastMessageSentTime.getSeconds()*1000
        ).toString();
    }


}
