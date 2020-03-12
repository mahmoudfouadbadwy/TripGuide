package com.iti.intake40.tripguide.model;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iti.intake40.tripguide.addTrip.AddTripContract;
import com.iti.intake40.tripguide.addTrip.AddTripPresenter;
import com.iti.intake40.tripguide.home.RecycleAdapter;

import java.util.ArrayList;

public class RealTime {
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private String key;
    private DatabaseReference noteReference;
    private ArrayList<String> noteList;
    private RecycleAdapter _recycleAdapter;
    private AddTripPresenter _AddTripPresenter;

    public RealTime() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference("TripGuide");
        }
    }

    public RealTime(AddTripPresenter presenter) {
        _AddTripPresenter =presenter;
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference("TripGuide");
        }
    }

    public RealTime(String key, RecycleAdapter recycleAdapter) {
        _recycleAdapter = recycleAdapter;
        if (noteReference == null) {
            user = FirebaseAuth.getInstance().getCurrentUser();
            noteReference = FirebaseDatabase.getInstance().
                    getReference("TripGuide").
                    child(user.getUid()).child(key).child("Notes");
        }
        if (noteList == null) {
            noteList = new ArrayList<>();
        }
    }

    public void addTrip(Trip trip) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        key = mDatabase.child(user.getUid()).push().getKey();
        mDatabase.child(user.getUid()).child(key).setValue(trip);
        mDatabase.keepSynced(true);
        _AddTripPresenter.setAlarm(trip,key);
    }

    public void editTrip(Trip trip,String key)
    {
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase.child(user.getUid()).child(key).child("day").setValue(trip.getDay());
        mDatabase.child(user.getUid()).child(key).child("direction").setValue(trip.getDirection());
        mDatabase.child(user.getUid()).child(key).child("endPoint").setValue(trip.getEndPoint());
        mDatabase.child(user.getUid()).child(key).child("startPoint").setValue(trip.getStartPoint());
        mDatabase.child(user.getUid()).child(key).child("repeating").setValue(trip.getRepeating());
        mDatabase.child(user.getUid()).child(key).child("time").setValue(trip.getTime());
        mDatabase.child(user.getUid()).child(key).child("tripName").setValue(trip.getTripName());
        mDatabase.keepSynced(true);
    }

    public void makeDone(String key) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase.child(user.getUid()).child(key).child("status").setValue("Done");
        mDatabase.keepSynced(true);
    }

    public void cancelTrip(String key) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase.child(user.getUid()).child(key).child("status").setValue("Cancel");
        mDatabase.keepSynced(true);
    }

    public void deleteTrip(String key) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase.child(user.getUid()).child(key).removeValue();
        mDatabase.keepSynced(true);
    }


    public void addNote(String content, String key) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        System.out.println(user.getUid());
        System.out.println(key);
        System.out.println(mDatabase.child(user.getUid()).child(key));
        System.out.println( mDatabase.child(user.getUid()).child(key).child("Notes").push().getKey());
        String noteKey = mDatabase.child(user.getUid()).child(key).child("Notes").push().getKey();
        mDatabase.child(user.getUid()).child(key).child("Notes").
                child(noteKey).setValue(content);
        mDatabase.keepSynced(true);
    }


    public void getNotes() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    noteList.add(data.getValue().toString());

                    System.out.println(noteList.get(noteList.size() - 1));
                }
                System.out.println(noteList.size());
               _recycleAdapter.showNotes(noteList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        noteReference.addValueEventListener(postListener);
    }


}
