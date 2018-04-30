package com.example.kashif.abesgram.LoginActivities;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kashif.abesgram.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity implements View.OnTouchListener {

    private EditText signUp_name_editText;
    private EditText signUp_email_editText;
    private EditText signUp_password_editText;

    private Button signUp_button;

    private String userName;
    private String userEmail;
    private String userPassword;

    private ProgressDialog signUpProgressDialog;

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setTitle("ABESgram SignUp");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initializing views
        signUp_name_editText = (EditText) this.findViewById(R.id.et_signup_name);
        signUp_email_editText = (EditText) this.findViewById(R.id.et_signup_email);
        signUp_password_editText = (EditText) this.findViewById(R.id.et_signup_password);
        signUp_button = (Button) this.findViewById(R.id.btn_signup);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //hiding the keyboard on screen touch
        findViewById(R.id.main_layout).setOnTouchListener(this);


        signUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });
    }

    // method to signUp user
    public void signUpUser(){

        userName = signUp_name_editText.getText().toString().trim();
        userEmail = signUp_email_editText.getText().toString().trim();
        userPassword = signUp_password_editText.getText().toString().trim();

        if (TextUtils.isEmpty(userName)){
            signUp_name_editText.setError("Name can not be blank");
            return;
        }

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
        /*else if(!userEmail.endsWith("@abes.ac.in")){
            signUp_email_editText.setError("Only ABESEC accounts are allowed");
            return;
        }*/

        signUpProgressDialog = new ProgressDialog(this);
        signUpProgressDialog.setMessage("Registering user...");
        signUpProgressDialog.show();
        signUpProgressDialog.setCancelable(false);

        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    currentUser = firebaseAuth.getCurrentUser();
                    final String currentUserId = currentUser.getUid();

                    currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                databaseReference.child("allUserDetails").child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        databaseReference.child("allUserDetails").child(currentUserId).child("UniqueUserId").setValue(currentUserId);
                                        databaseReference.child("allUserDetails").child(currentUserId).child("Name").setValue(userName);
                                        databaseReference.child("allUserDetails").child(currentUserId).child("Email").setValue(userEmail);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                        }
                    });


                    signUpProgressDialog.cancel();
                    Toast.makeText(SignUpActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
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



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
}
