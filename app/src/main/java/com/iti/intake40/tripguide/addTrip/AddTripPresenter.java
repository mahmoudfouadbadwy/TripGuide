package com.iti.intake40.tripguide.addTrip;

import com.iti.intake40.tripguide.model.RealTime;
import com.iti.intake40.tripguide.model.Trip;


public class AddTripPresenter implements AddTripContract.AddTripPresenter {
    private AddTripContract.AddTripView addTripView;
    private RealTime realTime;

    public AddTripPresenter(AddTripContract.AddTripView addTripView) {
        this.addTripView = addTripView;
        realTime = new RealTime(this);
    }

    @Override
    public void addTrip(String tripName, String startPoint, String endPoint, String timerText, String date, String status, String direction, String repeat) {
        realTime.addTrip(new Trip(tripName, startPoint, endPoint, date, timerText, status, direction, repeat));

    }

    @Override
    public void editTrip(String tripName, String startPoint, String endPoint, String timerText, String date, String status, String direction, String repeat,String key) {
        realTime.editTrip(new Trip(tripName, startPoint, endPoint, date, timerText, status, direction, repeat),key );
        onSuccess();
    }

    @Override
    public void stop() {
        addTripView = null;
        realTime = null;
    }

    @Override
    public void onSuccess() {
        addTripView.goToHomePage();
    }
    @Override
   public void setAlarm(Trip trip,String key)
   {
       addTripView.setAlarm(trip,key);
       onSuccess();
   }
}
