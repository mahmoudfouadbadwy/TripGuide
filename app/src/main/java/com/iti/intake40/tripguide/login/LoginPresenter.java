package com.iti.intake40.tripguide.login;


import com.iti.intake40.tripguide.model.FireBaseModel;

public class LoginPresenter implements LoginContract.LoginPresenter {
    private Login login;
    private FireBaseModel model;
    public LoginPresenter(Login login)
    {
        this.login=login;
        model = new FireBaseModel();
    }

    @Override
    public void loginWithEmail(final String email, final String password)
    {
             model.signIn(email,password,login,this);
    }

    @Override
    public void onSuccess() {
        login.goToHome();
    }

    @Override
    public void onFailure() {
        login.displayError("Invalid Email Or Password");
    }


    @Override
    public void stop()
    {
        login = null;
        model = null;
    }


}
