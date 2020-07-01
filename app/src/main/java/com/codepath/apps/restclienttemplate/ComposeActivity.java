package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class ComposeActivity extends AppCompatActivity {

    public static final String TAG = "ComposeActivity";
    private static final int MAX_TWEET_LENGTH = 280;
    TextInputLayout composeTextLayout;
    EditText composeText;
    Button tweetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        composeText = findViewById(R.id.composeText);
        tweetButton = findViewById(R.id.tweetButton);

        //set max tweet length counter
        composeTextLayout = findViewById(R.id.composeTextLayout);
        composeTextLayout.setCounterMaxLength(MAX_TWEET_LENGTH);

        //set toolbar and composing action
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //Necessary to make user of onCreateOptionsMenu() and onPrepareOptionsMenu() which are only useful for older legacy stuff, not relevant for new apps.
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.closeIcon) {
                    Intent i = new Intent(ComposeActivity.this, TimeLineActivity.class);
                    startActivity(i);
                }
                return true;
            }
        });
    }

    // Publishes the text in the composeText view to Twitter
    public void publishTweet(View view) {
        String tweetContent = composeText.getText().toString();

        if (tweetContent.isEmpty()){
            Toast.makeText(ComposeActivity.this, "Tweet Cannot Be Empty", Toast.LENGTH_SHORT).show();
        }
        else if (tweetContent.length() > MAX_TWEET_LENGTH){
            Toast.makeText(ComposeActivity.this, "Tweet Is Too Long", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_SHORT).show();
        }
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.compose_menu, menu);
        return true;
    }
}