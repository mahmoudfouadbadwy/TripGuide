package com.iti.intake40.tripguide.login;

public class LoginPresenter implements LoginContract.LoginPresenter {
    private  Login login;
    public LoginPresenter(Login login)
    {
        this.login=login;
    }
    @Override
    public boolean loginWithEmail(String email, String password) {
        return false;
    }

    @Override
    public boolean loginWithGoogle() {
        return false;
    }

    @Override
    public void stop() {

    }
}
