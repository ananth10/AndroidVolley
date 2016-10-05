package com.ananth.androidvolley;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.ananth.androidvolley.adapter.GistsAdapter;
import com.ananth.androidvolley.adapter.ReposAdapter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class GistsList extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    public static String mUrl = "https://api.github.com/users/";
    ArrayList<HashMap<String, String>> mAllGistsList = new ArrayList<>();
    private RecyclerView mRv;
    private LinearLayout mProgressLay;
    private LinearLayout mNoResultLay;
    private Toolbar toolbar;
    private String mUserName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gists_list);
        mRequestQueue = VolleySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        mRv = (RecyclerView) findViewById(R.id.gists_list);
        mProgressLay = (LinearLayout) findViewById(R.id.progress_lay);
        mNoResultLay = (LinearLayout) findViewById(R.id.no_result_lay);
        if(getIntent()!=null)
        {
            mUserName=getIntent().getStringExtra("username");
        }
        setupRecyclerView();
        fetchGistsList(mUserName);
    }
    private void setupRecyclerView() {
        mRv.setLayoutManager(new LinearLayoutManager(mRv.getContext()));
        mRv.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        mRv.addItemDecoration(itemDecoration);
        mRv.setItemAnimator(new SlideInUpAnimator());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return true;
    }


    private void fetchGistsList(String name) {
        String url = mUrl + name + "/gists";
        mProgressLay.setVisibility(View.VISIBLE);
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        mProgressLay.setVisibility(View.GONE);

                        System.out.println("repsonse :" + array);
                        if (array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                try {
                                    JSONObject mObject = array.getJSONObject(i);
                                    HashMap<String, String> mMap = new HashMap<String, String>();
                                    mMap.put("description", mObject.getString("description"));
                                    mMap.put("url", mObject.getString("html_url"));
                                    if (mObject.has("owner")) {
                                        JSONObject mOwnerobj = mObject.getJSONObject("owner");
                                        mMap.put("owner", mOwnerobj.getString("login"));
                                    }
                                    mAllGistsList.add(mMap);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        if (mAllGistsList.size() > 0) {
                            mRv.setVisibility(View.VISIBLE);
                            mNoResultLay.setVisibility(View.GONE);
                            mRv.setAdapter(new GistsAdapter(GistsList.this, mAllGistsList));
                        } else {
                            mRv.setVisibility(View.GONE);
                            mNoResultLay.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mNoResultLay.setVisibility(View.VISIBLE);
                mProgressLay.setVisibility(View.GONE);
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(req, "repo");
    }
}
