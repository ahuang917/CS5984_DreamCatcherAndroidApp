package com.bignerdranch.android.dreamcatcher.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.dreamcatcher.controller.DreamActivity;
import com.bignerdranch.android.dreamcatcher.R;
import com.bignerdranch.android.dreamcatcher.controller.DreamListFragment;
import com.bignerdranch.android.dreamcatcher.model.Dream;

import java.text.DateFormat;

import javax.security.auth.callback.Callback;

/**
 * Created by Alan on 2/13/2018.
 */

public class DreamHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //model fields
    private Dream mCrime;

    //view fields
    private TextView mTitleTextView;
    private TextView mDateTextView;
    private ImageView mDreamRealizedView;
    private ImageView mDreamDeferredView;

    public DreamHolder(LayoutInflater inflater, ViewGroup parent) {

        super(inflater.inflate(R.layout.list_item_crime, parent, false));

        mTitleTextView = itemView.findViewById(R.id.crime_title);
        mDateTextView = itemView.findViewById(R.id.crime_date);
        mDreamRealizedView = itemView.findViewById(R.id.realized_image);
        mDreamDeferredView = itemView.findViewById(R.id.deferred_image);

        itemView.setOnClickListener(this);
    }

    public void bind(Dream crime){
        mCrime = crime;
        mTitleTextView.setText(mCrime.getTitle());
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        mDateTextView.setText(dateFormat.format(mCrime.getRevealedDate()));
        mDreamRealizedView.setVisibility(mCrime.isRealized() ? View.VISIBLE : View.GONE);
        mDreamDeferredView.setVisibility(mCrime.isDeferred() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View view) {
        Context context = view.getContext();
        DreamListFragment.Callbacks callbacks = (DreamListFragment.Callbacks) context;
        callbacks.onDreamSelected(mCrime);
    }
}
