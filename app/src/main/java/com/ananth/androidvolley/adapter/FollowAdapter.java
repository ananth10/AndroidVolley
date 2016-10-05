package com.ananth.androidvolley.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ananth.androidvolley.ProfileActivity;
import com.ananth.androidvolley.R;
import com.ananth.androidvolley.utils.PrefUtils;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Babu on 10/3/2016.
 */
public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<HashMap<String, String>> mValues;
    public Context mContext;
    private View mView1;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String mBoundString;
        public final View mView;
        public TextView mUserName;
        public ImageView mUserImage;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mUserName = (TextView) view.findViewById(R.id.user_name);
            mUserImage = (ImageView) view.findViewById(R.id.user_img);
        }

    }


    public FollowAdapter(Context context, List<HashMap<String, String>> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
        this.mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.follow_items, parent, false);
        view.setBackgroundResource(mBackground);
        mView1=view;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mUserName.setText(mValues.get(position).get("name"));
        Picasso.with(mContext)
                .load(mValues.get(position).get("image")) // image url goes here
                .placeholder(R.drawable.noimage)
                .into(holder.mUserImage);

        mView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("clicked" +mValues.get(position).get("name"));
                PrefUtils.mSave=false;
                mContext.startActivity(new Intent(mContext, ProfileActivity.class).putExtra("username", mValues.get(position).get("name")));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}

