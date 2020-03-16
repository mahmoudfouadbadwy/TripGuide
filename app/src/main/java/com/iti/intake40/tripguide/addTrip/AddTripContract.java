package com.iti.intake40.tripguide.addTrip;

import android.widget.TextView;

import com.iti.intake40.tripguide.model.Trip;

public interface AddTripContract {
    interface AddTripView {
        void setupViews();

        boolean checkEmpty(String input);

        boolean checkValidation();

        void showDataPickerDialog();

        void showTimePickerDialog();

        void displayMessage(String message);

        void goToHomePage();

        void setAlarm(Trip trip, String key);

        void getLocations(int fragment, final TextView result);

        void getTripData();

        void cancelAlarm();

        String[] splitString(String txt, String operation);

        void editTrip();

        void addTrip();
    }

    interface AddTripPresenter {
        void addTrip(String tripName, String startPoint, String endPoint, String timerText, String date, String status, String direction, String repeat);

        void editTrip(String tripName, String startPoint, String endPoint, String timerText, String date, String status, String direction, String repeat, String key, Boolean change);

        void stop();

        void onSuccess();

        void setAlarm(Trip trip, String key);

    }


}
