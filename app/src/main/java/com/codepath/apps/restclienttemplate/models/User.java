package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class User {

    public String name;
    public String screenName;
    public String publicImageUrl;
    public String backgroundImageUrl;
    public String description;

    // Empty constructor needed for parceler library
    public User(){}

    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.name = jsonObject.getString("name");
        user.screenName = jsonObject.getString("screen_name");
        user.publicImageUrl = jsonObject.getString("profile_image_url_https");
        user.description = jsonObject.getString("description");
        try{
            user.backgroundImageUrl = jsonObject.getString("profile_banner_url");
        }
        catch (JSONException e){
            user.backgroundImageUrl = jsonObject.getString("profile_background_image_url_https");
        }

        return user;
    }

    public static List<User> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            users.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return users;
    }
}
