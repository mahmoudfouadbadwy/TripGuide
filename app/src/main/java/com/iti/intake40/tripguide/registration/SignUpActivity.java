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
import com.iti.intake40.tripguide.login.Login;

public class SignUpActivity extends AppCompatActivity implements SignUpContract.SignUpView {

    EditText nameTxt ;
    EditText mailTxt ;
    EditText passwordTxt ;
    EditText conformPasswordTxt ;
    Button signupBtn ;
    TextView loginLink;
    Intent loginIntent;


    SignUpContract.SignUpPresenter signUpPresenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_main) ;
        setupView();
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


        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginIntent = new Intent(SignUpActivity.this,Login.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    @Override
    public boolean validateName(String name) {

        boolean check  = false ;

        if(name.isEmpty() == false){
            nameTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.user,0);
            check = true ;
        }
        else{
            nameTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.error,0);
        }
        return check;
    }

    @Override
    public boolean validateEmail(String mail) {
        boolean check  = false ;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (mail.matches(emailPattern) && mail.isEmpty()==false)
        {
            mailTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.email,0);
            check = true;
        }
        else {
            mailTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.error,0);
        }

        return check;
    }



    @Override
    public boolean validatePassword(String password) {
        boolean check  = false ;

        if(password.length() >= 6){
            passwordTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.password,0);
            check = true ;
        }
        else {
            passwordTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.error,0);
            Toast.makeText(SignUpActivity.this,"PassWord Must Be More Than 6 Char",Toast.LENGTH_LONG).show();
        }
        return check;
    }

    @Override
    public boolean validateConfirmPassword(String confirmPassword) {
        boolean check  = false ;

        if(passwordTxt.getText().toString().equals(confirmPassword))
        {
            conformPasswordTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.confirm,0);
            check = true;
        }
        else {
            conformPasswordTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.error,0);
        }

        return check;
    }

    void makeValidation(boolean password , boolean conformPassword ,boolean name ,boolean email ){
        if(password == true && conformPassword == true && name == true && email == true){
           if( signUpPresenter.signUp(mailTxt.getText().toString() , passwordTxt.getText().toString() ,nameTxt.getText().toString()))
           {

           }
           else {
               Toast.makeText(SignUpActivity.this,"Email Already Exist",Toast.LENGTH_LONG).show();
           }
        }

    }


    private void setupView()
    {
        // Define component
        nameTxt = findViewById(R.id.nameText) ;
        mailTxt = findViewById(R.id.mailText) ;
        passwordTxt = findViewById(R.id.passwordText) ;
        conformPasswordTxt = findViewById(R.id.confirmText) ;
        signupBtn = findViewById(R.id.signUpButton) ;
        loginLink =findViewById(R.id.loginText);
    }

}
