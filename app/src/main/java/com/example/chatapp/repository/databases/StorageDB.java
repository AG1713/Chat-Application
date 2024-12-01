package com.example.chatapp.repository.databases;

import android.net.Uri;

import com.example.chatapp.Callbacks.StoragePhotoUrlCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class StorageDB {
    private final FirebaseStorage storage;
    private final StorageReference usersRef;
    private final StorageReference groupsRef;

    public StorageDB(){
        storage = FirebaseStorage.getInstance();
        usersRef = storage.getReference().child("users");
        groupsRef = storage.getReference().child("groups");
    }

    public void updateProfilePhoto(String currentUserId, Uri imageUri, StoragePhotoUrlCallback callback){
        String path = "photo-"+currentUserId;

        usersRef.child(path).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                usersRef.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        callback.onCallback(uri.toString());
                    }
                });
            }
        });
    }

    public void updateGroupPhoto(String groupId, Uri imageUri, StoragePhotoUrlCallback callback){
        String path = "photo-"+groupId;

        groupsRef.child(path).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                groupsRef.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        callback.onCallback(uri.toString());
                    }
                });
            }
        });

    }

}
