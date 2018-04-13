package com.example.kashif.abesgram.ProfileActivities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kashif.abesgram.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class ProfileEditActivity extends AppCompatActivity {


    private String userUniqueUID;
    private String userProfileUri;
    private String userName;
    private String userGender;
    private String userAge;
    private String userCourse;
    private String userCourseBranch;
    private String userCourseYear;
    private String userAboutMe;

    private CircleImageView userProfile_iV;
    private EditText userName_et;
    private EditText userGender_et;
    private EditText userAge_et;
    private EditText userCourse_et;
    private EditText userCourseBranch_et;
    private EditText userCourseYear_et;
    private EditText userAboutMe_et;

    private Button saveCredentials_btn;

    private Uri mainImageURI = null;
    private Uri download_uri;
    private Bitmap compressedImageFile;

    private HashMap<String, Object> user_edit_profile_details;

    private DatabaseReference profileEditDatabaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        getSupportActionBar().setTitle("Profile Edit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        userProfile_iV = (CircleImageView) findViewById(R.id.user_profile_image_imageview);
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
            userProfileUri = Bundle.getString("user_profile_uri");
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
        if (userProfileUri != null){
            Glide.with(ProfileEditActivity.this).load(userProfileUri).into(userProfile_iV);
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


        userProfile_iV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if(ContextCompat.checkSelfPermission(ProfileEditActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                        Toast.makeText(ProfileEditActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(ProfileEditActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {

                        BringImagePicker();

                    }

                } else {

                    BringImagePicker();

                }
            }
        });

        saveCredentials_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfileDetails();
            }
        });
    }


    // saving all the user entered details
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


        // getting value from all the fields
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


        // uploading user profile image
        if (mainImageURI != null){
            File newImageFile = new File(mainImageURI.getPath());
            try {

                compressedImageFile = new Compressor(ProfileEditActivity.this)
                        .setMaxHeight(125)
                        .setMaxWidth(125)
                        .setQuality(50)
                        .compressToBitmap(newImageFile);

            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] thumbData = baos.toByteArray();

            storageReference = FirebaseStorage.getInstance().getReference();

            UploadTask image_path = storageReference.child("profile_images").child(userUniqueUID + ".jpg").putBytes(thumbData);

            image_path.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    download_uri = task.getResult().getDownloadUrl();
                    if (task.isSuccessful()) {
                        updateUserDetails(download_uri.toString());

                    } else {

                        String error = task.getException().getMessage();
                        Toast.makeText(ProfileEditActivity.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();


                    }

                }
            });
        }
        else {
            updateUserDetails(null);
        }

    }

    // updating details into firebase database
    private void updateUserDetails(String imageUri){

        if (imageUri != null){
            user_edit_profile_details.put("ProfileImage", imageUri);
        }

        profileEditDatabaseReference = FirebaseDatabase.getInstance().getReference().child("allUserDetails").child(userUniqueUID);
        profileEditDatabaseReference.updateChildren(user_edit_profile_details);
        finish();
    }


    private void BringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(ProfileEditActivity.this);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                userProfile_iV.setImageURI(mainImageURI);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

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
