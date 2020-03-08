package com.iti.intake40.tripguide.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RealTime {
    private DatabaseReference mDatabase;
    private FirebaseUser user ;
    private String key;

    public RealTime()
    {
        if (mDatabase == null) {
            mDatabase =FirebaseDatabase.getInstance().getReference("TripGuide");
        }
    }

    public void addTrip(Trip trip)
    {
        user = FirebaseAuth.getInstance().getCurrentUser();
        key = mDatabase.child(user.getUid()).push().getKey();
        mDatabase.child(user.getUid()).child(key).setValue(trip);
        mDatabase.keepSynced(true);

    }
}
