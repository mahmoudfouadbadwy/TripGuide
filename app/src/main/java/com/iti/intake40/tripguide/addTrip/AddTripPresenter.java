package com.iti.intake40.tripguide.addTrip;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class AddTripPresenter implements AddTripContract.AddTripPresenter {

    // Write a message to the database
    private DatabaseReference mDatabase;
    private FirebaseUser user ;

    private AddTripContract.AddTripView  addTripView ;


    public AddTripPresenter(){

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference("TripGuide");


        user = FirebaseAuth.getInstance().getCurrentUser();

        addTripView = new AddTrip();


    }


    @Override
    public void addTripToDataBase(TripBojo tripBojo) {

        //user.getUid()
        String key = mDatabase.child("user id 111111").push().getKey();
        mDatabase.child("user id 111111").child(key).setValue(tripBojo);

        mDatabase.keepSynced(true);

        addTripView.goToHomePage();



    }
}
