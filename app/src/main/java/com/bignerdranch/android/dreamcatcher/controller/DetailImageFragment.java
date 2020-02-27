package com.bignerdranch.android.dreamcatcher.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bignerdranch.android.dreamcatcher.R;

import java.util.Date;

/**
 * Created by Alan on 4/9/2018.
 */

public class DetailImageFragment
    extends DialogFragment{

    private static final String ARG_IMAGE = "image";

    //private DatePicker mDatePicker;

    public static DetailImageFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_IMAGE, date);

        DetailImageFragment fragment = new DetailImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_image, null);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setNegativeButton(
                        android.R.string.cancel,
                        (dialog, which) -> {
                        })
                .create();
    }

}
