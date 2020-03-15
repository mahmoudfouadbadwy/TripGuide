package com.iti.intake40.tripguide.model;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iti.intake40.tripguide.addTrip.AddTripPresenter;
import com.iti.intake40.tripguide.home.HistoryAdapter;
import com.iti.intake40.tripguide.home.UpcomingAdapter;

import java.util.ArrayList;

public class RealTime {
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private String key;
    private DatabaseReference noteReference;
    private ArrayList<String> noteList;
    private UpcomingAdapter _upcomingAdapter;
    private HistoryAdapter _HistoryAdapter;
    private AddTripPresenter _AddTripPresenter;
    private DatabaseReference addNoteRef;

    public RealTime() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference("TripGuide");
        }
    }

    public RealTime(AddTripPresenter presenter) {
        _AddTripPresenter = presenter;
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference("TripGuide");
        }
    }

    public RealTime(String key, UpcomingAdapter upcomingAdapter) {
        _upcomingAdapter = upcomingAdapter;
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

    public RealTime(String key, HistoryAdapter historyAdapter) {
        _HistoryAdapter = historyAdapter;
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
        _AddTripPresenter.setAlarm(trip, key);
    }

    public void editTrip(Trip trip, String key) {
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
        String noteKey;
        addNoteRef = mDatabase.child(user.getUid()).child(key);
        noteKey = addNoteRef.child("Notes").push().getKey();
        addNoteRef.child("Notes").child(noteKey).setValue(content);
        addNoteRef.keepSynced(true);
    }


    public void getNotes() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    noteList.add(data.getValue().toString());
                }
                if (_upcomingAdapter != null) {
                    _upcomingAdapter.showNotes(noteList);
                } else if (_HistoryAdapter != null) {
                    _HistoryAdapter.showNotes(noteList);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        noteReference.addValueEventListener(postListener);
    }


}
