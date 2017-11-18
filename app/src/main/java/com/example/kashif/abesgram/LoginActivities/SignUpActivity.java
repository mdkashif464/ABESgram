package com.example.kashif.abesgram.LoginActivities;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kashif.abesgram.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText signUp_email_editText;
    private EditText signUp_password_editText;

    private Button signUp_button;

    private String userEmail;
    private String userPassword;

    private ProgressDialog signUpProgressDialog;

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setTitle("ABESgram SignUp");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initializing views
        signUp_email_editText = (EditText) this.findViewById(R.id.et_signup_email);
        signUp_password_editText = (EditText) this.findViewById(R.id.et_signup_password);
        signUp_button = (Button) this.findViewById(R.id.btn_signup);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();


        signUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });
    }

    // method to signUp user
    public void signUpUser(){
        userEmail = signUp_email_editText.getText().toString().trim();
        userPassword = signUp_password_editText.getText().toString().trim();

        if (TextUtils.isEmpty(userEmail)){
            signUp_email_editText.setError("Enter your email");
            return;
        }
        if (TextUtils.isEmpty(userPassword)){
            signUp_password_editText.setError("Enter your password");
            return;
        }
        else if(userPassword.length() < 6){
            signUp_password_editText.setError("should be atleast 6 digits");
            return;
        }

        signUpProgressDialog = new ProgressDialog(this);
        signUpProgressDialog.setMessage("Registering user...");
        signUpProgressDialog.show();
        signUpProgressDialog.setCancelable(false);

        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    signUpProgressDialog.cancel();
                    Toast.makeText(SignUpActivity.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    signUpProgressDialog.cancel();
                    Toast.makeText(SignUpActivity.this, "Registeration failed:\n"+task.getException(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home : {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
