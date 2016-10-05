package com.ananth.androidvolley.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ananth.androidvolley.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Babu on 10/3/2016.
 */
public class ReposAdapter extends RecyclerView.Adapter<ReposAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<HashMap<String, String>> mValues;
    public Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String mBoundString;
        public final View mView;
        public TextView mTitle;
        public TextView mDescription;
        public TextView mUrl;
        public TextView mOwner;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = (TextView) view.findViewById(R.id.title);
            mDescription = (TextView) view.findViewById(R.id.description);
            mUrl = (TextView) view.findViewById(R.id.url);
            mOwner = (TextView) view.findViewById(R.id.name);
        }

    }


    public ReposAdapter(Context context, List<HashMap<String, String>> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repos_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTitle.setText(mValues.get(position).get("name"));
        if (!TextUtils.isEmpty(mValues.get(position).get("description"))) {
            holder.mDescription.setText(mValues.get(position).get("description"));
        } else {
            holder.mDescription.setText("No description");

        }
        holder.mUrl.setText(mValues.get(position).get("url"));
        holder.mOwner.setText(mValues.get(position).get("owner"));


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}
