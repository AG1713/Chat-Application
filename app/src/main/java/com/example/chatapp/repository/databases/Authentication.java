package com.example.chatapp.repository.databases;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.chatapp.Callbacks.FirebaseAuthUidCallback;
import com.example.chatapp.views.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Authentication {
    private final FirebaseAuth firebaseAuth;

    public Authentication() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    // TODO: Decide whether this stays or not
    public void firebaseAnonymousAuth(Context context){
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    // Authentication is successful
                    Intent i = new Intent(context, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // Indicates that a new task should be created for the activity that is being
                    // started. This means that the new activity will be started in the new task
                    // instead of same task
                    // IMPORTANT
                    // It is a good practice to use the above flag, since you are not moving from
                    // the current activity to other, you are doing it through another class,
                    // here, the repository

                    context.startActivity(i);

                }

            }
        });
    }

    public void signUpUser(String email, String password, FirebaseAuthUidCallback callback){
        if (!email.isEmpty() && !password.isEmpty()){
            FirebaseAuth.getInstance().signOut();

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            signInUser(email, password, callback);
                        }
                    });


        }

    }

    public void signInUser(String email, String password, FirebaseAuthUidCallback callback){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        callback.onCallback(firebaseAuth.getUid());
                        // if succeeds, gives valid id, else null
                    }
                });
    }

    // Sign out functionality
    public void signOut(){
        FirebaseAuth.getInstance().signOut();
    }

    // Guess what, its synchronous
    public String getCurrentUserId(){
        return firebaseAuth.getUid();
    }


}
