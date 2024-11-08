package com.example.chatapp.repository;

import com.example.chatapp.Callbacks.FireStoreChatRoomIdCallback;
import com.example.chatapp.Callbacks.FireStoreDocumentReferenceCallback;
import com.example.chatapp.Callbacks.FireStoreUserObjectCallback;
import com.example.chatapp.Callbacks.FirebaseAuthUidCallback;
import com.example.chatapp.repository.databases.Authentication;
import com.example.chatapp.repository.databases.FireStoreDB;
import com.example.chatapp.repository.models.Message;
import com.example.chatapp.repository.models.User;
import com.example.chatapp.repository.models.UserChat;
import com.example.chatapp.repository.models.UserGroup;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class Repository {
    Authentication authentication;
    FireStoreDB fireStoreDB;

    public Repository() {
        authentication = new Authentication();

        if (authentication.getCurrentUserId() != null){
            fireStoreDB = new FireStoreDB(authentication.getCurrentUserId());
        }
    }

    public void createUser(String email, String password, User user, FirebaseAuthUidCallback callback){
        authentication.signUpUser(email, password, new FirebaseAuthUidCallback() {
            @Override
            public void onCallback(String Uid) {
                user.setId(Uid);
                fireStoreDB = new FireStoreDB(Uid);
                fireStoreDB.createUserProfile(user, callback);
            }
        });
    }

    public void signInUser(String email, String password, FirebaseAuthUidCallback callBack){
        authentication.signInUser(email, password, new FirebaseAuthUidCallback() {
            @Override
            public void onCallback(String Uid) {
//                fireStoreDB = new FireStoreDB(Uid); // No need really
                callBack.onCallback(Uid);
                // if succeeds, gives a valid Uid, else null
            }
        });
    }

    public void signOut(){
        authentication.signOut();
        // No need of deleting the currentUser and currentUserId from the repository
        // since after this we will navigate to a new activity, and all the previous
        // instances will be gone
    }

    public String getCurrentUserId(){
        return authentication.getCurrentUserId();
    }

    public DocumentReference getCurrentUser(){
        return fireStoreDB.getCurrentUser();
    }

    public DocumentReference getSpecifiedUser(String id){
        return fireStoreDB.getSpecifiedUser(id);
    }

    public void editCurrentUserProfile(String description, FireStoreDocumentReferenceCallback callback){

        fireStoreDB.editCurrentUserProfile(description, callback);
    }

    public FirestoreRecyclerOptions<User> getUserOptions(String hint){
        return fireStoreDB.getUserOptions(hint);
    }

    public FirestoreRecyclerOptions<UserChat> getCurrentUsersChats(){
        return fireStoreDB.getCurrentUsersChats();
    }

    public void createChatRoomForUsers(String userId, FireStoreChatRoomIdCallback callback){
        fireStoreDB.createChatRoomForUsers(userId, callback);
    }

    public void createChatRoomForGroups(String groupName, String description, ArrayList<String> members, FireStoreChatRoomIdCallback callback){
        fireStoreDB.createChatRoomForGroups(groupName, description, members, callback);
    }

    public DocumentReference getChatRoom(String chatRoomId){
        return fireStoreDB.getChatRoom(chatRoomId);
    }

    public FirestoreRecyclerOptions<Message> getAllMessages(String chatRoomId){
        return fireStoreDB.getAllMessages(chatRoomId);
    }

    public void sendMessageToUser(String username, String chatRoomId, String messageText){
        fireStoreDB.sendMessageToUser(username, chatRoomId, messageText);
    }

    public void sendMessageToGroup(String username, String chatRoomId, String messageText){
        fireStoreDB.sendMessageToGroup(username, chatRoomId, messageText);
    }

    public DocumentReference getGroup(String groupId){
        return fireStoreDB.getGroup(groupId);
    }

    public void addGroupMembers(String groupId, String groupName, String chatRoomId, ArrayList<String> newMembers){
        fireStoreDB.addGroupMembers(groupId, groupName, chatRoomId, newMembers);
    }

    public FirestoreRecyclerOptions<UserGroup> getCurrentUsersGroups(){
        return fireStoreDB.getCurrentUsersGroups();
    }

}
