package com.iti.intake40.tripguide.addTrip;

public interface AddTripContract {
     interface AddTripView{
         boolean checkEmpty(String input) ;
         boolean checkValidation() ;
         void showDataPickerDialog() ;
         void showTimePickerDialog() ;
         void displayMessage(String message);
         void goToHomePage();
    }
     interface AddTripPresenter{
        void addTrip(String tripName,String startPoint,String endPoint,String timerText,String date,String status,String direction ,String repeat);
        void stop();
        void onSuccess();
    }


}
