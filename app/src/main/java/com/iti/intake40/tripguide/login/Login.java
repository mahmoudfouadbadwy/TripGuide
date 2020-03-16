package com.iti.intake40.tripguide.login;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    ///////face
    private LoginButton floginButton;
    private CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupViews();
        floginButton=findViewById(R.id.login_button);
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

        callbackManager = CallbackManager.Factory.create();
        floginButton.setReadPermissions("email", "public_profile");

        floginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            goToHome();
                            //c3WypRk4jZ785IRJ3rTrAldGklw=
                        } else {
                            Toast.makeText(Login.this, "Login failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
