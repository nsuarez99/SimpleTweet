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
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.github.scribejava.apis.TwitterApi;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimeLineActivity extends AppCompatActivity {

    private static final String TAG = "TimeLineActivity";
    public static final int REQUEST_CODE = 20;

    TwitterClient client;
    RecyclerView rvTweets;
    TweetAdapter adapter;
    List<Tweet> tweets;
    SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        client = TwitterApp.getRestClient(this);

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

        //Find the recycler view
        rvTweets = findViewById(R.id.rvTweets);
        //Initialize the list of tweets and a dapter
        tweets = new ArrayList<>();
        adapter = new TweetAdapter(this, tweets);
        //Set up the recycler view
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(adapter);
        populateHomeTimeLine();


        //set toolbar and composing action
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //Necessary to make user of onCreateOptionsMenu() and onPrepareOptionsMenu() which are only useful for older legacy stuff, not relevant for new apps.
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

    //Gets the tweet from ComposeActivity and adds it to the recyclerView and notifies adapter
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra(ComposeActivity.TWEET));
            Log.i(TAG, "have returned tweet: " + tweet);

            final int insertPosition = 0;
            tweets.add(insertPosition, tweet);
            adapter.notifyItemInserted(insertPosition);
            rvTweets.smoothScrollToPosition(insertPosition);
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