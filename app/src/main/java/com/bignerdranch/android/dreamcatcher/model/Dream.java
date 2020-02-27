package com.bignerdranch.android.dreamcatcher.model;

import android.content.ContentValues;

import com.bignerdranch.android.dreamcatcher.database.DreamDbSchema.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by Alan on 2/10/2018.
 */

public class Dream {

    private UUID mId;
    private String mTitle;
    private Date mRevealedDate;
    private boolean mRealized;
    private boolean mDeferred;
    private List<DreamEntry> mDreamEntries;
    private Date mDeferredDate;
    private Date mRealizedDate;

    public Dream() {
        this(UUID.randomUUID());
    }

    public Dream(UUID id) {
        mId = id;
        mTitle = "";
        mRevealedDate = new Date();
        mRealized = false;
        mDeferred = false;
        mDreamEntries = new ArrayList<>();
        mRealizedDate = new Date();
        mDeferredDate = new Date();

    }

    // getters

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public Date getRevealedDate() {
        return mRevealedDate;
    }

    public boolean isRealized() {
        return mRealized;
    }

    public boolean isDeferred() { return mDeferred; }

    public List<DreamEntry> getDreamEntries() {
        return mDreamEntries;
    }

    // setters

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setRevealedDate(Date revealedDate) {
        mRevealedDate = revealedDate;
    }

    public void setRealized(boolean realized) {
        mRealized = realized;
    }

    public void setDeferred(boolean deferred) {
        mDeferred = deferred;
    }

    // convenience methods

    public void addComment(String text) {
        DreamEntry entry = new DreamEntry(text, new Date(), DreamEntryKind.COMMENT);
        mDreamEntries.add(entry);
    }

    public void addDreamRealized() {
        DreamEntry entry = new DreamEntry("Dream Realized", new Date(), DreamEntryKind.REALIZED);
        this.mRealizedDate = new Date();
        mDreamEntries.add(entry);
    }

    public void addDreamRevealed() {
        DreamEntry entry = new DreamEntry("Dream Revealed", new Date(), DreamEntryKind.REVEALED);
        mDreamEntries.add(entry);
    }

    public void addDreamDeferred() {
        DreamEntry entry = new DreamEntry("Dream Deferred", new Date(), DreamEntryKind.DEFERRED);
        this.mDeferredDate = new Date();
        mDreamEntries.add(entry);
    }
    // Consider consolidating further into a method
    // (for example, selectDreamRealized) that includes:
    // (1) logic related to deferred
    // (2) setting realized
    // (3) creating + adding realized dream entry

    public void removeDreamRealized() {
        Iterator<DreamEntry> iterator = mDreamEntries.iterator();
        while (iterator.hasNext()) {
            DreamEntry e = iterator.next();
            if (e.getKind() == DreamEntryKind.REALIZED) {
                iterator.remove();
            }
            else if(e.getKind() == DreamEntryKind.DEFERRED){
                iterator.remove();
            }
        }
    }

    public Date getRealizedDate(){ return this.mRealizedDate; }
    public Date getmDeferredDate() { return this.mDeferredDate; }

    public void setDreamEntries(List<DreamEntry> dreamEntries) {
        this.mDreamEntries = dreamEntries;
    }

    public String getPhotoFileName(){
        return "IMG_" + getId().toString()+".jpg";
    }


}
