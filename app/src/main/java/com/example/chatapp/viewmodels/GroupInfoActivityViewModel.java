package com.example.chatapp.viewmodels;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatapp.Callbacks.CompletionCallback;
import com.example.chatapp.Callbacks.FireStoreDocumentReferenceCallback;
import com.example.chatapp.Callbacks.StoragePhotoUrlCallback;
import com.example.chatapp.repository.models.ChatRoom;
import com.example.chatapp.repository.models.Group;
import com.example.chatapp.repository.Repository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
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

    public void updateGroupPhoto(String groupId, Uri uri){
        repository.updateGroupPhotoUrl(groupId, uri,
                group -> group.get().addOnSuccessListener(
                        snapshot -> groupMutableLiveData.postValue(snapshot.toObject(Group.class))
                )
        );
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

    public void leaveGroup(String groupId, CompletionCallback callback){
        repository.leaveGroup(groupId, callback);
    }


}
