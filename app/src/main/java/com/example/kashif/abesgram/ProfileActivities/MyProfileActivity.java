package com.example.kashif.abesgram.ProfileActivities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.kashif.abesgram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity {

    private FloatingActionButton profile_edit_fab;

    private CircleImageView user_profile_image_iv;

    private TextView username_tv;
    private TextView useremail_tv;
    private TextView user_gender_age_tv;
    private TextView user_course_tv;
    private TextView user_branch_tv;
    private TextView user_course_year_tv;
    private TextView user_about_me_tv;

    private String userName;
    private String userProfileUrl;
    private String userEmail;
    private String userGender;
    private String userAge;
    private String userCourse;
    private String userCourseBranch;
    private String userCourseYear;
    private String userAboutMe;

    private String receivedUserUniqueID;
    private String currentUserUniqueID;

    private DatabaseReference profileDatabaseReference;

    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        getSupportActionBar().setTitle("User Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initializing views
        profile_edit_fab = (FloatingActionButton) findViewById(R.id.profile_edit_fab);

        user_profile_image_iv = (CircleImageView) findViewById(R.id.user_profile_image_imagevieww);
        username_tv = (TextView) findViewById(R.id.user_profile_name_tv);
        useremail_tv = (TextView) findViewById(R.id.user_profile_email_tv);
        user_gender_age_tv = (TextView) findViewById(R.id.user_gender_age_tv);
        user_course_tv = (TextView) findViewById(R.id.user_course_tv);
        user_branch_tv = (TextView) findViewById(R.id.user_branch_tv);
        user_course_year_tv = (TextView) findViewById(R.id.user_course_year_tv);
        user_about_me_tv = (TextView) findViewById(R.id.user_about_me_tv);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserUniqueID = currentUser.getUid();

        Intent intent = getIntent();
        receivedUserUniqueID = intent.getExtras().getString("UserUniqueID");

        if (receivedUserUniqueID.equals(currentUserUniqueID)){
               // profile_edit_fab.setVisibility(View.VISIBLE);
            }

        profileDatabaseReference = FirebaseDatabase.getInstance().getReference().child("allUserDetails").child(receivedUserUniqueID);


        profileDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName = dataSnapshot.child("Name").getValue(String.class);
                userProfileUrl = dataSnapshot.child("ProfileImage").getValue(String.class);
                userEmail = dataSnapshot.child("Email").getValue(String.class);
                userGender = dataSnapshot.child("Gender").getValue(String.class);
                userAge = dataSnapshot.child("Age").getValue(String.class);
                userCourse = dataSnapshot.child("Course").getValue(String.class);
                userCourseBranch = dataSnapshot.child("Branch").getValue(String.class);
                userCourseYear = dataSnapshot.child("CourseYear").getValue(String.class);
                userAboutMe = dataSnapshot.child("AboutMe").getValue(String.class);

                useremail_tv.setText(userEmail);
                if (userName == null){
                  //  goToProfileEditActivity();
                }

                username_tv.setText(userName);

                RequestOptions placeholderOption = new RequestOptions();
                placeholderOption.placeholder(R.drawable.ic_person_black_24dp);
                Glide.with(MyProfileActivity.this).applyDefaultRequestOptions(placeholderOption).load(userProfileUrl).into(user_profile_image_iv);

                user_gender_age_tv.setText("Gender: "+userGender+" | Age: "+userAge);
                user_course_tv.setText(userCourse);
                user_branch_tv.setText(userCourseBranch);
                user_course_year_tv.setText(userCourseYear);
                user_about_me_tv.setText(userAboutMe);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // method on floating action button
        profile_edit_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfileEditActivity();
            }
        });
    }

    // method to go to profile edit activity
    public void goToProfileEditActivity(){
        Intent edit_your_profile_intent = new Intent(MyProfileActivity.this, ProfileEditActivity.class);
                edit_your_profile_intent.putExtra("username", userName)
                        .putExtra("uniqueUserId", currentUser.getUid())
                        .putExtra("user_gender", userGender)
                        .putExtra("user_age", userAge)
                        .putExtra("user_course", userCourse)
                        .putExtra("user_course_branch", userCourseBranch)
                        .putExtra("user_course_year", userCourseYear)
                        .putExtra("user_about_me", userAboutMe);

        startActivity(edit_your_profile_intent);
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
