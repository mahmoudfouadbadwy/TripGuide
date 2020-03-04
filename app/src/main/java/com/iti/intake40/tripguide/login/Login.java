package com.iti.intake40.tripguide.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.iti.intake40.tripguide.R;
import com.iti.intake40.tripguide.registration.SignUpActivity;

public class Login extends AppCompatActivity implements LoginContract.LoginView {
    private TextView signUpLink;
    private Intent signUpIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupViews();

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

    private void setupViews()
    {
        signUpLink = findViewById(R.id.signUpText);
    }

    public void goToSignUpActivity(View view) {
        signUpIntent = new Intent(Login.this, SignUpActivity.class);
        startActivity(signUpIntent);
        finish();
    }
}
