package com.example.chatapp.Callbacks;

import com.example.chatapp.repository.models.User;

public interface FireStoreUserObjectCallback {
    void onCallback(User user);
}
