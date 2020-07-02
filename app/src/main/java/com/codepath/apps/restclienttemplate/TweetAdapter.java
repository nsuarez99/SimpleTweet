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
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    Context context;
    List<Tweet> tweets;

    //Pass in the context and list of tweets
    public TweetAdapter(Context context, List<Tweet> tweets){
        this.context = context;
        this.tweets = tweets;
    }


    //For each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }


    //Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Get the data at the position
        Tweet tweet = tweets.get(position);
        //Bind the tweet with the view holder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of tweets -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }


    //Define the viewholder
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView tweetProfileImage;
        TextView tweetUserScreenName;
        TextView tweetBody;
        TextView tweetTime;
        ImageView tweetEmbedImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tweetProfileImage = itemView.findViewById(R.id.tweetProfileImage);
            tweetUserScreenName = itemView.findViewById(R.id.tweetUserScreenName);
            tweetBody = itemView.findViewById(R.id.tweetBody);
            tweetTime = itemView.findViewById(R.id.tweetTime);
            tweetEmbedImage = itemView.findViewById(R.id.tweetEmbedImage);
        }

        public void bind(Tweet tweet) {
            tweetBody.setText(tweet.body);
            tweetUserScreenName.setText(tweet.user.screenName);
            tweetTime.setText(tweet.createdAt);
            Glide.with(context).load(tweet.user.publicImageUrl).into(tweetProfileImage);

            if (tweet.embeddedImage != null){
                Log.i(Tweet.TAG, tweet.embeddedImage);
                Glide.with(context).load(Uri.parse(tweet.embeddedImage)).apply(new RequestOptions()
                        .placeholder(R.drawable.ic_vector_photo_stroke)).into(tweetEmbedImage);
                tweetEmbedImage.setVisibility(View.VISIBLE);
            }
            else{
                tweetEmbedImage.setVisibility(View.GONE);
            }
        }
    }
}
