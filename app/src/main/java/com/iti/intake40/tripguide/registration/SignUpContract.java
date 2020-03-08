package com.iti.intake40.tripguide.registration;

public interface SignUpContract {
     interface SignUpView{
         boolean validateEmail(String mail);
         boolean validatePassword(String password);
         boolean validateConfirmPassword(String confirmPassword);
         void goToHome();
         void goToLogin();
         void displayMessage(String message);
    }
     interface SignUpPresenter{
         void signUp(String mail,String password );
         void onSuccess();
         void onFailure();
         void stop();
    }
}
