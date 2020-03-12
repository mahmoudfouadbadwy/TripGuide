package com.iti.intake40.tripguide.addTrip;

import com.iti.intake40.tripguide.model.Trip;

public interface AddTripContract {
     interface AddTripView{
         boolean checkEmpty(String input) ;
         boolean checkValidation() ;
         void showDataPickerDialog() ;
         void showTimePickerDialog() ;
         void displayMessage(String message);
         void goToHomePage();
         void setAlarm(Trip trip,String key);
    }
     interface AddTripPresenter{
        void addTrip(String tripName,String startPoint,String endPoint,String timerText,String date,String status,String direction ,String repeat);
        void editTrip(String tripName,String startPoint,String endPoint,String timerText,String date,String status,String direction ,String repeat,String key);
        void stop();
        void onSuccess();
        void setAlarm(Trip trip,String key);
    }


}
