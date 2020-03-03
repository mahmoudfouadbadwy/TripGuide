package com.iti.intake40.tripguide.registration;

public interface SignUpContract {
    public interface SignUpView{
        public boolean validateName(String name);
        public boolean validateEmail(String mail);
        public boolean validatePassword(String password);
        public boolean validateConfirmPassword(String confirmPassword);
    }
    public interface SignUpPresenter{

        public boolean signUp(String mail,String password ,String name);
        public void stop();
    }
}
