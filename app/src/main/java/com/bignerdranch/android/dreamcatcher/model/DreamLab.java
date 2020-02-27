package com.bignerdranch.android.dreamcatcher.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.dreamcatcher.database.DreamBaseHelper;
import com.bignerdranch.android.dreamcatcher.database.DreamDbSchema.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Alan on 2/22/2018.
 */

public class DreamLab {
    private static DreamLab sDreamLab;
    private List<Dream> mDreams;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static DreamLab getInstance(Context context) {
        if (sDreamLab == null) { sDreamLab = new DreamLab(context); }
        return sDreamLab;
    }
    private DreamLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DreamBaseHelper(mContext).getWritableDatabase();

    }

    public File getPhotoFile(Dream crime){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, crime.getPhotoFileName());
    }

    public List<Dream> getDreams() {
        List<Dream> dreams = new ArrayList<>();
        DreamCursorWrapper cursor = queryDreams(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                dreams.add(cursor.getDream());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return dreams;
    }

    private static ContentValues getDreamValues(Dream dream){
        ContentValues values = new ContentValues();
        values.put(DreamTable.Cols.UUID, dream.getId().toString());
        values.put(DreamTable.Cols.TITLE, dream.getTitle());
        values.put(DreamTable.Cols.DATE, dream.getRevealedDate().getTime());
        values.put(DreamTable.Cols.DEFERRED, dream.isDeferred() ? 1 : 0);
        values.put(DreamTable.Cols.REALIZED, dream.isRealized() ? 1 : 0);

        return values;
    }

    public void addDream(Dream dream) {
        ContentValues values = getDreamValues(dream);
        mDatabase.insert(DreamTable.NAME, null, values);
        for (DreamEntry entry : dream.getDreamEntries()) {
            DreamEntryLab.getInstance(mContext).addDreamEntry(entry, dream);
        }
    }

    public void updateDream(Dream dream) {
        String uuidString = dream.getId().toString();
        ContentValues values = getDreamValues(dream);
        mDatabase.update(DreamTable.NAME, values,
                DreamTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    public Dream getDream(UUID id) {
        DreamCursorWrapper cursor = queryDreams(
                DreamTable.Cols.UUID + " = ?",
                new String[]{ id.toString() }
        );
        try {
            if (cursor.getCount() == 0) { return null; }
            cursor.moveToFirst();
            Dream dream = cursor.getDream();
            List<DreamEntry> entries =
                    DreamEntryLab.getInstance(mContext).getDreamEntries(dream);

            dream.setDreamEntries(entries);
            return dream;
        } finally {
            cursor.close();
        }
    }

    private DreamCursorWrapper queryDreams(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                DreamTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new DreamCursorWrapper(cursor);
    }


}