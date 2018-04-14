package com.example.kashif.abesgram.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.kashif.abesgram.ProfileActivities.ProfileEditActivity;
import com.example.kashif.abesgram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {


    private FloatingActionButton profile_edit_fab;

    //private ImageView user_profile_image_iv;
    private CircleImageView user_profile_image_iv;

    private TextView username_tv;
    private TextView useremail_tv;
    private TextView user_gender_age_tv;
    private TextView user_course_tv;
    private TextView user_branch_tv;
    private TextView user_course_year_tv;
    private TextView user_about_me_tv;

    private String userName;
    private String userImageUri;
    private String userEmail;
    private String userGender;
    private String userAge;
    private String userCourse;
    private String userCourseBranch;
    private String userCourseYear;
    private String userAboutMe;

    private String currentUserUniqueID;

    private DatabaseReference profileDatabaseReference;

    private FirebaseUser currentUser;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);


        // initializing views
        profile_edit_fab = (FloatingActionButton) view.findViewById(R.id.profile_edit_fab);

        user_profile_image_iv = (CircleImageView) view.findViewById(R.id.user_profile_image_imageview);
        username_tv = (TextView) view.findViewById(R.id.user_profile_name_tv);
        useremail_tv = (TextView) view.findViewById(R.id.user_profile_email_tv);
        user_gender_age_tv = (TextView) view.findViewById(R.id.user_gender_age_tv);
        user_course_tv = (TextView) view.findViewById(R.id.user_course_tv);
        user_branch_tv = (TextView) view.findViewById(R.id.user_branch_tv);
        user_course_year_tv = (TextView) view.findViewById(R.id.user_course_year_tv);
        user_about_me_tv = (TextView) view.findViewById(R.id.user_about_me_tv);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserUniqueID = currentUser.getUid();


        profileDatabaseReference = FirebaseDatabase.getInstance().getReference().child("allUserDetails").child(currentUserUniqueID);


        profileDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName = dataSnapshot.child("Name").getValue(String.class);
                userImageUri = dataSnapshot.child("ProfileImage").getValue(String.class);
                userEmail = dataSnapshot.child("Email").getValue(String.class);
                userGender = dataSnapshot.child("Gender").getValue(String.class);
                userAge = dataSnapshot.child("Age").getValue(String.class);
                userCourse = dataSnapshot.child("Course").getValue(String.class);
                userCourseBranch = dataSnapshot.child("Branch").getValue(String.class);
                userCourseYear = dataSnapshot.child("CourseYear").getValue(String.class);
                userAboutMe = dataSnapshot.child("AboutMe").getValue(String.class);

                username_tv.setText(userName);
                useremail_tv.setText(userEmail);
                if (userAge == null){
                    goToProfileEditActivity();
                }
                else {

                    RequestOptions placeholderOption = new RequestOptions();
                    placeholderOption.placeholder(R.drawable.ic_person_black_24dp);
                    Glide.with(getActivity()).applyDefaultRequestOptions(placeholderOption).load(userImageUri).into(user_profile_image_iv);
                    user_gender_age_tv.setText("Gender: " + userGender + " | Age: " + userAge);
                    user_course_tv.setText(userCourse);
                    user_branch_tv.setText(userCourseBranch);
                    user_course_year_tv.setText(userCourseYear);
                    user_about_me_tv.setText(userAboutMe);
                }
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


        return view;
    }

    // method to go to profile edit activity
    public void goToProfileEditActivity(){
        Intent edit_your_profile_intent = new Intent(getActivity(), ProfileEditActivity.class);
        edit_your_profile_intent.putExtra("username", userName)
                .putExtra("uniqueUserId", currentUser.getUid())
                .putExtra("user_gender", userGender)
                .putExtra("user_profile_uri", userImageUri)
                .putExtra("user_age", userAge)
                .putExtra("user_course", userCourse)
                .putExtra("user_course_branch", userCourseBranch)
                .putExtra("user_course_year", userCourseYear)
                .putExtra("user_about_me", userAboutMe);

        startActivity(edit_your_profile_intent);
    }

}
