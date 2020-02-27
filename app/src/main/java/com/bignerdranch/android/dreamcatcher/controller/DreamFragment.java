package com.bignerdranch.android.dreamcatcher.controller;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.bignerdranch.android.dreamcatcher.R;
import com.bignerdranch.android.dreamcatcher.model.Dream;
import com.bignerdranch.android.dreamcatcher.model.DreamEntry;
import com.bignerdranch.android.dreamcatcher.model.DreamEntryKind;
import com.bignerdranch.android.dreamcatcher.model.DreamEntryLab;
import com.bignerdranch.android.dreamcatcher.model.DreamLab;
import com.bignerdranch.android.dreamcatcher.utils.PictureUtils;
import com.bignerdranch.android.dreamcatcher.view.DreamAdapter;
import com.bignerdranch.android.dreamcatcher.view.EntryAdapter;

public class DreamFragment extends Fragment {

    private static final int REQUEST_IMAGE = 3;
    private static final int REQUEST_PHOTO = 2;
    private static String ARG_CRIME_ID = "crime_id";
    private static final int REQUEST_COMMENT = 0;
    private static final String DIALOG_ADD_DREAM_ENTRY= "DialogAddDreamEntry";
    private static final String DIALOG_IMAGE= "DialogImage";

    // model fields
    private Dream mDream;
    private File mPhotoFile;

    // view fields
    private RecyclerView mEntryRecyclerView;
    private EntryAdapter mEntryAdapter;
    private EditText mTitleField;
    private CheckBox mRealizedCheckBox;
    private CheckBox mDeferredCheckBox;
    private Button mEntryButton0;
    private Button mEntryButton1;
    private Button mEntryButton2;
    private Button mEntryButton3;
    private Button mEntryButton4;
    private FloatingActionButton mAddCommentFAB;
    private ImageView mPhotoView;
    private ImageButton mPhotoButton;

    private Callbacks mCallbacks;
    public interface Callbacks {
        void onDreamUpdated(Dream dream);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public static DreamFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        DreamFragment fragment = new DreamFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mDream = DreamLab.getInstance(getActivity()).getDream(crimeId);
        mPhotoFile = DreamLab.getInstance(getActivity()).getPhotoFile(mDream);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dream, container, false);


        PackageManager packageManager = getActivity().getPackageManager();
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;



        // initialize view fields

        mTitleField = view.findViewById(R.id.dream_title);
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mDream.setTitle(charSequence.toString());
                updateDream();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // do nothing
            }
        });

        mRealizedCheckBox = view.findViewById(R.id.dream_realized);
        mDeferredCheckBox = view.findViewById(R.id.dream_deffered);

        //Set Realized or Deferred to enabled if the other is checked
        if(mDream.isRealized()) {
            mDeferredCheckBox.setEnabled(false);
        }
        else if(mDream.isDeferred()){
            mRealizedCheckBox.setEnabled(false);
        }

        mRealizedCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                // add the DR message
                if (!mDream.isRealized()) {
                    mDream.addDreamRealized();
                    mDeferredCheckBox.setEnabled(false);

                }
            } else {
                // remove the DR message
                if (mDream.isRealized()) {
                    mDream.removeDreamRealized();
                    mDeferredCheckBox.setEnabled(true);
                }
            }
            mDream.setRealized(b);
            refreshView();
            updateDream();
        });

        mDeferredCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                // add the DD message
                if (!mDream.isDeferred()) {
                    mDream.addDreamDeferred();
                    mRealizedCheckBox.setEnabled(false);

                }
            } else {
                // remove the DD message
                if (mDream.isDeferred()) {
                    mDream.removeDreamRealized();
                    mRealizedCheckBox.setEnabled(true);

                }
            }

            mDream.setDeferred(b);
            refreshView();
            updateDream();
        });

        mEntryRecyclerView = view.findViewById(R.id.entry_recycler_view);
        mEntryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mAddCommentFAB = view.findViewById(R.id.add_entry);
        mAddCommentFAB.setOnClickListener(
                v -> {
                    FragmentManager manager =
                            DreamFragment.this.getFragmentManager();
                    AddDreamEntryFragment dialog = new AddDreamEntryFragment();
                    dialog.setTargetFragment(
                            DreamFragment.this, REQUEST_COMMENT);
                    dialog.show(manager, DIALOG_ADD_DREAM_ENTRY);
                });

        mPhotoButton = view.findViewById(R.id.dream_camera);
        mPhotoButton.setEnabled(canTakePhoto);
        mPhotoButton.setOnClickListener(v -> {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.bignerdranch.android.dreamcatcher.fileprovider",
                    mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            List<ResolveInfo> cameraActivities = getActivity()
                    .getPackageManager().queryIntentActivities(captureImage,
                            PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo activity : cameraActivities) {
                getActivity().grantUriPermission(activity.activityInfo.packageName,
                        uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            startActivityForResult(captureImage, REQUEST_PHOTO);
        });

        mPhotoView = view.findViewById(R.id.dream_photo);
        mPhotoView.setOnClickListener(
                v -> {
                    FragmentManager manager =
                            DreamFragment.this.getFragmentManager();
                    DetailImageFragment dialog = new DetailImageFragment();
                    dialog.setTargetFragment(
                            DreamFragment.this, REQUEST_IMAGE);
                    dialog.show(manager, DIALOG_IMAGE);
                });
        updatePhotoView();

        refreshView();
        //updateDreamEntries();
        updateDream();

        return view;
    }

    @Override
    public void onPause(){
        super.onPause();
        DreamLab.getInstance(getActivity())
                .updateDream(mDream);
        DreamEntryLab.getInstance(getActivity())
                .updateDreamEntry(mDream);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != Activity.RESULT_OK) { return;}
        if (requestCode == REQUEST_COMMENT) {
            String comment =
                    (String) intent.getSerializableExtra(AddDreamEntryFragment.EXTRA_COMMENT);
            mDream.addComment(comment);
            //updateDreamEntries();
        }

        if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.bignerdranch.android.dreamcatcher.fileprovider",
                    mPhotoFile);
            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
        }
        updateDreamEntries();
        //handlke other dialogs depending on requestCode
    }

    private void refreshView() {
        if (mDream.getTitle() != null) {
            mTitleField.setText(mDream.getTitle());
        }
        mRealizedCheckBox.setChecked(mDream.isRealized());
        mDeferredCheckBox.setChecked(mDream.isDeferred());

        updateDreamEntries();
        //refreshEntryButtons();
        List<DreamEntry> entries = mDream.getDreamEntries();
        for (DreamEntry e : entries) {
            Log.d("refreshView", e.getText());
        }
    }

    private void updateDreamEntries() {
        List<DreamEntry> entries = mDream.getDreamEntries();
        if (mEntryAdapter == null) {
            mEntryAdapter = new EntryAdapter(entries);
            mEntryRecyclerView.setAdapter(mEntryAdapter);
        } else {
            mEntryAdapter.setDreamEntries(entries);
            mEntryAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_dream, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_dream:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getDreamReport());
                i.putExtra(Intent.EXTRA_SUBJECT, "DreamCatcher Dream Report");
                try {
                    startActivity(i);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(),"NO APP TO HANDLE THIS", Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    private void updateDream() {
        DreamLab.getInstance(getActivity()).updateDream(mDream);
        DreamEntryLab.getInstance(getActivity()).updateDreamEntry(mDream);
        mCallbacks.onDreamUpdated(mDream);
    }

    private String getDreamReport(){
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        StringBuilder sb = new StringBuilder();

        String date = dateFormat.format(mDream.getRevealedDate());
        sb.append("My Dream: " + mDream.getTitle() + ". This dream was revealed on " +
        date + ". ");

        if(mDream.isRealized()){
            date = dateFormat.format(mDream.getRealizedDate());
            sb.append("Realized on " + date + ".");
        }
        if(mDream.isDeferred()){
            date = dateFormat.format(mDream.getmDeferredDate());
            sb.append("Deferred on " + date + ".");
        }

        return sb.toString();
    }
}

