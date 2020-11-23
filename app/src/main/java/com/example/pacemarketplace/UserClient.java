package com.example.pacemarketplace;

import android.app.Application;

import com.example.pacemarketplace.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserClient extends Application {

    private User user = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}