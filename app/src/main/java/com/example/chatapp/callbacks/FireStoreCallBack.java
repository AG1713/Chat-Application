package com.example.chatapp.callbacks;

import com.google.firebase.firestore.DocumentReference;

public interface FireStoreCallBack {
    void onCallBack(DocumentReference documentReference);

}
