package com.example.chatapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatapp.repository.models.ChatRoom;
import com.example.chatapp.repository.models.Group;
import com.example.chatapp.repository.Repository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class GroupInfoActivityViewModel extends ViewModel {
    private final Repository repository;
    private MutableLiveData<Group> groupMutableLiveData;
    private MutableLiveData<ChatRoom> chatRoomMutableLiveData;

    public GroupInfoActivityViewModel(){
        repository = new Repository();
        groupMutableLiveData = new MutableLiveData<>();
        chatRoomMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<Group> getGroup(String groupId){
        repository.getGroup(groupId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                groupMutableLiveData.postValue(snapshot.toObject(Group.class));
            }
        });

        return groupMutableLiveData;
    }

    public LiveData<ChatRoom> getChatRoom(String chatRoomId){
        repository.getChatRoom(chatRoomId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                chatRoomMutableLiveData.postValue(snapshot.toObject(ChatRoom.class));
            }
        });

        return chatRoomMutableLiveData;
    }




}
