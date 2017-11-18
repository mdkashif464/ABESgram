package com.example.kashif.abesgram;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.kashif.abesgram.LoginActivities.LoginActivity;
import com.example.kashif.abesgram.ProfileActivities.MyProfileActivity;
import com.example.kashif.abesgram.UsersListActivities.AllUsersListActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String uniqueUserId;
    private String userEmail;

    private TextView nav_username_tv;
    private TextView nav_useremail_tv;

    private DrawerLayout drawer;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        nav_username_tv = (TextView) header.findViewById(R.id.nav_username_textview);
        nav_useremail_tv = (TextView) header.findViewById(R.id.nav_useremail_textView);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        // getting the uniqueUserId from login Activity
        Intent intent = getIntent();
        uniqueUserId = intent.getExtras().getString("uniqueUID");
        userEmail = intent.getExtras().getString("userEmail");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        // creating entry into database if logging for the first time
        createDatabaseEntryForNewUser(uniqueUserId);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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

            case R.id.nav_myprofile : {
                // Handle the profile action
                Intent intent = new Intent(MainActivity.this, MyProfileActivity.class)
                        .putExtra("UserUniqueID", uniqueUserId);
                startActivity(intent);
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
        return true;
    }

    // logout method
    public void logoutMethod(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // method to create database entry for new user
    public void createDatabaseEntryForNewUser(final String uniqueUserId){
        databaseReference.child("allUserDetails").child(uniqueUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0){
                    databaseReference.child("allUserDetails").child(uniqueUserId).child("UniqueUserId").setValue(uniqueUserId);
                    databaseReference.child("allUserDetails").child(uniqueUserId).child("Email").setValue(userEmail);
                }
                else {
                    nav_username_tv.setText(dataSnapshot.child("Name").getValue(String.class));
                    nav_useremail_tv.setText(dataSnapshot.child("Email").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, ""+databaseError,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
