package com.bignerdranch.android.dreamcatcher.controller;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bignerdranch.android.dreamcatcher.model.Dream;

import java.util.UUID;

public class DreamActivity extends SingleFragmentActivity
        implements DreamFragment.Callbacks {
    private static final String EXTRA_CRIME_ID =
            "criminalintent.crime_id";

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, DreamActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }
    @Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return DreamFragment.newInstance(crimeId);
    }

    @Override
    public void onDreamUpdated(Dream dream) {
        // no actions needed
    }
}