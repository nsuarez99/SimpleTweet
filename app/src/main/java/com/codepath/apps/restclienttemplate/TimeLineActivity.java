package com.codepath.apps.restclienttemplate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class TimeLineActivity extends AppCompatActivity {

    private static final String TAG = "TimeLineActivity";
    public static final int REQUEST_CODE = 20;

    TwitterClient client;
    RecyclerView tweetList;
    TweetAdapter adapter;
    List<Tweet> tweets;
    SwipeRefreshLayout swipeContainer;
    User myUser;
    ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        client = TwitterApp.getRestClient(this);

        //set user profile image
        profileImage = findViewById(R.id.timelineMyProfile);
        setUser();

        //set refresh colors and actions
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "fetching new data!");
                populateHomeTimeLine();
            }
        });

        //set tweet adapter click listeners
        TweetAdapter.DetailsOnClickListener detailClickListener = new TweetAdapter.DetailsOnClickListener() {
            @Override
            public void onClick(int position) {
                Tweet tweet = tweets.get(position);
                Intent i = new Intent(TimeLineActivity.this, DetailActivity.class);
                i.putExtra("tweet", Parcels.wrap(tweet));
                startActivity(i);
            }
        };

        TweetAdapter.ProfileOnClickListener profileClickListener = new TweetAdapter.ProfileOnClickListener() {
            @Override
            public void onClick(int position) {
                User user = tweets.get(position).user;
                Intent i = new Intent(TimeLineActivity.this, ProfileActivity.class);
                i.putExtra("user", Parcels.wrap(user));
                startActivity(i);
            }
        };

        //Find the recycler view
        tweetList = findViewById(R.id.tweetList);
        //Initialize the list of tweets and a adapter
        tweets = new ArrayList<>();
        adapter = new TweetAdapter(this, tweets, detailClickListener, profileClickListener);
        //Set up the recycler view
        tweetList.setLayoutManager(new LinearLayoutManager(this));
        tweetList.setAdapter(adapter);
        populateHomeTimeLine();


        //set toolbar
        Toolbar toolbar = findViewById(R.id.timelineToolbar);
        setSupportActionBar(toolbar); //Necessary to make user of onCreateOptionsMenu() and onPrepareOptionsMenu() which are only useful for older legacy stuff, not relevant for new apps.


        //set toolbar composing action
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.composeIcon) {
                    Log.i(TAG, "composition has been clicked");
                    Intent i = new Intent(TimeLineActivity.this, ComposeActivity.class);
                    startActivityForResult(i, REQUEST_CODE);
                }
                return true;
            }
        });
    }

    //sets user and userImage
    public void setUser(){
        client.getMyUser(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    myUser = User.fromJson(json.jsonObject);
                    Glide.with(TimeLineActivity.this).load(myUser.publicImageUrl).transform(new RoundedCornersTransformation(80,5)).placeholder(R.drawable.ic_vector_photo).into(profileImage);
                } catch (JSONException e) {
                    Log.e(TAG, "getting myUser failure: " + e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "getting onFailure");
            }
        });
    }


    public void onClickToProfile(View view){
        Intent i = new Intent(TimeLineActivity.this, ProfileActivity.class);
        i.putExtra("user", Parcels.wrap(myUser));
        startActivity(i);
    }

    //Gets the tweet from ComposeActivity and adds it to the recyclerView and notifies adapter
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra(ComposeActivity.TWEET));
            Log.i(TAG, "have returned tweet: " + tweet);

            final int insertPosition = 0;
            tweets.add(insertPosition, tweet);
            adapter.notifyItemInserted(insertPosition);
            tweetList.smoothScrollToPosition(insertPosition);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline_menu, menu);
        return true;
    }

    //calls TwitterClient to send get request for user timeline and updates adapter and recycler view accordingly
    private void populateHomeTimeLine() {
        client.getHomeTimeLine(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess");
                try {
                    adapter.clear();
                    adapter.addAll(Tweet.fromJsonArray(json.jsonArray));
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    Log.e(TAG, "Json exception: " + e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure: " + response, throwable);
            }
        });
    }
}