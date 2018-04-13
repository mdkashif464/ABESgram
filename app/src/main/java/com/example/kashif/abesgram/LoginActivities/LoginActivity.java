package com.example.kashif.abesgram.LoginActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

public class LoginActivity extends AppCompatActivity {

    private EditText signIn_email_editText;
    private EditText signIn_password_editText;

    private Button signIn_button;
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

        logInProgressDialog = new ProgressDialog(this);
        logInProgressDialog.setMessage("Signing In...");
        logInProgressDialog.setCancelable(false);


        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();


        signIn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
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
        if (currentUser != null){
            gotoWelcomeActivity();
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

        logInProgressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    logInProgressDialog.cancel();
                    Toast.makeText(LoginActivity.this, "Successfully SignedIn", Toast.LENGTH_LONG).show();
                    gotoWelcomeActivity();
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
}
