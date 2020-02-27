package com.bignerdranch.android.dreamcatcher.view;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bignerdranch.android.dreamcatcher.R;
import com.bignerdranch.android.dreamcatcher.model.Dream;
import com.bignerdranch.android.dreamcatcher.model.DreamEntry;

import java.text.DateFormat;

/**
 * Created by Alan on 4/8/2018.
 */

public class EntryHolder extends RecyclerView.ViewHolder{
    //model fields
    private DreamEntry mEntry;

    private Button mButton;

    public EntryHolder(LayoutInflater inflater, ViewGroup parent) {

        super(inflater.inflate(R.layout.list_item_entry, parent, false));
        mButton = itemView.findViewById(R.id.entry_button);

    }

    public void bind(DreamEntry entry){
        mEntry = entry;

        switch (entry.getKind()) {
            case REVEALED:
                setRevealedStyle(mButton);
                mButton.setText(entry.getText());
                break;
            case DEFERRED:
                setDeferredStyle(mButton);
                mButton.setText(entry.getText());
                break;
            case REALIZED:
                setRealizedStyle(mButton);
                mButton.setText(entry.getText());
                break;
            case COMMENT:
                DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
                setCommentStyle(mButton);
                String text = entry.getText();
                String date = dateFormat.format(entry.getDate());
                String fullCommentString = text + " (" + date + ")";

                Spannable spannable = new SpannableString(fullCommentString);

                int start = text.length();
                int end = fullCommentString.length();

                spannable.setSpan(
                        new ForegroundColorSpan(Color.WHITE),
                        start,
                        end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                mButton.setText(spannable);
        }
    }

    private void setRevealedStyle(Button button){
        button.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
        button.setTextColor(Color.BLACK);
    }

    private void setRealizedStyle(Button button) {
        button.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        button.setTextColor(Color.BLACK);
    }

    private void setCommentStyle(Button button) {
        //button.setBackgroundColor(Color.GRAY);
        button.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        button.setTextColor(Color.BLACK);
    }

    private void setDeferredStyle(Button button) {
        //button.setBackgroundColor(Color.BLACK);
        button.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
        button.setTextColor(Color.RED);
    }

}
