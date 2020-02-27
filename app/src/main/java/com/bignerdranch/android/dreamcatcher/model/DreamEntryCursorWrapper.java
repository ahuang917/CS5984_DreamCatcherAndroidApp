package com.bignerdranch.android.dreamcatcher.model;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.dreamcatcher.database.DreamDbSchema;
import com.bignerdranch.android.dreamcatcher.database.DreamDbSchema.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Alan on 3/15/2018.
 */

public class DreamEntryCursorWrapper extends CursorWrapper{
    public DreamEntryCursorWrapper(Cursor cursor) { super(cursor); }
    public DreamEntry getDreamEntries() {
        String uuidString = getString(getColumnIndex(DreamEntryTable.Cols.UUID));
        String text = getString(getColumnIndex(DreamEntryTable.Cols.TEXT));
        long date = getLong(getColumnIndex(DreamEntryTable.Cols.DATE));
        String kind = getString(getColumnIndex(DreamEntryTable.Cols.KIND));


        DreamEntry dreamEntry = new DreamEntry(text, new Date(date), DreamEntryKind.valueOf(kind));
        return dreamEntry;
    }
}
