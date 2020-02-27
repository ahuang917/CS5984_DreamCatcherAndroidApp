package com.bignerdranch.android.dreamcatcher.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.dreamcatcher.database.DreamBaseHelper;
import com.bignerdranch.android.dreamcatcher.database.DreamDbSchema.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan on 3/15/2018.
 */

public class DreamEntryLab {

    private static DreamEntryLab sDreamEntryLab;
    private Context mContext;

    private SQLiteDatabase mDatabase;

    public static DreamEntryLab getInstance(Context context){
        if (sDreamEntryLab == null) { sDreamEntryLab = new DreamEntryLab(context); }
        return sDreamEntryLab;
    }

    private DreamEntryLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new DreamBaseHelper(mContext).getWritableDatabase();
    }

    private static ContentValues getEntryValues(DreamEntry entry, Dream dream){
        ContentValues values = new ContentValues();
        values.put(DreamEntryTable.Cols.TEXT, entry.getText());
        values.put(DreamEntryTable.Cols.DATE, entry.getDate().getTime());
        values.put(DreamEntryTable.Cols.KIND, entry.getKind().toString());
        values.put(DreamEntryTable.Cols.UUID, dream.getId().toString());

        return values;
    }

    public void addDreamEntry(DreamEntry entry, Dream dream){
        ContentValues values = getEntryValues(entry, dream);
        mDatabase.insert(DreamEntryTable.NAME, null, values);
    }

    public void updateDreamEntry(Dream dream){
        //delete all entries for this dream in the DB
        //iterate through entries in Dream object and
        //add them to the DB one at a time.

        mDatabase.delete(DreamEntryTable.NAME, DreamEntryTable.Cols.UUID + "=?", new String[]{dream.getId().toString()});

        for(DreamEntry e : dream.getDreamEntries()){
            addDreamEntry(e, dream);
        }
    }

    public List<DreamEntry> getDreamEntries(Dream dream){
        DreamEntryCursorWrapper cursor = queryDreamEntry(DreamEntryTable.Cols.UUID + " =?", new String[] {dream.getId().toString()});
        List<DreamEntry> listEntries = new ArrayList<>();
        try {
            if(cursor.getCount() == 0) { return null; }
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                listEntries.add(cursor.getDreamEntries());
                cursor.moveToNext();
            }
/*            DreamEntry dreamEntry = cursor.getDreamEntries();

            listEntries.add(dreamEntry);*/
        } finally{
            cursor.close();
        }
        return listEntries;
}

    private DreamEntryCursorWrapper queryDreamEntry(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                DreamEntryTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new DreamEntryCursorWrapper(cursor);
    }


}
