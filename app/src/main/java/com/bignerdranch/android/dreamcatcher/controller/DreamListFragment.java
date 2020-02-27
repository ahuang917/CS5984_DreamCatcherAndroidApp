package com.bignerdranch.android.dreamcatcher.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.dreamcatcher.R;

import com.bignerdranch.android.dreamcatcher.model.Dream;
import com.bignerdranch.android.dreamcatcher.model.DreamLab;
import com.bignerdranch.android.dreamcatcher.view.DreamAdapter;

import java.util.Date;
import java.util.List;

/**
 * Created by Alan on 2/10/2018.
 */

public class DreamListFragment extends Fragment{

    // model fields
    //private DreamLab mCrimeLab = DreamLab.getInstance(getActivity());
    private DreamLab mCrimeLab;

    //view field
// view fields (recycler + adapter)
    private RecyclerView mCrimeRecyclerView;
    private DreamAdapter mCrimeAdapter;

    private Callbacks mCallbacks;


    public interface Callbacks {
        public void onDreamSelected(Dream dream);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Context mContext = getActivity();
        mCrimeLab = DreamLab.getInstance(mContext);
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }



    public void updateUI() {
        DreamLab dreamLab = DreamLab.getInstance(getActivity());
        List<Dream> dreams = dreamLab.getDreams();
        if (mCrimeAdapter == null) {
            mCrimeAdapter = new DreamAdapter(mCrimeLab.getDreams());
            mCrimeRecyclerView.setAdapter(mCrimeAdapter);
        } else {
            mCrimeAdapter.setDreams(dreams);
            mCrimeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime:
                Dream dream = new Dream();
                dream.setRevealedDate(new Date());
                dream.addDreamRevealed();
                DreamLab.getInstance(getActivity()).addDream(dream);
                mCallbacks.onDreamSelected(dream);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
