package com.example.chatapp.repository.models;

import java.util.ArrayList;

public class ChatRoom {
    String id;
    ArrayList<Member> members;

    public ChatRoom() {
    }

    public ChatRoom(String id, ArrayList<Member> members) {
        this.id = id;
        this.members = members;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }

}
