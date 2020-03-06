package com.iti.intake40.tripguide.registration;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpPresenter implements SignUpContract.SignUpPresenter {
    private FirebaseAuth firebaseAuth;
    private SignUpActivity context;
    private boolean status = false;

    public SignUpPresenter(SignUpActivity context)
    {
        this.context = context;
    }
    @Override
    public boolean signUp(String mail, String password) {
        // Initialize Fire base Auth
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(context,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            status = true;
                        } else {
                            // If sign in fails, display a message to the user.
                            status = false;
                        }
                    }
                });
        return status;
    }
    @Override
    public void stop() {

    }
}
