package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>{

    Context context;
    List<User> users;

    //Pass in the context and list of tweets
    public ProfileAdapter(Context context, List<User> users){
        this.context = context;
        this.users = users;
    }

    //For each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false);
        return new ViewHolder(view);
    }


    //Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Get the data at the position
        User user = users.get(position);
        //Bind the tweet with the view holder
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        users.clear();
        notifyDataSetChanged();
    }

    // Add a list of users -- change to type used
    public void addAll(List<User> list) {
        users.addAll(list);
        notifyDataSetChanged();
    }


    //Define the viewholder
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profileListImage;
        TextView profileListDescription;
        TextView profileListName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileListImage = itemView.findViewById(R.id.profileListImage);
            profileListDescription = itemView.findViewById(R.id.profileListDescription);
            profileListName = itemView.findViewById(R.id.profileListName);
        }

        public void bind(User user) {
            profileListName.setText(user.screenName);
            profileListDescription.setText(user.description);
            Glide.with(context).load(user.publicImageUrl).transform(new RoundedCornersTransformation(65,5)).into(profileListImage);
        }
    }
}
