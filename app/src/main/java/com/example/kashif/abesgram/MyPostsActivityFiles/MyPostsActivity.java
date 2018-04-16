package com.example.kashif.abesgram.MyPostsActivityFiles;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import com.example.kashif.abesgram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyPostsActivity extends AppCompatActivity {

    private RecyclerView my_posts_list_recycler_view;
    private List<MyPostsModel> my_posts_list;

    private FirebaseFirestore firebaseFirestore;
    private String current_user_id;

    private MyPostsRecyclerAdapter myPostsRecyclerAdapter;

    private DocumentSnapshot lastVisible;
    private Boolean isFirstPageFirstLoad = true;

    private Snackbar noPostsFoundMessageSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        getSupportActionBar().setTitle("My Posts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        my_posts_list = new ArrayList<>();
        my_posts_list_recycler_view = findViewById(R.id.my_posts_recycler_view);

        noPostsFoundMessageSnackbar = Snackbar.make(findViewById(R.id.activity_my_posts),
                "You have not uploaded any post yet!", Snackbar.LENGTH_INDEFINITE);


        myPostsRecyclerAdapter = new MyPostsRecyclerAdapter(my_posts_list);
        my_posts_list_recycler_view.setLayoutManager(new GridLayoutManager(this,3, LinearLayoutManager.VERTICAL,false));
        my_posts_list_recycler_view.setAdapter(myPostsRecyclerAdapter);
        my_posts_list_recycler_view.setHasFixedSize(true);



        firebaseFirestore = FirebaseFirestore.getInstance();
        current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        my_posts_list_recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Boolean reachedBottom = !recyclerView.canScrollVertically(1);

                if(reachedBottom){

                    loadMorePost();

                }

            }
        });


        Query firstQuery = firebaseFirestore.collection("EachUserPosts/"+current_user_id+"/MyPosts").orderBy("timestamp", Query.Direction.DESCENDING).limit(3);
        firstQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (!documentSnapshots.isEmpty()) {

                    noPostsFoundMessageSnackbar.dismiss();
                    if (isFirstPageFirstLoad) {

                        lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                        my_posts_list.clear();

                    }

                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            String blogPostId = doc.getDocument().getId();
                            MyPostsModel myPostsModel = doc.getDocument().toObject(MyPostsModel.class);

                            if (isFirstPageFirstLoad) {

                                my_posts_list.add(myPostsModel);

                            } else {

                                my_posts_list.add(0, myPostsModel);

                            }



                            myPostsRecyclerAdapter.notifyDataSetChanged();

                        }
                    }

                    isFirstPageFirstLoad = false;

                }
                else {
                    noPostsFoundMessageSnackbar.show();
                }

            }

        });


    }

    public void loadMorePost(){

        Query nextQuery = firebaseFirestore.collection("EachUserPosts/"+current_user_id+"/MyPosts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .startAfter(lastVisible)
                .limit(3);

        nextQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (!documentSnapshots.isEmpty()) {

                    lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            String blogPostId = doc.getDocument().getId();
                            MyPostsModel myPostsModel = doc.getDocument().toObject(MyPostsModel.class);
                            my_posts_list.add(myPostsModel);


                            myPostsRecyclerAdapter.notifyDataSetChanged();
                        }

                    }
                }

            }
        });

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
