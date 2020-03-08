package com.iti.intake40.tripguide.login;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.iti.intake40.tripguide.R;
import com.iti.intake40.tripguide.home.Home;
import com.iti.intake40.tripguide.registration.SignUpActivity;

public class Login extends AppCompatActivity implements LoginContract.LoginView {
    private Intent signUpIntent;
    private Button loginBtn;
    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth;
    private LoginContract.LoginPresenter presenter;
    private Intent homeIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupViews();
        presenter = new LoginPresenter(this);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (checkValidation())
               {
                   presenter.loginWithEmail(email.getText().toString(),password.getText().toString());
               }
            }
        });
    }

    @Override
    public boolean validateEmail(String email) {
        boolean check  ;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.matches(emailPattern) && email.isEmpty()==false)
        {
            this.email.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.email,0);
            check = true;
        }
        else {
            this.email.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.error,0);
            check  = false;
        }
        return check;
    }

    @Override
    public boolean validatePassword(String password) {
        boolean check ;

        if(password.length() >= 6){
            this.password.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.password,0);
            check = true ;
        }
        else {
            this.password.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.error,0);
            Toast.makeText(Login.this,"Invalid PassWord",Toast.LENGTH_LONG).show();
            check  = false;
        }
        return check;
    }

    private boolean checkValidation()
    {
        boolean check =false;
        if ( validateEmail(email.getText().toString()) & validatePassword(password.getText().toString()))
        {
            check = true;
        }
      return  check;
    }
    @Override
    public void goToHome()
    {
        homeIntent = new Intent(this, Home.class);
        homeIntent.putExtra("Email",mAuth.getCurrentUser().getEmail());
        startActivity(homeIntent);
        finish();
    }

    @Override
    public void displayError(String message) {
        Toast.makeText(Login.this,message,Toast.LENGTH_LONG).show();
    }

    private void setupViews()
    {
        loginBtn = findViewById(R.id.loginButton);
        email = findViewById(R.id.emailText);
        password = findViewById(R.id.passwordText);
       // googleBtn = findViewById(R.id.signGoogle);
        mAuth = FirebaseAuth.getInstance();

    }

    public void goToSignUpActivity(View view) {
        signUpIntent = new Intent(Login.this, SignUpActivity.class);
        startActivity(signUpIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
       if( mAuth.getCurrentUser() != null)
       {
           goToHome();
       }
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stop();
    }
}
