package com.iti.intake40.tripguide.model;

import com.google.firebase.database.FirebaseDatabase;

public class FireBaseSettings extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /* Enable disk persistence */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

}
