package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;



public class ComposeActivity extends AppCompatActivity {

    BirdieClient client;
    EditText etCompose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar);
        setContentView(R.layout.activity_compose);
        etCompose = (EditText) findViewById(R.id.etCompose);

        /*
        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);
        final String youTubeKey = getIntent().getStringExtra("youTubeKey");
        // initialize with API key stored in secrets.xml
        playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                // do any work here to cue video, play video, etc.
                youTubePlayer.cueVideo(youTubeKey);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                // log the error
                Log.e("MovieTrailerActivity", "Error initializing YouTube player");
            }
        });
        */

    }


    public void createBeep(View view)
    {
        // take whats in the etView
       // holder, asspciate view with tweet
        client = BirdieApp.getRestClient();

        client.sendTweet(etCompose.getText().toString(), new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                Tweet tweet;
                try {
                    tweet = Tweet.fromJSON(response);
                    Intent intent = new Intent(ComposeActivity.this, TimelineActivity.class);
                    intent.putExtra("tweet", String.valueOf(tweet));
                    setResult(20, intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent data = new Intent(ComposeActivity.this, TimelineActivity.class);
                //data.putExtra("tweet", tweet);

                Log.d("TwitterClient", response.toString());

            }

        });
    }


    public void endActivity(View view) {

        finish();
    }
}
