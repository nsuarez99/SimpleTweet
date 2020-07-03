package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class DetailActivity extends AppCompatActivity {

    public static final String TAG = "DetailActivity";
    TwitterClient client;
    ImageView detailProfileImage;
    TextView detailUserScreenName;
    TextView detailBody;
    TextView detailTime;
    TextView detailName;
    ImageView detailEmbedImage;
    ToggleButton favButton;
    TextView favCount;
    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        client = TwitterApp.getRestClient(this);

        detailProfileImage = findViewById(R.id.detailProfileImage);
        detailUserScreenName = findViewById(R.id.detailScreenName);
        detailBody = findViewById(R.id.detailBody);
        detailTime = findViewById(R.id.detailTime);
        detailName = findViewById(R.id.detailName);
        detailEmbedImage = findViewById(R.id.detailEmbedImage);
        favButton = findViewById(R.id.favButton);
        favCount = findViewById(R.id.favoriteCount);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        //set texts
        detailUserScreenName.setText(tweet.user.screenName);
        detailName.setText(tweet.user.name);
        detailBody.setText(tweet.body);
        detailTime.setText(tweet.createdAt);
        favCount.setText("" + tweet.favoriteCount);


        //set profile image
        Glide.with(DetailActivity.this).load(tweet.user.publicImageUrl).transform(new RoundedCornersTransformation(65,5)).into(detailProfileImage);

        //set embeddedImage
        if (tweet.embeddedImage != null){
            Log.i(Tweet.TAG, tweet.embeddedImage);
            Glide.with(DetailActivity.this).load(Uri.parse(tweet.embeddedImage)).apply(new RequestOptions()
                    .placeholder(R.drawable.ic_vector_photo_stroke)).into(detailEmbedImage);
            detailEmbedImage.setVisibility(View.VISIBLE);
        }
        else{
            detailEmbedImage.setVisibility(View.GONE);
        }
    }

    public void updateFavorite(View view){
        if (favButton.isChecked()){
            client.addFavorite(tweet.id, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess addFavorite");
                    tweet.favoriteCount += 1;
                    favCount.setText("" + tweet.favoriteCount);
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "onFailure addFavorite");
                }
            });
        }
        else{
            client.deleteFavorite(tweet.id, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess deleteFavorite");
                    tweet.favoriteCount -= 1;
                    favCount.setText("" + tweet.favoriteCount);
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "onFailure deleteFavorite");
                }
            });
        }
    }
}