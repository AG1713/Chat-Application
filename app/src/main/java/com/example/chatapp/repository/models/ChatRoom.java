package com.example.chatapp.repository.models;

import java.util.ArrayList;

public class ChatRoom {
    String id;
    ArrayList<String> members;

    public ChatRoom() {
    }

    public ChatRoom(String id, ArrayList<String> members) {
        this.id = id;
        this.members = members;
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

}
