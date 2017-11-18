package com.example.kashif.abesgram.ProfileActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kashif.abesgram.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ProfileEditActivity extends AppCompatActivity {


    private String userUniqueUID;
    private String userName;
    private String userGender;
    private String userAge;
    private String userCourse;
    private String userCourseBranch;
    private String userCourseYear;
    private String userAboutMe;

    private EditText userName_et;
    private EditText userGender_et;
    private EditText userAge_et;
    private EditText userCourse_et;
    private EditText userCourseBranch_et;
    private EditText userCourseYear_et;
    private EditText userAboutMe_et;

    private Button saveCredentials_btn;

    private HashMap<String, Object> user_edit_profile_details;

    private DatabaseReference profileEditDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        getSupportActionBar().setTitle("Profile Edit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        userName_et = (EditText) findViewById(R.id.user_name_et);
        userGender_et = (EditText) findViewById(R.id.user_gender_et);
        userAge_et = (EditText) findViewById(R.id.user_age_et);
        userCourse_et = (EditText) findViewById(R.id.user_course_et);
        userCourseBranch_et = (EditText) findViewById(R.id.user_branch_et);
        userCourseYear_et = (EditText) findViewById(R.id.user_course_year_et);
        userAboutMe_et = (EditText) findViewById(R.id.user_about_me_et);

        saveCredentials_btn = (Button) findViewById(R.id.save_credentials_btn);

        Bundle Bundle = getIntent().getExtras();
        if (Bundle != null) {
            userUniqueUID = Bundle.getString("uniqueUserId");
            userName = Bundle.getString("username");
            userGender = Bundle.getString("user_gender");
            userAge = Bundle.getString("user_age");
            userCourse = Bundle.getString("user_course");
            userCourseBranch = Bundle.getString("user_course_branch");
            userCourseYear = Bundle.getString("user_course_year");
            userAboutMe = Bundle.getString("user_about_me");

        }
        // checking for blank entries

        if (userName != null){
            userName_et.setText(userName);
        }
        if (userGender != null){
            userGender_et.setText(userGender);
        }
        if (userAge != null){
            userAge_et.setText(userAge);
        }
        if (userCourse != null){
            userCourse_et.setText(userCourse);
        }
        if (userCourseBranch != null){
            userCourseBranch_et.setText(userCourseBranch);
        }
        if (userCourseYear != null){
            userCourseYear_et.setText(userCourseYear);
        }
        if (userAboutMe != null){
            userAboutMe_et.setText(userAboutMe);
        }

        saveCredentials_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfileDetails();
            }
        });
    }

    public void saveUserProfileDetails(){
        if (userName_et.getText().toString().length() == 0) {
            userName_et.setError("Name not entered");
            return;
        }
        else if (userGender_et.getText().toString().length() == 0){
            userGender_et.setError("Enter your Gender");
            return;
        }
        else if (userAge_et.getText().toString().length() == 0 ){
            userAge_et.setError("Please Enter Age");
            return;
        }
        else if (userCourse_et.getText().toString().length() == 0 ){
            userCourse_et.setError("Please Enter Branch");
            return;
        }
        else if (userCourseYear_et.getText().toString().length() == 0 ){
            userCourseYear_et.setError("Please Enter Course year");
            return;
        }
        else if (userAboutMe_et.getText().toString().length() == 0 ){
            userAboutMe_et.setError("Please About Yourself");
            return;
        }

        userName = userName_et.getText().toString();
        userGender = userGender_et.getText().toString().toUpperCase();
        userAge = userAge_et.getText().toString();
        userCourse = userCourse_et.getText().toString();
        userCourseBranch = userCourseBranch_et.getText().toString();
        userCourseYear = userCourseYear_et.getText().toString();
        userAboutMe = userAboutMe_et.getText().toString();


        user_edit_profile_details = new HashMap<>();
        user_edit_profile_details.put("Name", userName);
        user_edit_profile_details.put("Gender", userGender);
        user_edit_profile_details.put("Age", userAge);
        user_edit_profile_details.put("Course", userCourse);
        user_edit_profile_details.put("Branch", userCourseBranch);
        user_edit_profile_details.put("CourseYear", userCourseYear);
        user_edit_profile_details.put("AboutMe", userAboutMe);

        profileEditDatabaseReference = FirebaseDatabase.getInstance().getReference().child("allUserDetails").child(userUniqueUID);

        profileEditDatabaseReference.updateChildren(user_edit_profile_details);
        finish();
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
