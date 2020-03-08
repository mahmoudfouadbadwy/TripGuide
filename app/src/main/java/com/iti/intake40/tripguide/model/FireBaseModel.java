package com.iti.intake40.tripguide.model;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.iti.intake40.tripguide.login.Login;
import com.iti.intake40.tripguide.login.LoginContract;
import com.iti.intake40.tripguide.registration.SignUpActivity;
import com.iti.intake40.tripguide.registration.SignUpContract;


public class FireBaseModel {
    private FirebaseAuth firebaseAuth;
    public  void signUp(String email, String password,SignUpActivity _context,final SignUpContract.SignUpPresenter signUpPresenter)
    {

        // Initialize Fire base Auth
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(_context,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            signUpPresenter.onSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            signUpPresenter.onFailure();
                        }
                    }
                });
    }


    public void signIn(String email, String password,Login _context,final LoginContract.LoginPresenter loginPresenter)
    {
        // Initialize Fire base Auth
        firebaseAuth = FirebaseAuth.getInstance() ;
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(_context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                              loginPresenter.onSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                             loginPresenter.onFailure();
                        }
                    }
                });
    }
}
