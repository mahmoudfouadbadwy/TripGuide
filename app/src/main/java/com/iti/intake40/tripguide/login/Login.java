package com.iti.intake40.tripguide.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.iti.intake40.tripguide.R;

public class Login extends AppCompatActivity implements LoginContract.LoginView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public boolean validateEmail(String email) {
        return false;
    }

    @Override
    public boolean validatePassword(String password) {
        return false;
    }

    @Override
    public void goToHome() {

    }
}
