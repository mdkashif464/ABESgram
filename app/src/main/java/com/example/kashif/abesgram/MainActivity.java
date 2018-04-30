package com.example.kashif.abesgram;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kashif.abesgram.Fragments.AccountFragment;
import com.example.kashif.abesgram.Fragments.AddNewPostFragment;
import com.example.kashif.abesgram.Fragments.HomeFragment;
import com.example.kashif.abesgram.LoginActivities.LoginActivity;
import com.example.kashif.abesgram.MyPostsActivityFiles.MyPostsActivity;
import com.example.kashif.abesgram.UsersListActivities.AllUsersListActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CircleImageView nav_profile_image_iV;
    private TextView nav_username_tv;
    private TextView nav_useremail_tv;

    private DrawerLayout drawer;
    private BottomNavigationView mainbottomNav;

    private HomeFragment homeFragment;
    private AddNewPostFragment addNewPostFragment;
    private AccountFragment accountFragment;
    private Fragment currentFragment;

    public static boolean addedNewPost = false;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        nav_profile_image_iV = (CircleImageView) header.findViewById(R.id.nav_profile_image_imageView);
        nav_username_tv = (TextView) header.findViewById(R.id.nav_username_textview);
        nav_useremail_tv = (TextView) header.findViewById(R.id.nav_useremail_textView);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mainbottomNav = (BottomNavigationView) findViewById(R.id.mainBottomNav);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        // loading details into navigationView
        loadDetailsForNavigationView(FirebaseAuth.getInstance().getCurrentUser().getUid());

        // FRAGMENTS
        homeFragment = new HomeFragment();
        addNewPostFragment = new AddNewPostFragment();
        accountFragment = new AccountFragment();

        changeActionBarTitle("Home");
        initializeFragment();


        mainbottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_container);

                switch (item.getItemId()) {

                    case R.id.bottom_action_home: {
                        replaceFragment(homeFragment, currentFragment);
                        return true;
                    }

                    case R.id.bottom_action_account:

                        replaceFragment(accountFragment, currentFragment);
                        return true;

                    case R.id.bottom_action_add_new_post:

                        replaceFragment(addNewPostFragment, currentFragment);
                        return true;

                    default:
                        return false;


                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logoutMethod();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {

            case R.id.nav_home : {
                replaceFragment(homeFragment, currentFragment);
                mainbottomNav.setSelectedItemId(R.id.bottom_action_home);
                break;
            }

            case R.id.nav_myposts : {
                Intent intent = new Intent(MainActivity.this, MyPostsActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.nav_add_new_post : {
                replaceFragment(addNewPostFragment, currentFragment);
                mainbottomNav.setSelectedItemId(R.id.bottom_action_add_new_post);
                break;
            }

            case R.id.nav_myprofile : {
                replaceFragment(accountFragment, currentFragment);
                mainbottomNav.setSelectedItemId(R.id.bottom_action_account);
                break;
            }

            case R.id.nav_all_users_list : {
                Intent intent = new Intent(MainActivity.this, AllUsersListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_logout : {
                logoutMethod();
                break;
            }
        }

        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    // logout method
    public void logoutMethod(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // method to load NaviationView details
    public void loadDetailsForNavigationView(String uniqueUserId){
        databaseReference.child("allUserDetails").child(uniqueUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    nav_username_tv.setText(dataSnapshot.child("Name").getValue(String.class));
                    nav_useremail_tv.setText(dataSnapshot.child("Email").getValue(String.class));
                    String userImageUri = dataSnapshot.child("ProfileImage").getValue(String.class);
                    if (userImageUri != null){
                        Glide.with(MainActivity.this).load(userImageUri).into(nav_profile_image_iV);
                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, ""+databaseError,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeFragment(){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.main_container, homeFragment);
        fragmentTransaction.add(R.id.main_container, addNewPostFragment);
        fragmentTransaction.add(R.id.main_container, accountFragment);

        fragmentTransaction.hide(addNewPostFragment);
        fragmentTransaction.hide(accountFragment);

        fragmentTransaction.commit();

    }

    private void replaceFragment(Fragment fragment, Fragment currentFragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(fragment == homeFragment){

            changeActionBarTitle("Home");

            if (addedNewPost) {
                fragmentTransaction.remove(homeFragment);
                homeFragment = new HomeFragment();
                fragmentTransaction.add(R.id.main_container,homeFragment);
                addedNewPost = false;
            }

                fragmentTransaction.hide(accountFragment);
                fragmentTransaction.hide(addNewPostFragment);


        }

        if(fragment == accountFragment){

            changeActionBarTitle("My Profile");
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(addNewPostFragment);

        }

        if(fragment == addNewPostFragment){

            changeActionBarTitle("Add New Post");
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(accountFragment);

        }
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();

    }

    private void changeActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }
}
