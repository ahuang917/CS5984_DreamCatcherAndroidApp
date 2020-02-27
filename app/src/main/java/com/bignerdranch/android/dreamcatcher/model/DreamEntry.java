package com.bignerdranch.android.dreamcatcher.model;

import java.util.Date;

/**
 * Created by Alan on 2/24/2018.
 */

public class DreamEntry {

    private String mText;
    private Date mDate;
    private DreamEntryKind mKind;

    public DreamEntry(String text, Date date, DreamEntryKind kind) {
        mText = text;
        mDate = date;
        mKind = kind;
    }

    public String getText() {
        return mText;
    }

    public Date getDate() {
        return mDate;
    }

    public DreamEntryKind getKind() {
        return mKind;
    }
}