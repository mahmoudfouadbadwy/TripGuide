package com.iti.intake40.tripguide.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iti.intake40.tripguide.R;

public class SignUpActivity extends AppCompatActivity implements SignUpContract.SignUpView {

    EditText nameTxt ;
    EditText mailTxt ;
    EditText passwordTxt ;
    EditText conformPasswordTxt ;
    Button signupBtn ;




    SignUpContract.SignUpPresenter signUpPresenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_main) ;

        // Define component
        nameTxt = findViewById(R.id.nameText) ;
        mailTxt = findViewById(R.id.mailText) ;
        passwordTxt = findViewById(R.id.passwordText) ;
        conformPasswordTxt = findViewById(R.id.confirmText) ;
        signupBtn = findViewById(R.id.signUpButton) ;




        //Define singupPresenter
        signUpPresenter = new SignUpPresenter(this);


        //Button clicked
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean nameValid = validateName(nameTxt.getText().toString().trim());
                boolean emailValid = validateEmail(mailTxt.getText().toString().trim());
                boolean passwordValil = validatePassword(passwordTxt.getText().toString().trim());
                boolean conformPasswordValid = validateConfirmPassword(conformPasswordTxt.getText().toString().trim());
                makeValidation(passwordValil ,conformPasswordValid ,nameValid ,emailValid);

            }
        });



    }

    @Override
    public boolean validateName(String name) {

        boolean check  = false ;

        if(name.isEmpty() == false){
            check = true ;
        }
        return check;
    }

    @Override
    public boolean validateEmail(String mail) {
        boolean check  = false ;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (mail.matches(emailPattern) && mail.isEmpty()==false)
        {
            check = true;
        }

        return check;
    }



    @Override
    public boolean validatePassword(String password) {
        boolean check  = false ;

        if(password.length() >= 6){
            check = true ;
        }
        return check;
    }

    @Override
    public boolean validateConfirmPassword(String confirmPassword) {
        boolean check  = false ;

        if(passwordTxt.getText().toString().equals(confirmPassword))
        {
            check = true;
        }

        return check;
    }

    void makeValidation(boolean password , boolean conformPassword ,boolean name ,boolean email ){
        if(password == true && conformPassword == true && name == true && email == true){
            signUpPresenter.signUp(mailTxt.getText().toString() , passwordTxt.getText().toString() ,nameTxt.getText().toString());
        }

    }

}
