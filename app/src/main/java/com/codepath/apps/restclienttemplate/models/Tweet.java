package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
public class Tweet {

    public static final String TAG = "Tweet";
    public String body;
    public String createdAt;
    public User user;
    public String embeddedImage;
    public long favoriteCount;
    public String id;
    // Empty constructor needed for parceler library
    public Tweet(){}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = Tweet.parseTime(jsonObject.getString("created_at"));
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.favoriteCount = jsonObject.getLong("favorite_count");
        tweet.id = jsonObject.getString("id_str");
        try{
            tweet.embeddedImage = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url");
            Log.i(TAG, "Has image: " + tweet.body);
        }
        catch (JSONException e){
        }

        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    // parses from Wed Oct 10 20:19:24 +0000 2018 to xx minutes/hours ago
    public static String parseTime(String rawJsonTime){
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        dateFormat.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = dateFormat.parse(rawJsonTime).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "parsing date error: " + e);
            relativeDate = rawJsonTime;
        }
        return relativeDate;

    }
}
