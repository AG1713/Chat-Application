package com.example.chatapp.repository.databases;

import android.util.Log;

import com.example.chatapp.Callbacks.CompletionCallback;
import com.example.chatapp.Callbacks.FireStoreChatRoomIdCallback;
import com.example.chatapp.Callbacks.FireStoreDocumentReferenceCallback;
import com.example.chatapp.Callbacks.FirebaseAuthUidCallback;
import com.example.chatapp.Callbacks.StoragePhotoUrlCallback;
import com.example.chatapp.repository.models.ChatRoom;
import com.example.chatapp.repository.models.Group;
import com.example.chatapp.repository.models.Member;
import com.example.chatapp.repository.models.Message;
import com.example.chatapp.repository.models.User;
import com.example.chatapp.repository.models.UserChat;
import com.example.chatapp.repository.models.UserGroup;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;

public class FireStoreDB {
    private final FirebaseFirestore database;
    private final CollectionReference usersRef;
    private DocumentReference currentUserReference;
    private String currentUserId;
    private final CollectionReference groupsRef;
    private final CollectionReference chatRoomsRef;

    // NOTE: At last when the activities get destroyed, no objects of Repository remain,
    //       so no manual deletion of values in variables is performed here.
    //       Situation might change later (probably when using dependency injection).

    public FireStoreDB(String Uid) {
        this.database = FirebaseFirestore.getInstance();
        this.usersRef = database.collection("Users");
        this.currentUserReference = usersRef.document(Uid);

//        this.currentUserReference.get().addOnSuccessListener(snapshot -> {
//            currentUser = snapshot.toObject(User.class);
//        });

        this.currentUserId = Uid;
        this.groupsRef = database.collection("Groups");
        this.chatRoomsRef = database.collection("ChatRooms");
    }

    public void createUserProfile(User user, FirebaseAuthUidCallback callback){
        currentUserId = user.getId();
        usersRef.document(user.getId()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                currentUserReference = usersRef.document(user.getId());
                callback.onCallback(user.getId());
            }
        });
    }

    public void getCurrentUser(FireStoreDocumentReferenceCallback callBack){
        callBack.onCallback(currentUserReference);
    }

    public DocumentReference getCurrentUser(){
        return usersRef.document(currentUserId);
    }

    public DocumentReference getSpecifiedUser(String id){
        return usersRef.document(id);
    }

    public void editCurrentUserProfile(String description, FireStoreDocumentReferenceCallback callBack){
        HashMap<String, String> updates = new HashMap<>();
        updates.put("description", description);
        // For now, only description, might scale in future

        currentUserReference.update("description", description)
                .addOnSuccessListener(unused -> {
//                    currentUserReference.get().addOnSuccessListener(snapshot ->
//                            currentUser = snapshot.toObject(User.class));

                    callBack.onCallback(currentUserReference);
                });

    }

    public void getUserProfilePhotoUrl(String id, StoragePhotoUrlCallback callback){
        usersRef.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                callback.onCallback(snapshot.toObject(User.class).getProfilePhotoUrl());
                // null case is handled by glide by displaying error photo, which is the default photo icon
            }
        });
    }

    public void updateProfilePhotoUrl(String Url, FireStoreDocumentReferenceCallback callback){
        currentUserReference.update("profilePhotoUrl", Url).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                callback.onCallback(currentUserReference);
            }
        });
    }

    public FirestoreRecyclerOptions<User> getUserOptions(String hint){
        Query query = usersRef.whereGreaterThanOrEqualTo("username", hint)
                .whereLessThan("username", hint + '\uf8ff')
                .whereNotEqualTo("id", currentUserId);
        // '\uf8ff' is a high Unicode value character that is often used as a
        // practical end boundary in queries. This is because it is one of the highest values
        // in the Unicode character set, so it effectively includes all possible values that
        // could follow a given prefix.

        return new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();
    }

    public FirestoreRecyclerOptions<UserChat> getCurrentUsersChats(){
        Query query = currentUserReference.collection("Chats")
                .orderBy("lastMessageSentTime", Query.Direction.DESCENDING);

        return new FirestoreRecyclerOptions.Builder<UserChat>().setQuery(query, UserChat.class).build();
    }

    // ***************************** ChatRooms *****************************

    private String getChatRoomIdForUsers(String userId1, String userId2){
        if (userId1.compareTo(userId2) > 0){
            return userId1 + "_" + userId2;
        }
        return userId2 + "_" + userId1;
    }

    private void createChatRoom(String chatRoomId, ArrayList<Member> members, FireStoreChatRoomIdCallback callBack){
        // chatRoomId - passed as a parameter since the chatroom could be made for groups or one-to-one chat
        ChatRoom newChatRoom = new ChatRoom(
                chatRoomId,
                members
        );
        chatRoomsRef.document(chatRoomId).set(newChatRoom)
                .addOnSuccessListener(unused -> {

                    callBack.onCallback(chatRoomId);
                    Log.d("FireStoreDB", "getOrCreateChatRoom: Ran");
                });
    }

    public DocumentReference getChatRoom(String chatRoomId){
        // Used to get any kind of chatroom
        return chatRoomsRef.document(chatRoomId);
    }

    public void createChatRoomForUsers(String userId, String username, FireStoreChatRoomIdCallback callBack){
        ArrayList<Member> members = new ArrayList<>();
        currentUserReference.get().addOnSuccessListener(snapshot -> {
            User currentUser = snapshot.toObject(User.class);
            members.add(new Member(currentUserId, currentUser.getUsername()));
            members.add(new Member(userId, username));

            String chatRoomId = getChatRoomIdForUsers(currentUserId, userId);

            createChatRoom(chatRoomId, members, new FireStoreChatRoomIdCallback() {
                @Override
                public void onCallback(String chatRoomId) {
                    for (int i=0 ; i<members.size() ; i++){

                        int finalI = i;
                        usersRef.document(members.get((i+1)%members.size()).getId()).get().addOnSuccessListener(snapshot ->
                                usersRef.document(members.get(finalI).getId()).collection("Chats")
                                        .document(chatRoomId).set(new UserChat(
                                                chatRoomId,
                                                members.get((finalI +1)%members.size()).getId(),
                                                snapshot.toObject(User.class).getUsername()
                                        )));

                        callBack.onCallback(chatRoomId);
                    }
                }
            });


        });


    }

    public void createChatRoomForGroups(String groupName, String description, ArrayList<Member> members, FireStoreChatRoomIdCallback callBack){

        String groupId = groupName.replace(' ','_') + Timestamp.now().getSeconds();
        String chatRoomId = groupId;

        // Effectively, chatRoomId and groupId are same, but lets avoid assumptions, since it may
        // change in future

        // Getting current user
        currentUserReference.get().addOnSuccessListener(snapshot -> {
            User currentUser = snapshot.toObject(User.class);
            members.add(new Member(currentUserId, currentUser.getUsername()));

            createChatRoom(chatRoomId, members, new FireStoreChatRoomIdCallback() {
                @Override
                public void onCallback(String chatRoomId) {

                    groupsRef.document(groupId).set(new Group(
                                    groupId,
                                    chatRoomId,
                                    groupName,
                                    description))
                            .addOnSuccessListener(unused -> {
                                for (int i=0; i< members.size() ; i++){
                                    usersRef.document(members.get(i).getId()).collection("Groups")
                                            .document(groupId).set(new UserGroup(
                                                    groupId,
                                                    groupName
                                            ));
                                }

                                callBack.onCallback(chatRoomId);
                            });

                }
            });

        });



    }


    // ***************************** Chats *****************************

    public FirestoreRecyclerOptions<Message> getAllMessages(String chatRoomId){
        Query query = chatRoomsRef.document(chatRoomId) // Getting document from collection reference isn't that heavy
                .collection("Messages").orderBy("time");

        // Thanks to FirestoreRecyclerOptions, we don't have to worry about pagination in case of
        // too many chats

        return new FirestoreRecyclerOptions.Builder<Message>().setQuery(query, Message.class).build();
    }

    // A trigger could be used instead of this, but its not free :(
    private void updateUserChats(String chatRoomId, Message message){
        chatRoomsRef.document(chatRoomId).get().addOnSuccessListener(snapshot -> {
            ArrayList<Member> members = snapshot.toObject(ChatRoom.class).getMembers();

            for (Member member : members){
                usersRef.document(member.getId()).collection("Chats").document(chatRoomId).update(
                        "lastMessage", message.getText(),
                        "lastMessageSenderId", message.getSenderId(),
                        "lastMessageSenderName", message.getSenderName(),
                        "lastMessageSentTime", message.getTime()
                );
            }
        });
    }

    private void updateGroupChats(String chatRoomId, Message message){
        chatRoomsRef.document(chatRoomId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                ArrayList<Member> members = snapshot.toObject(ChatRoom.class).getMembers();

                for (Member member : members){
                    usersRef.document(member.getId()).collection("Groups").document(chatRoomId).update(
                            "lastMessage", message.getText(),
                            "lastMessageSenderId", message.getSenderId(),
                            "lastMessageSenderName", message.getSenderName(),
                            "lastMessageSentTime", message.getTime()
                    );
                }
            }
        });
    }

    public void sendMessageToUser(String username, String chatRoomId, String messageText){

        Message message = new Message(
                currentUserId,
                username,
                messageText,
                Timestamp.now()
        );

        chatRoomsRef.document(chatRoomId).collection("Messages").add(message)
                .addOnSuccessListener(documentReference -> updateUserChats(chatRoomId, message)); // Only difference
        // Here i am directly accessing chatroom, since getting the reference is very fast whatsoever
    }

    public void sendMessageToGroup(String username, String chatRoomId, String messageText){
        Message message = new Message(
                currentUserId,
                username,
                messageText,
                Timestamp.now()
        );

        chatRoomsRef.document(chatRoomId).collection("Messages").add(message)
                .addOnSuccessListener(documentReference -> updateGroupChats(chatRoomId, message)); // Only difference
        // Here i am directly accessing chatroom, since getting the reference is very fast whatsoever
    }

    // ***************************** Groups *****************************

    // Necessary to see group info
    public DocumentReference getGroup(String groupId){
        return groupsRef.document(groupId);
    }

    public void addGroupMembers(String groupId, String groupName, String chatRoomId, ArrayList<Member> newMembers){

        chatRoomsRef.document(chatRoomId).update("members", FieldValue.arrayUnion(newMembers.toArray()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        for (Member member : newMembers){
                            usersRef.document(member.getId()).collection("Groups")
                                    .document(chatRoomId).set(new UserGroup(groupId, groupName));
                        }
                    }
                });
    }

    public FirestoreRecyclerOptions<UserGroup> getCurrentUsersGroups(){

        Query query = currentUserReference.collection("Groups")
                .orderBy("lastMessageSentTime", Query.Direction.DESCENDING);

        return new FirestoreRecyclerOptions.Builder<UserGroup>().setQuery(query, UserGroup.class).build();
    }

    private void removeMember(String chatRoomId, CompletionCallback callback){
        // So first we get the username

        currentUserReference.get().addOnSuccessListener(snapshot -> {
            chatRoomsRef.document(chatRoomId).update("members", FieldValue.arrayRemove(
                    new Member(currentUserId, snapshot.toObject(User.class).getUsername())
            )).addOnSuccessListener(unused -> callback.onCallback());

        });

    }

    public void leaveGroup(String groupId, CompletionCallback callback){
        // This means, removing them from the chat room members, and also, removing 'UserGroup'
        // object

        // Note that the groupId and chatRoomId are same for now, might change in future
        removeMember(groupId, () -> {
            currentUserReference.collection("Groups").document(groupId).delete()
                    .addOnSuccessListener(unused -> {
                        callback.onCallback();
                        Log.d("FireStoreDB", "leaveGroup: Completed Successfully");
                    });

        });

    }

}
