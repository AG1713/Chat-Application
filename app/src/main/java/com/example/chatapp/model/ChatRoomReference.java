package com.example.chatapp.model;

public class ChatRoomReference {
    String chatRomId;

    public ChatRoomReference() {
    }

    public ChatRoomReference(String chatRomId) {
        this.chatRomId = chatRomId;
    }

    public String getChatRomId() {
        return chatRomId;
    }

    public void setChatRomId(String chatRomId) {
        this.chatRomId = chatRomId;
    }
}
