package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
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

public class ProfileActivity extends AppCompatActivity {

    public static final String TAG = "ProfileActivity";
    TwitterClient client;
    RecyclerView profileList;
    ProfileAdapter adapter;
    List<User> users;
    ImageView profileImage;
    ImageView backgroundImage;
    Toolbar profileToolbar;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        client = TwitterApp.getRestClient(this);
        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

        profileImage = findViewById(R.id.profileImage);
        backgroundImage = findViewById(R.id.profileBackgorund);
        profileToolbar = findViewById(R.id.toolbarProfile);
        setSupportActionBar(profileToolbar);

        //Find the recycler view
        profileList = findViewById(R.id.profileList);
        //Initialize the list of tweets and a dapter
        users = new ArrayList<>();
        adapter = new ProfileAdapter(this, users);
        //Set up the recycler view
        profileList.setLayoutManager(new LinearLayoutManager(this));
        profileList.setAdapter(adapter);

        Glide.with(ProfileActivity.this).load(user.publicImageUrl).transform(new RoundedCornersTransformation(65,5)).into(profileImage);
        Glide.with(ProfileActivity.this).load(user.backgroundImageUrl).transform(new RoundedCornersTransformation(65,5)).into(backgroundImage);
    }

    //calls TwitterClient to send get request for user timeline and updates adapter and recycler view accordingly
    public void getFollowersList(View view) {
        client.getFollowers(user, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess");
                try {
                    adapter.clear();
                    adapter.addAll(User.fromJsonArray(json.jsonObject.getJSONArray("users")));
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

    //calls TwitterClient to send get request for user timeline and updates adapter and recycler view accordingly
    public void getFollowingList(View view) {
        client.getFollowing(user, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess");
                try {
                    adapter.clear();
                    adapter.addAll(User.fromJsonArray(json.jsonObject.getJSONArray("users")));
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