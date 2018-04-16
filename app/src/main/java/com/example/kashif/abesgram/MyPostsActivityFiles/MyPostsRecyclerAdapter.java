package com.example.kashif.abesgram.MyPostsActivityFiles;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.kashif.abesgram.R;

import java.util.List;

/**
 * Created by kashif on 15/4/18.
 */

public class MyPostsRecyclerAdapter extends RecyclerView.Adapter<MyPostsRecyclerAdapter.ViewHolder> {


    public List<MyPostsModel> myPostsList;
    public Context context;

    public MyPostsRecyclerAdapter(List<MyPostsModel> myPostsList){

        this.myPostsList = myPostsList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_posts_list_item, parent, false);
        context = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        String image_url = myPostsList.get(position).getImage_url();
        String thumbUri = myPostsList.get(position).getImage_thumb();

        holder.setMyPostsImage(image_url, thumbUri);
    }

    @Override
    public int getItemCount() {
        return myPostsList.size();
    }



    //viewHolder for myPostsRecyclerAdapter
    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private ImageView myPostImageView;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }


        public void setMyPostsImage(String downloadUri, String thumbUri){

            myPostImageView = mView.findViewById(R.id.my_posts_image_imageview);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.image_placeholder);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
                    Glide.with(context).load(thumbUri)
            ).into(myPostImageView);

        }
    }
}
