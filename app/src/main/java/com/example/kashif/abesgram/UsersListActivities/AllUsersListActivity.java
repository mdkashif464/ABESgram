package com.example.kashif.abesgram.UsersListActivities;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kashif.abesgram.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllUsersListActivity extends AppCompatActivity {


    private RecyclerView all_users_list_recyclerView;
    private AllUserListRecyclerViewAdapter allUserListRecyclerViewAdapter;

    private ProgressDialog progressDialog;
    private Snackbar noUsersFoundMessageSnackbar;

    private ArrayList<AllUserListModel> allUserListModelArrayList;

    private static DatabaseReference userList_databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users_list);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Users..");

        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noUsersFoundMessageSnackbar = Snackbar.make(findViewById(R.id.all_users_list_layout),
                "currently no other user is registered.", Snackbar.LENGTH_INDEFINITE);

        all_users_list_recyclerView = (RecyclerView) findViewById(R.id.all_users_list_recyclerview);
        all_users_list_recyclerView.setLayoutManager(new LinearLayoutManager(this));


        getAllUserDetailsIntoArrayList();
    }

    public void getAllUserDetailsIntoArrayList(){

        showProgressDialog();
        allUserListModelArrayList = new ArrayList<AllUserListModel>();

        userList_databaseReference = FirebaseDatabase.getInstance().getReference().child("allUserDetails");
        userList_databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount() == 0){
                    noUsersFoundMessageSnackbar.show();
                    hideProgressDialog();
                }
                else {
                    noUsersFoundMessageSnackbar.dismiss();
                }

                int i = 0;
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    String user_name = childSnapshot.child("Name").getValue().toString();
                    String user_image_Url = childSnapshot.child("ProfileImage").getValue(String.class);
                    String user_uniqueId = childSnapshot.child("UniqueUserId").getValue().toString();
                    allUserListModelArrayList.add(i, new AllUserListModel(user_name, user_image_Url, user_uniqueId));
                    i++;
                }
                setAdapter(allUserListModelArrayList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setAdapter(ArrayList<AllUserListModel> allPlayersListModelArrayList){
        allUserListRecyclerViewAdapter = new AllUserListRecyclerViewAdapter(allPlayersListModelArrayList, this);
        all_users_list_recyclerView.setAdapter(allUserListRecyclerViewAdapter);
        hideProgressDialog();
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

    // method to show progress dialog while signing in
    public void showProgressDialog() {
        progressDialog.show();
    }

    // method to show progress dialog
    public void hideProgressDialog() {
        progressDialog.cancel();
    }
}
