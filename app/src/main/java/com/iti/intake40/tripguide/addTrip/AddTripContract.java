package com.iti.intake40.tripguide.addTrip;

public interface AddTripContract {

    public interface AddTripView{

        public boolean noEmptyText(String input) ;
        public void checkValidation() ;
        public void showDataPickerDialog() ;
        public void showTimePickerDialog() ;
        public void goToHomePage();

    }

    public interface AddTripPresenter{

       public void addTripToDataBase(TripBojo tripBojo);

    }


}
