package com.example.chatapp.model;

import android.text.format.DateUtils;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

public class ChatRoom {
    String id;
    ArrayList<String> members;
    String lastMessage;
    String lastMessageSenderId;
    String lastMessageSenderName;
    Timestamp lastMessageSentTime;

    public ChatRoom() {
    }

    public ChatRoom(String id, ArrayList<String> members, String lastMessage, String lastMessageSenderId,String lastMessageSenderName, Timestamp lastMessageSentTime) {
        this.id = id;
        this.members = members;
        this.lastMessage = lastMessage;
        this.lastMessageSenderId = lastMessageSenderId;
        this.lastMessageSenderName = lastMessageSenderName;
        this.lastMessageSentTime = lastMessageSentTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
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
