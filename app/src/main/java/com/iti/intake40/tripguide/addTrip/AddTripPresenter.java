package com.iti.intake40.tripguide.addTrip;

import com.iti.intake40.tripguide.model.RealTime;
import com.iti.intake40.tripguide.model.Trip;


public class AddTripPresenter implements AddTripContract.AddTripPresenter {
    private AddTripContract.AddTripView  addTripView ;
    private RealTime realTime;
    public AddTripPresenter(AddTripContract.AddTripView addTripView){
        this.addTripView = addTripView;
    }
    @Override
    public void addTrip(String tripName, String startPoint, String endPoint, String timerText, String date, String status, String direction, String repeat) {

        realTime = new RealTime();
        realTime.addTrip(new Trip(tripName,startPoint,endPoint,date,timerText,status,direction,repeat));
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



}
