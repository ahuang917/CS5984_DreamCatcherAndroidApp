package com.bignerdranch.android.dreamcatcher.controller;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.bignerdranch.android.dreamcatcher.R;
import com.bignerdranch.android.dreamcatcher.model.Dream;

/**
 * Created by Alan on 2/10/2018.
 */

public class DreamListActivity extends SingleFragmentActivity
        implements DreamListFragment.Callbacks, DreamFragment.Callbacks{

    @Override
    public void onDreamSelected(Dream dream) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = DreamActivity.newIntent(this, dream.getId());
            startActivity(intent);
        } else {
            Fragment dreamFragment = DreamFragment.newInstance(dream.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, dreamFragment)
                    .commit();
        }
    }

    @Override
    protected Fragment createFragment() {

        return new DreamListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onDreamUpdated(Dream dream) {
        FragmentManager fm = getSupportFragmentManager();
        DreamListFragment dreamListFragment = (DreamListFragment) fm.findFragmentById(R.id.fragment_container);
        dreamListFragment.updateUI();

    }
}