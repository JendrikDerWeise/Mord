package com.example.jendrik.moerder.GUI;

import android.content.Intent;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class MyApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this.getApplicationContext());

    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
