package com.openclassrooms.realestatemanager.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.realestatemanager.EditPropertyActivity;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.MediaAdapter;
import com.openclassrooms.realestatemanager.models.Media;

import java.util.ArrayList;
import java.util.List;

public class DetailsPropertyFragment extends Fragment {
    private static final String ARG_PROPERTY_ID = "propertyId";

    RecyclerView mMediaRecyclerView;
    FloatingActionButton mEditPropertyFab;

    private String mPropertyId;

    private OnDetailsDispatchListener mListener;
    private MediaAdapter mMediaAdapter;
    private List<Media> mMediaList;

    public static DetailsPropertyFragment newInstance(String propertyId) {
        DetailsPropertyFragment fragment = new DetailsPropertyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PROPERTY_ID, propertyId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPropertyId = getArguments().getString(ARG_PROPERTY_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details_property, container, false);
        mMediaRecyclerView = view.findViewById(R.id.mediaRecyclerView);
        mEditPropertyFab = view.findViewById(R.id.editPropertyFab);

        mMediaList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mMediaList.add(new Media());
        }

        mMediaAdapter = new MediaAdapter(mMediaList);
        mMediaRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        mMediaRecyclerView.setAdapter(mMediaAdapter);

        listeners();

        return view;
    }

    private void listeners() {
        mEditPropertyFab.setOnClickListener(v -> showEditActivity());
    }

    private void showEditActivity() {
        Intent intent = new Intent(getContext(), EditPropertyActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailsDispatchListener) {
            mListener = (OnDetailsDispatchListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDetailsDispatchListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDetailsDispatchListener {
        void onPropertyMarkAsSell();
    }
}
