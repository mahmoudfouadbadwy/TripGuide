package com.iti.intake40.tripguide.login;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPresenter implements LoginContract.LoginPresenter {
    private  Login login;
    private FirebaseAuth mAuth;
    public LoginPresenter(Login login)
    {
        this.login=login;
    }

    @Override
    public void loginWithEmail(String email, String password)
    {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance() ;
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(login, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            login.goToHome();
                        } else {
                            // If sign in fails, display a message to the user.
                            login.displayError("Invalid Email or PassWord");
                        }
                    }
                });
    }

    @Override
    public void stop()
    {
        login = null;
        mAuth = null;
    }
}
