package com.iti.intake40.tripguide.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.iti.intake40.tripguide.R;
import com.iti.intake40.tripguide.registration.SignUpActivity;

public class Login extends AppCompatActivity implements LoginContract.LoginView {
    private Intent signUpIntent;
    private Button loginBtn;
    private EditText email;
    private EditText password;
    private SignInButton googleBtn;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;

    private LoginContract.LoginPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupViews();
        configureGoogle();
        presenter = new LoginPresenter(this);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (checkValidation())
               {
                   if(presenter.loginWithEmail(email.getText().toString(),password.getText().toString()))
                   {
                       // go to home .
                       Toast.makeText(Login.this,"Login",Toast.LENGTH_LONG).show();
                   }
                   else
                   {
                       Toast.makeText(Login.this,"Invalid Email Or Password",Toast.LENGTH_LONG).show();
                   }
               }
            }
        });

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
    }

    @Override
    public boolean validateEmail(String email) {
        boolean check  = false ;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.matches(emailPattern) && email.isEmpty()==false)
        {
            this.email.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.email,0);
            check = true;
        }
        else {
            this.email.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.error,0);
        }
        return check;
    }

    @Override
    public boolean validatePassword(String password) {
        boolean check  = false ;

        if(password.length() >= 6){
            this.password.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.password,0);
            check = true ;
        }
        else {
            this.password.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.error,0);
            Toast.makeText(Login.this,"PassWord Must Be More Than 6 Char",Toast.LENGTH_LONG).show();
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
    public void goToHome() {

    }

    private void setupViews()
    {
        loginBtn = findViewById(R.id.loginButton);
        email = findViewById(R.id.emailText);
        password = findViewById(R.id.passwordText);
        googleBtn = findViewById(R.id.signGoogle);
        mAuth = FirebaseAuth.getInstance();
    }

    public void goToSignUpActivity(View view) {
        signUpIntent = new Intent(Login.this, SignUpActivity.class);
        startActivity(signUpIntent);
        finish();
    }

    private void configureGoogle()
    {
        // Configure Google Sign In
      //  "339443214130-ji062htcmt87ovo1i1ps6ii4h0sg5s7i.apps.googleusercontent.com"
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("339443214130-ji062htcmt87ovo1i1ps6ii4h0sg5s7i.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase

                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                 e.printStackTrace();
                Toast.makeText(Login.this,e.getStackTrace().toString(),Toast.LENGTH_LONG).show();
                // Google Sign In failed, update UI appropriately

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Login.this,user.getEmail(),Toast.LENGTH_LONG).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Snackbar.make(findViewById(R.id.medLevel), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
