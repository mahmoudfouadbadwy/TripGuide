package com.iti.intake40.tripguide.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iti.intake40.tripguide.R;
import com.iti.intake40.tripguide.home.Home;
import com.iti.intake40.tripguide.login.Login;

public class SignUpActivity extends AppCompatActivity implements SignUpContract.SignUpView {

    private EditText mailTxt;
    private EditText passwordTxt;
    private EditText conformPasswordTxt;
    private Button signUpBtn;
    private TextView loginLink;
    private Intent loginIntent;
    private SignUpContract.SignUpPresenter signUpPresenter;
    private Intent homeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();
        //Define singUpPresenter
        signUpPresenter = new SignUpPresenter(this);
        //Button clicked
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeValidation();

            }
        });
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });
    }

    @Override
    public boolean validateEmail(String mail) {
        boolean check = false;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (mail.matches(emailPattern) && mail.isEmpty() == false) {
            mailTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.email, 0);
            check = true;
        } else {
            mailTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
        }

        return check;
    }

    @Override
    public boolean validatePassword(String password) {
        boolean check = false;

        if (password.length() >= 6) {
            passwordTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.password, 0);
            check = true;
        } else {
            passwordTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
            Toast.makeText(SignUpActivity.this, "PassWord Must Be More Than 6 Char", Toast.LENGTH_LONG).show();
        }
        return check;
    }

    @Override
    public boolean validateConfirmPassword(String confirmPassword) {
        boolean check = false;

        if (passwordTxt.getText().toString().equals(confirmPassword)) {
            conformPasswordTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.confirm, 0);
            check = true;
        } else {
            conformPasswordTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
        }

        return check;
    }

    @Override
    public void goToHome() {
        homeIntent = new Intent(SignUpActivity.this, Home.class);
        homeIntent.putExtra("Email", mailTxt.getText().toString());
        startActivity(homeIntent);
        finish();
    }

    @Override
    public void goToLogin() {
        loginIntent = new Intent(SignUpActivity.this, Login.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        signUpPresenter.stop();
    }

    void makeValidation() {

        boolean email = validateEmail(mailTxt.getText().toString().trim());
        boolean password = validatePassword(passwordTxt.getText().toString().trim());
        boolean conformPassword = validateConfirmPassword(conformPasswordTxt.getText().toString().trim());
        if (email == true & password == true & conformPassword == true) {
            signUpPresenter.signUp(mailTxt.getText().toString(), passwordTxt.getText().toString());
        }

    }

    private void setupView() {
        // Define component
        mailTxt = findViewById(R.id.mailText);
        passwordTxt = findViewById(R.id.passwordText);
        conformPasswordTxt = findViewById(R.id.confirmText);
        signUpBtn = findViewById(R.id.signUpButton);
        loginLink = findViewById(R.id.loginText);
    }

}
