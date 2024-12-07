package com.example.chatapp.viewmodels;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatapp.Callbacks.CompletionCallback;
import com.example.chatapp.Callbacks.FireStoreDocumentReferenceCallback;
import com.example.chatapp.Callbacks.StoragePhotoUrlCallback;
import com.example.chatapp.repository.models.User;
import com.example.chatapp.repository.models.UserChat;
import com.example.chatapp.repository.models.UserGroup;
import com.example.chatapp.repository.Repository;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class MainActivityViewModel extends ViewModel {
    private final Repository repository;
    public MutableLiveData<User> currentUser;

    public MainActivityViewModel(){
        this.repository = new Repository();
        this.currentUser = new MutableLiveData<>();
    }

    public LiveData<User> getCurrentUser(){
        repository.getCurrentUser().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                currentUser.postValue(snapshot.toObject(User.class));
            }
        });
        return currentUser;
    }

    public void editCurrentUserProfile(String description){
        repository.editCurrentUserProfile(description, new FireStoreDocumentReferenceCallback() {
                    @Override
                    public void onCallback(DocumentReference user) {
                        user.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot snapshot) {
                                currentUser.postValue(snapshot.toObject(User.class));
                            }
                        });
                    }
                }
        );
    }

    public void updateLastActiveTime(){
        repository.updateLastActiveTime(
                user -> user.get().addOnSuccessListener(
                        snapshot -> currentUser.postValue(snapshot.toObject(User.class))
                )
        );
    }

    public void updateProfilePhoto(Uri imageUri){
        repository.updateProfilePhoto(imageUri, new FireStoreDocumentReferenceCallback() {
            @Override
            public void onCallback(DocumentReference user) {
                user.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        currentUser.postValue(snapshot.toObject(User.class));
                    }
                });
            }
        });
    }

    public void getUserProfilePhotoUrl(String id, StoragePhotoUrlCallback callback){
        repository.getUserProfilePhotoUrl(id, callback);
    }

    public FirestoreRecyclerOptions<UserChat> getCurrentUsersChats(){
        return repository.getCurrentUsersChats();
    }

    public FirestoreRecyclerOptions<UserGroup> getCurrentUsersGroups(){
        return repository.getCurrentUsersGroups();
    }

    public void getGroupPhotoUrl(String groupId, StoragePhotoUrlCallback callback){
        repository.getGroupPhotoUrl(groupId, callback);
    }



    public void signOut(){
        repository.signOut();
    }

}
