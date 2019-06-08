package com.openclassrooms.realestatemanager.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;

import butterknife.ButterKnife;

public class DetailsPropertyFragment extends Fragment {
    private static final String ARG_PROPERTY_ID = "propertyId";

    private String mPropertyId;

    private OnDetailsDispatchListener mListener;

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
        ButterKnife.bind(view);

        return view;
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
