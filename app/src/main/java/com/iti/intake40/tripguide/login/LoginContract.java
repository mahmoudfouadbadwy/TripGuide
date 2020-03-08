package com.iti.intake40.tripguide.login;

public interface LoginContract {
     interface LoginView{
         boolean validateEmail(String email);
         boolean validatePassword(String password);
         void goToHome();
         void displayError(String message);
    }
     interface LoginPresenter{
         void loginWithEmail(String email,String password);
         void onSuccess();
         void onFailure();
         void stop();
    }
}
