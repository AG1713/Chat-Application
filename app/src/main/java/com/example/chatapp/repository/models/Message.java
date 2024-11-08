package com.example.chatapp.repository.models;

import android.text.format.DateUtils;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Exclude;

public class Message {

    String senderId;
    String senderName;
    String text;
    Timestamp time;

    public Message() {
    }

    public Message(String senderId, String senderName, String text, Timestamp time) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.text = text;
        this.time = time;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Exclude
    public boolean isMine() {
        // This function is just to check whether the message is ours or not.
        // This is obviously not a filed, so it is excluded

        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(senderId)){
            return true;
        }
        return false;
    }

    @Exclude
    public String convertTime(){
        String relativeTime = DateUtils.getRelativeTimeSpanString(
                time.getSeconds()*1000
        ).toString();

        return relativeTime;
    }


}
