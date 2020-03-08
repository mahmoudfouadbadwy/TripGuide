package com.iti.intake40.tripguide.registration;
import com.iti.intake40.tripguide.model.FireBaseModel;

public class SignUpPresenter implements SignUpContract.SignUpPresenter {
    private SignUpActivity context;
    private FireBaseModel model;
    public SignUpPresenter(SignUpActivity context)
    {
        this.context = context;
        model = new FireBaseModel();
    }
    @Override
    public void signUp(String mail,String password) {
              model.signUp(mail,password,context,this);
    }

    @Override
    public void onSuccess() {
           context.goToHome();
    }

    @Override
    public void onFailure() {
          context.displayMessage("Email Already Exist");
    }

    @Override
    public void stop() {
        context = null;
        model = null;
    }
}
