package com.iti.intake40.tripguide.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.iti.intake40.tripguide.R;

public class SignUpActivity extends AppCompatActivity implements SignUpContract.SignUpView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean validateName(String name) {
        return false;
    }

    @Override
    public boolean validateEmail(String mail) {
        return false;
    }

    @Override
    public boolean validatePassword(String password) {
        return false;
    }

    @Override
    public boolean validateConfirmPassword(String confirmPassword) {
        return false;
    }
}
