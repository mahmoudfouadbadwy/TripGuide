package com.iti.intake40.tripguide.registration;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class SignUpPresenter implements SignUpContract.SignUpPresenter {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private UserProfileChangeRequest profileUpdates;
    private SignUpActivity context;
    private boolean status = false;

    public SignUpPresenter(SignUpActivity context)
    {
        this.context = context;
    }
    @Override
    public boolean signUp(String mail, String password, String name) {
        // Initialize Fire base Auth
        final String userName = name;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(context,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = firebaseAuth.getCurrentUser();
                            profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(userName)
                                    .build();
                            user.updateProfile(profileUpdates);
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
