package com.example.kashif.abesgram.UsersListActivities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.kashif.abesgram.ProfileActivities.MyProfileActivity;
import com.example.kashif.abesgram.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kashif on 29/10/17.
 */

public class AllUserListRecyclerViewAdapter extends RecyclerView.Adapter<AllUserListRecyclerViewAdapter.AllUserListViewHolder> {


    private ArrayList<AllUserListModel> allUserListModelArrayList = new ArrayList<AllUserListModel>();
    private Context context;

    public AllUserListRecyclerViewAdapter(ArrayList<AllUserListModel> allUserListModelArrayList, Context context){
        this.allUserListModelArrayList = allUserListModelArrayList;
        this.context = context;
    }

    @Override
    public AllUserListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.all_user_list_recyclerview_layout, parent, false);
        return new AllUserListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AllUserListViewHolder holder, final int position) {

        holder.setUserName(allUserListModelArrayList.get(position).getUserName());
        holder.setPlayerProfileImage(allUserListModelArrayList.get(position).getUserImageUrl());
        holder.viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "will be applied later", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, MyProfileActivity.class)
                        .putExtra("UserUniqueID", allUserListModelArrayList.get(position).getUserUniqueId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allUserListModelArrayList.size();
    }

    public class AllUserListViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView userProfileImageView;
        private TextView userNameTextView;
        public Button viewProfileButton;

        public AllUserListViewHolder(View itemView) {
            super(itemView);

            userProfileImageView = (CircleImageView) itemView.findViewById(R.id.all_user_list_image_iv);
            userNameTextView = (TextView) itemView.findViewById(R.id.all_user_list_name_tv);
            viewProfileButton = (Button) itemView.findViewById(R.id.all_user_list_view_profile_btn);
        }

        public void setUserName(String name) {
            userNameTextView.setText(name);
        }

        public void setPlayerProfileImage(String profileImageUrl) {
            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.ic_person_black_24dp);

            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(profileImageUrl).into(userProfileImageView);

        }
    }
}
