package com.iti.intake40.tripguide.registration;

public interface SignUpContract {
     interface SignUpView{

         boolean validateEmail(String mail);
         boolean validatePassword(String password);
         boolean validateConfirmPassword(String confirmPassword);
         void goToHome(String email);
    }
     interface SignUpPresenter{

         boolean signUp(String mail,String password );
         void stop();
    }
}
