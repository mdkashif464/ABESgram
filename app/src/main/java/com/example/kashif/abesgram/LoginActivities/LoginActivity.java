package com.example.kashif.abesgram.LoginActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kashif.abesgram.MainActivity;
import com.example.kashif.abesgram.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnTouchListener {

    private EditText signIn_email_editText;
    private EditText signIn_password_editText;

    private Button signIn_button;
    private Button forgot_password_button;
    private Button signUp_button;

    private String userEmail;
    private String userPassword;

    private ProgressDialog logInProgressDialog;

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;

    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //initializing views
        signIn_email_editText = (EditText) this.findViewById(R.id.et_login_email);
        signIn_password_editText = (EditText) this.findViewById(R.id.et_login_password);
        signIn_button = (Button) this.findViewById(R.id.btn_signin);
        signUp_button = (Button) this.findViewById(R.id.btn_signup_at_login_page);
        forgot_password_button = (Button) this.findViewById(R.id.btn_forgot_password);

        logInProgressDialog = new ProgressDialog(this);
        logInProgressDialog.setMessage("Signing In...");
        logInProgressDialog.setCancelable(false);


        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        // hiding the keyboard on screen touch
        findViewById(R.id.main_layout).setOnTouchListener(this);


        signIn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }
        });

        forgot_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sentResetPasswordEmail();
            }
        });


        signUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening signUp Activity
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            if (currentUser.isEmailVerified()) {
                gotoWelcomeActivity();
            }
            else{
                Toast.makeText(LoginActivity.this, "Please verify your email and then SignIn", Toast.LENGTH_LONG).show();
                signOutIfUserNotVerified();
            }
        }
    }

    //method to signIn user
    public void signInUser(){
        userEmail = signIn_email_editText.getText().toString().trim();
        userPassword = signIn_password_editText.getText().toString().trim();

        if (TextUtils.isEmpty(userEmail)){
            signIn_email_editText.setError("Enter your email");
            return;
        }
        if (TextUtils.isEmpty(userPassword)){
            signIn_password_editText.setError("Enter your password");
            return;
        }
        else if(userPassword.length() < 6){
            signIn_password_editText.setError("should be atleast 6 digits");
            return;
        }

        /*else if(!userEmail.endsWith("@abes.ac.in")){
            signIn_email_editText.setError("Only ABESEC accounts are allowed");
            return;
        }*/

        logInProgressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    currentUser = firebaseAuth.getCurrentUser();
                    logInProgressDialog.cancel();
                    if (currentUser.isEmailVerified()) {
                        Toast.makeText(LoginActivity.this, "Successfully SignedIn", Toast.LENGTH_LONG).show();
                        gotoWelcomeActivity();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Verify your email before Signing In", Toast.LENGTH_LONG).show();
                        signOutIfUserNotVerified();
                    }
                }
                else {
                    logInProgressDialog.cancel();
                    Toast.makeText(LoginActivity.this, "Authentication failed:\n"+task.getException(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void gotoWelcomeActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void signOutIfUserNotVerified(){
        firebaseAuth.signOut();
    }

    public void sentResetPasswordEmail(){
        userEmail = signIn_email_editText.getText().toString().trim();

        if (TextUtils.isEmpty(userEmail)){
            signIn_email_editText.setError("Enter your email to reset password");
            return;
        }

        firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Password Reset Email sent", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
}
