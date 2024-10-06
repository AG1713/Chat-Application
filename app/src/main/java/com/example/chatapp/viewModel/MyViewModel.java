package com.example.chatapp.viewModel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.chatapp.Repository.Repository;
import com.example.chatapp.callbacks.FireStoreCallBack;
import com.example.chatapp.callbacks.FireStoreGroupsOptionsCallback;
import com.example.chatapp.model.ChatRoom;
import com.example.chatapp.model.ChatRoomReference;
import com.example.chatapp.model.Group;
import com.example.chatapp.model.Message;
import com.example.chatapp.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MyViewModel extends AndroidViewModel {
    Repository repository;
    Context context;
    public MutableLiveData<User> currentUser = new MutableLiveData<>();
    public MutableLiveData<ChatRoom> currentChatRoom = new MutableLiveData<>();
    public MutableLiveData<Group> currentGroup = new MutableLiveData<>();
    public MutableLiveData<FirestoreRecyclerOptions<ChatRoom>> recentUserChats = new MutableLiveData<>();
    public MutableLiveData<FirestoreRecyclerOptions<Group>> recentGroups = new MutableLiveData<>();

    public MyViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
        context = application.getApplicationContext();

//        repository.getCurrentUser().get()
//                .addOnSuccessListener(documentSnapshot ->
//                        currentUser.postValue(documentSnapshot.toObject(User.class)));

    }

    // Auth
    public void signUpAnonymousUser(){
        Context c = this.getApplication(); // We could do this since we extended AndroidViewModel
        repository.firebaseAnonymousAuth(c);
    }

    public void signInVerifiedUser(String email, String password){
        repository.signInVerifiedUser(email, password, context);
    }

    public void createVerifiedUser(String email, String password, User userModel, Context context){
        repository.createVerifiedUser(email, password, userModel,context);
    }

    public void signOut(){
        repository.signOut();
    }

    public MutableLiveData<User> getCurrentUser(){
        repository.getCurrentUser(new FireStoreCallBack() {
            @Override
            public void onCallBack(DocumentReference documentReference) {
                documentReference.get().addOnSuccessListener(documentSnapshot -> currentUser.postValue(documentSnapshot.toObject(User.class)));
            }
        });

        return currentUser;
    }
    public String getCurrentUserId(){
        return repository.getCurrentUserId();
    }

    public MutableLiveData<User> getUser(String userId){
        MutableLiveData<User> user = new MutableLiveData<>();

        repository.getUser(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user.postValue(documentSnapshot.toObject(User.class));
            }
        });

        return user;
    }

    public FirestoreRecyclerOptions<User> getSpecifiedUser(String username){
        Query query = repository.getAllUsersCollectionReference()
                .whereGreaterThanOrEqualTo("username", username)
                .whereLessThan("username", username + '\uf8ff')
                .whereNotEqualTo("id", repository.getCurrentUserId());
        // '\uf8ff' is a high Unicode value character that is often used as a
        // practical end boundary in queries. This is because it is one of the highest values
        // in the Unicode character set, so it effectively includes all possible values that
        // could follow a given prefix.
        
        return new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();
    }

    public String getChatRoomIdForUsers(String userId1, String userId2){
        return repository.getChatRoomIdForUsers(userId1, userId2);
    }

    // TODO: Create another version of getCurrentChatRoom for getting group related chatroom
    public MutableLiveData<ChatRoom> getCurrentChatRoom(String userId1, String userId2, ArrayList<String> members){
        Log.d("MyViewModel", "getCurrentChatRoom: Running");
        repository.getOrCreateChat(userId1, userId2, members,
                        new FireStoreCallBack() {
                            @Override
                            public void onCallBack(DocumentReference documentReference) {
                                documentReference.get().addOnSuccessListener(
                                        documentSnapshot -> currentChatRoom.postValue(documentSnapshot.toObject(ChatRoom.class))
                                );
                            }
                        });

        Log.d("MyViewModel", "getCurrentChatRoom: Ran");

        return currentChatRoom;
    }

    public void getChatRoomForGroups(String chatRoomId, FireStoreCallBack callBack){
        repository.getChatRoomForGroups(chatRoomId, callBack);
    }

    public MutableLiveData<ChatRoom> getChatRoomForGroups(String chatRoomId){
        repository.getChatRoomForGroups(chatRoomId, documentReference ->
                documentReference.get().addOnSuccessListener(documentSnapshot ->
                        currentChatRoom.postValue(documentSnapshot.toObject(ChatRoom.class))));

        return currentChatRoom;
    }


    public void sendMessageToUser(String messageText){

        ChatRoom chatRoom = new ChatRoom(
                currentChatRoom.getValue().getId(),
                currentChatRoom.getValue().getMembers(),
                messageText,
                repository.getCurrentUserId(),
                getCurrentUser().getValue().getUsername(),
                Timestamp.now()
        );
        Log.d("MyViewModel", "current chat room id : " + currentChatRoom.getValue().getId());

        repository.getChatRoomForUsers(currentChatRoom.getValue().getId()).set(chatRoom);


        Message message = new Message(
                currentUser.getValue().getId(),
                currentUser.getValue().getUsername(),
                messageText,
                Timestamp.now()
        );


        repository.getChatRoomChatMessagesReference(currentChatRoom.getValue().getId())
                .add(message);

    }

    public FirestoreRecyclerOptions<Message> getAllChats(String chatRoomId){
        getCurrentUser();

        Query query = repository.getChatRoomChatMessagesReference(chatRoomId)
                .orderBy("time", Query.Direction.ASCENDING);

        return new FirestoreRecyclerOptions.Builder<Message>().setQuery(query, Message.class).build();
    }

    public MutableLiveData<FirestoreRecyclerOptions<ChatRoom>> getCurrentUsersChats(){

        repository.getCurrentUsersChatsCollection().get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()){

                            List<String> chatRoomIds = new ArrayList<>();

                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                String chatId = doc.toObject(ChatRoomReference.class).getChatRomId();
                                Log.d("ViewModel", "getCurrentUsersChats: " + chatId);

                                if (chatId != null)
                                    chatRoomIds.add(chatId);
                            }

                            Log.d("ViewModel", "ChatRoomIds size: " + chatRoomIds.size());
                            // This will only update the LiveData if the chatRoomIds is not empty
                            if (!chatRoomIds.isEmpty()){
                                Query query = repository.getAllChatRoomReference()
                                        .whereIn(FieldPath.documentId(), chatRoomIds)
                                        .orderBy("lastMessageSentTime", Query.Direction.DESCENDING);

                                recentUserChats.postValue(new FirestoreRecyclerOptions.Builder<ChatRoom>()
                                        .setQuery(query, ChatRoom.class).build());
                            } else {
                                // If chatRoomIds is empty, post an empty FireStoreRecyclerOptions object
                                recentUserChats.postValue(new FirestoreRecyclerOptions.Builder<ChatRoom>().build());
                            }


                        }

                })
                .addOnFailureListener(e -> {
                    // Handle errors (like network failure, etc.)
                    recentUserChats.postValue(new FirestoreRecyclerOptions.Builder<ChatRoom>().build());
                });

        return recentUserChats;
    }

    public void getOtherUserFromChatroom(ArrayList<String> members, OnCompleteListener<DocumentSnapshot> listener){
        repository.getOtherUserFromChatroom(members, listener);
    }

    public MutableLiveData<Group> getOrCreateGroup(String groupName, String groupDescription, ArrayList<String> members){
        repository.getOrCreateGroup(groupName, groupDescription, members, new FireStoreCallBack() {
            @Override
            public void onCallBack(DocumentReference documentReference) {
                if (documentReference != null){
                    documentReference.get().addOnSuccessListener(documentSnapshot -> currentGroup.postValue(documentSnapshot.toObject(Group.class)));
                }
            }
        });

        return currentGroup;
    }

    public MutableLiveData<Group> getGroup(String groupId){
        MutableLiveData<Group> group = new MutableLiveData<>();

        repository.getGroup(groupId).get().addOnSuccessListener(documentSnapshot ->
                group.postValue(documentSnapshot.toObject(Group.class)));

        return group;
    }

    public void addGroupMembers(String chatRoomId, String groupId, ArrayList<String> newMembers){
        repository.addGroupMembers(chatRoomId, groupId, newMembers);
    }


    public MutableLiveData<FirestoreRecyclerOptions<Group>> getAllRecentGroupsOptions(){
        repository.getAllRecentGroupsOptions(new FireStoreGroupsOptionsCallback() {
            @Override
            public void onCallBack(FirestoreRecyclerOptions<Group> options) {
                recentGroups.postValue(options);
            }
        });

        return recentGroups;
    }



}
