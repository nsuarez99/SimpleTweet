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

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    public interface DetailsOnClickListener{
        void onClick(int position);
    }

    public interface ProfileOnClickListener{
        void onClick(int position);
    }

    Context context;
    List<Tweet> tweets;
    DetailsOnClickListener detailsListener;
    ProfileOnClickListener profileListener;

    //Pass in the context and list of tweets
    public TweetAdapter(Context context, List<Tweet> tweets, DetailsOnClickListener detailsOnClickListener, ProfileOnClickListener profileListener){
        this.context = context;
        this.tweets = tweets;
        this.detailsListener = detailsOnClickListener;
        this.profileListener = profileListener;
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
        TextView tweetName;
        ImageView tweetEmbedImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tweetProfileImage = itemView.findViewById(R.id.detailProfileImage);
            tweetUserScreenName = itemView.findViewById(R.id.detailScreenName);
            tweetBody = itemView.findViewById(R.id.detailBody);
            tweetTime = itemView.findViewById(R.id.detailTime);
            tweetEmbedImage = itemView.findViewById(R.id.detailEmbedImage);
            tweetName = itemView.findViewById(R.id.detailName);
        }

        public void bind(Tweet tweet) {
            tweetBody.setText(tweet.body);
            tweetUserScreenName.setText("@" + tweet.user.screenName);
            tweetTime.setText(tweet.createdAt);
            tweetName.setText(tweet.user.name);
            Glide.with(context).load(tweet.user.publicImageUrl).transform(new RoundedCornersTransformation(65,5)).into(tweetProfileImage);

            if (tweet.embeddedImage != null){
                Log.i(Tweet.TAG, tweet.embeddedImage);
                Glide.with(context).load(Uri.parse(tweet.embeddedImage)).apply(new RequestOptions()
                        .placeholder(R.drawable.ic_vector_photo_stroke)).into(tweetEmbedImage);
                tweetEmbedImage.setVisibility(View.VISIBLE);
                tweetEmbedImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        detailsListener.onClick(getAdapterPosition());
                    }
                });
            }
            else{
                tweetEmbedImage.setVisibility(View.GONE);
            }

            setListeners();
        }

        public void setListeners(){
            tweetProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    profileListener.onClick(getAdapterPosition());
                }
            });

            tweetBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    detailsListener.onClick(getAdapterPosition());
                }
            });

            tweetUserScreenName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    detailsListener.onClick(getAdapterPosition());
                }
            });

            tweetTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    detailsListener.onClick(getAdapterPosition());
                }
            });

            tweetName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    detailsListener.onClick(getAdapterPosition());
                }
            });

            tweetEmbedImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    detailsListener.onClick(getAdapterPosition());
                }
            });
        }
    }
}
