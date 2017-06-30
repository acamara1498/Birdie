package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class ComposeActivity extends AppCompatActivity {
    BirdieClient client;
    EditText etCompose;
    TextView tvCharCount;
    Button btnBeep;
    ImageView ivProfileCompose;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar);
        setContentView(R.layout.activity_compose);

        etCompose = (EditText) findViewById(R.id.etCompose);
        tvCharCount = (TextView) findViewById(R.id.tvCharCount);
        etCompose.addTextChangedListener(TextEditorWatcher);
        btnBeep = (Button) findViewById(R.id.btnBeep);


        findViewById(R.id.ibExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.context = this;

        ivProfileCompose = (ImageView) findViewById(R.id.ivProfileCompose);

        client = BirdieApp.getRestClient();
        client.getOwnProfile(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String profilePicURL = response.getString("profile_image_url_https");
                    Glide.with(context)
                            .load(profilePicURL)
                            .bitmapTransform(new RoundedCornersTransformation(context, 50, 0))
                            .into(ivProfileCompose);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });


    }

    private final TextWatcher TextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            tvCharCount.setText(String.valueOf(140-s.length()));

            if (s.length()>140)
            {
                tvCharCount.setTextColor(Color.RED);
                btnBeep.setAlpha(.5f);
                btnBeep.setBackgroundColor(Color.GRAY);
                btnBeep.setClickable(false);
                Toast.makeText(ComposeActivity.this, "Too many chaacters", Toast.LENGTH_LONG).show();;
            } else
            {
                tvCharCount.setTextColor(Color.BLACK);
                btnBeep.setAlpha(1f);
                btnBeep.setClickable(true);
            }

        }

        public void afterTextChanged(Editable s) {
        }
    };


    public void makeBeep(View view)
    {
        client = BirdieApp.getRestClient();

        client.sendTweet(etCompose.getText().toString(), new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                Tweet tweet;
                try {
                    tweet = Tweet.fromJSON(response);
                    Intent intent = new Intent();
                    intent.putExtra("tweet", Parcels.wrap(tweet));
                    setResult(RESULT_OK, intent);
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

}
