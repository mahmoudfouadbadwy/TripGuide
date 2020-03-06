package com.iti.intake40.tripguide.login;

public interface LoginContract {
    public interface LoginView{
        public boolean validateEmail(String email);
        public boolean validatePassword(String password);
        public void goToHome(String email);
        public void displayError(String message);
    }
    public interface LoginPresenter{
        public void loginWithEmail(String email,String password);
        public void stop();
    }
}
