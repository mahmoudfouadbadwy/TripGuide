package com.iti.intake40.tripguide.addTrip;

import com.iti.intake40.tripguide.model.RealTime;
import com.iti.intake40.tripguide.model.Trip;

import java.util.Random;


public class AddTripPresenter implements AddTripContract.AddTripPresenter {
    private AddTripContract.AddTripView addTripView;
    private RealTime realTime;
    Trip trip;
    public AddTripPresenter(AddTripContract.AddTripView addTripView) {
        this.addTripView = addTripView;
        realTime = new RealTime(this);
    }

    @Override
    public void addTrip(String tripName, String startPoint, String endPoint, String timerText, String date, String status, String direction, String repeat) {
        trip = new Trip(tripName, startPoint, endPoint, date, timerText, status, direction, repeat);
        trip.setAlarmKey(new Random().nextInt(10000));
        realTime.addTrip(trip);
    }

    @Override
    public void editTrip(String tripName, String startPoint, String endPoint, String timerText, String date, String status, String direction, String repeat,String key,Boolean change) {
        trip =new Trip(tripName, startPoint, endPoint, date, timerText, status, direction, repeat);
        trip.setAlarmKey(new Random().nextInt(10000));
        realTime.editTrip(trip,key,change);

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
