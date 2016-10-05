package com.ananth.androidvolley;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ananth.androidvolley.utils.PrefUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rey.material.app.ThemeManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import jp.wasabeef.picasso.transformations.BlurTransformation;

public class ProfileActivity extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    public static String mUrl="https://api.github.com/users/";
    private Toolbar toolbar;
    private ImageView mUserImage;
    private TextView mUserName;
    private TextView mCompany;
    private TextView mLocation;
    private TextView mReposCount;
    private TextView mGistsCount;
    private TextView mFollowingsCount;
    private TextView mFollowersCount;
    private String mGitUserName = "";
    private LinearLayout mProgressLay;
    private LinearLayout mNoResultLay;
    private FrameLayout mContentLay;
    private ImageView mBackGroundImage;
    private TextView mNoResult;
    private LinearLayout mReposLay;
    private LinearLayout mGistsLay;
    private LinearLayout mFollowersLay;
    private LinearLayout mFollowingLay;
    private TextView mHappyCoding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ThemeManager.init(this, 1, 0, null);
        mProgressLay = (LinearLayout) findViewById(R.id.progress_lay);
        mNoResultLay = (LinearLayout) findViewById(R.id.no_result_lay);
        mContentLay = (FrameLayout) findViewById(R.id.content_lay);
        mUserImage = (ImageView) findViewById(R.id.user_img);
        mBackGroundImage = (ImageView) findViewById(R.id.user_bg);
        mUserName = (TextView) findViewById(R.id.user_name);
        mCompany = (TextView) findViewById(R.id.company_name);
        mLocation = (TextView) findViewById(R.id.user_location);
        mReposCount = (TextView) findViewById(R.id.repos_count);
        mGistsCount = (TextView) findViewById(R.id.gist_count);
        mFollowingsCount = (TextView) findViewById(R.id.followings_count);
        mFollowersCount = (TextView) findViewById(R.id.followers_count);
        mHappyCoding = (TextView) findViewById(R.id.happy_coding);
        mReposLay = (LinearLayout) findViewById(R.id.repos);
        mGistsLay = (LinearLayout) findViewById(R.id.gist_lay);
        mFollowersLay = (LinearLayout) findViewById(R.id.followers);
        mFollowingLay = (LinearLayout) findViewById(R.id.followings);
        mNoResult = (TextView) findViewById(R.id.noresult);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        mRequestQueue= VolleySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();
        if (getIntent() != null) {
            mGitUserName = getIntent().getStringExtra("username");
        }
        mReposLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, RepositaryList.class).putExtra("username",mGitUserName));
            }
        });
        mFollowersLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, FollowersList.class).putExtra("username",mGitUserName));
            }
        });
        mFollowingLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, FollowingsList.class).putExtra("username",mGitUserName));
            }
        });
        mGistsLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, GistsList.class).putExtra("username",mGitUserName));
            }
        });
        fetchProfileInfo(mGitUserName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        if (id == R.id.logout) {
            SharedPreferences preferences = getSharedPreferences("Volley", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
            return true;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout, menu);
        return true;
    }

    private void fetchProfileInfo(String name)
    {
        String url=mUrl+name;
        mProgressLay.setVisibility(View.VISIBLE);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("repsonse :" +response);
                        bindData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mNoResultLay.setVisibility(View.VISIBLE);
                mProgressLay.setVisibility(View.GONE);
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(req,"profile");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("profile");
        }
    }



    private void bindData(final JSONObject response) {
        String name = null;
        try {
            name = response.getString("login");
            if(PrefUtils.mSave) {
                PrefUtils.saveData("username", name, ProfileActivity.this);
            }
            System.out.println("name :" + name);
            mContentLay.setVisibility(View.VISIBLE);
            mHappyCoding.setVisibility(View.VISIBLE);
            mNoResultLay.setVisibility(View.GONE);
            mProgressLay.setVisibility(View.GONE);

            mUserName.setText(name);
            if (!TextUtils.isEmpty(response.getString("company"))) {
                mCompany.setText(response.getString("company"));
            } else {
                mCompany.setVisibility(View.GONE);
            }
            mLocation.setText(response.getString("location"));
            mReposCount.setText("" + response.getInt("public_repos"));
            mGistsCount.setText("" + response.getInt("public_gists"));
            mFollowersCount.setText("" + response.getInt("followers"));
            mFollowingsCount.setText("" +response.getInt("following"));
            Picasso.with(ProfileActivity.this)
                    .load(response.getString("avatar_url")) // thumbnail url goes here
                    .placeholder(R.mipmap.ic_launcher)
                    .transform(new BlurTransformation(ProfileActivity.this, 40))
                    .into(mBackGroundImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            try {
                                Picasso.with(ProfileActivity.this)
                                        .load(response.getString("avatar_url")) // image url goes here
                                        .placeholder(mUserImage.getDrawable())
                                        .into(mUserImage);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError() {
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
