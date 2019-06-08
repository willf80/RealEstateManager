package com.openclassrooms.realestatemanager.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.PropertyAdapter;
import com.openclassrooms.realestatemanager.models.Property;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PropertyListFragment extends Fragment {

    PropertyAdapter mPropertyAdapter;
    List<Property> mPropertyList;

    public static PropertyListFragment newInstance() {
        PropertyListFragment fragment = new PropertyListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_property_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        mPropertyList = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            mPropertyList.add(new Property());
        }

        mPropertyAdapter = new PropertyAdapter(mPropertyList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mPropertyAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        return view;
    }

}
