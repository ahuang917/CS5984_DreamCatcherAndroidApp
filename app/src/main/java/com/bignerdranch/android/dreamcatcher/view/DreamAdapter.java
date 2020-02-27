package com.bignerdranch.android.dreamcatcher.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bignerdranch.android.dreamcatcher.model.Dream;

import java.util.List;

/**
 * Created by Alan on 2/13/2018.
 */

public class DreamAdapter extends RecyclerView.Adapter<DreamHolder> {
    // model fields
    private List<Dream> mCrimes;
    public DreamAdapter(List<Dream> crimes) {
        mCrimes = crimes;
    }
    @Override
    public DreamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new DreamHolder(layoutInflater, parent);
    }
    @Override
    public void onBindViewHolder(DreamHolder holder, int position) {
        Dream crime = mCrimes.get(position);
        holder.bind(crime);
    }
    @Override
    public int getItemCount() {
        return mCrimes.size();
    }

    public void setDreams(List<Dream> dreams) { mCrimes = dreams; }
}
