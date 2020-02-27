package com.bignerdranch.android.dreamcatcher.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bignerdranch.android.dreamcatcher.model.Dream;
import com.bignerdranch.android.dreamcatcher.model.DreamEntry;

import java.util.List;

/**
 * Created by Alan on 4/8/2018.
 */

public class EntryAdapter extends RecyclerView.Adapter<EntryHolder> {
    // model fields
    private List<DreamEntry> mEntries;

    public EntryAdapter(List<DreamEntry> entries) {
        mEntries = entries;
    }

    @Override
    public EntryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new EntryHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(EntryHolder holder, int position) {
        DreamEntry entry = mEntries.get(position);
        holder.bind(entry);
    }
    @Override
    public int getItemCount() {
        return mEntries.size();
    }

    public void setDreamEntries(List<DreamEntry> entries) { mEntries = entries; }
}
