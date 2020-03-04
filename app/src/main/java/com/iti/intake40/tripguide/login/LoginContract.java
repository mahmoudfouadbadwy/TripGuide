package com.iti.intake40.tripguide.login;

public interface LoginContract {
    public interface LoginView{
        public boolean validateEmail(String email);
        public boolean validatePassword(String password);
        public void goToHome();
    }
    public interface LoginPresenter{
        public boolean loginWithEmail(String email,String password);
        public boolean loginWithGoogle();
        public void stop();
    }
}
