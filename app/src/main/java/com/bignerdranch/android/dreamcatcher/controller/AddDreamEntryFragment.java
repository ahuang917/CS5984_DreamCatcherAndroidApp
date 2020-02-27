package com.bignerdranch.android.dreamcatcher.controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.bignerdranch.android.dreamcatcher.R;

/**
 * Created by Alan on 3/17/2018.
 */

public class AddDreamEntryFragment
        extends DialogFragment {
    public static final String EXTRA_COMMENT = "dreamcatcher.comment";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_entry, null);
        EditText text = view.findViewById(R.id.editText);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Add Comment")
                .setPositiveButton(
                        android.R.string.ok,
                        (dialog, which) -> {
                            sendResult(Activity.RESULT_OK, text.getText().toString());
                        })
                .create();
    }

    private void sendResult(int resultCode, String text) {
        if (getTargetFragment() == null) { return; }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_COMMENT, text);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}