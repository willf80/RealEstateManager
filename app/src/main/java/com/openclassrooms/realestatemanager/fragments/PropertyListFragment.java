package com.openclassrooms.realestatemanager.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.PropertyAdapter;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.viewmodels.UserViewModel;
import com.openclassrooms.realestatemanager.viewmodels.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;


public class PropertyListFragment extends Fragment implements PropertyAdapter.OnDispatchListener {

    PropertyAdapter mPropertyAdapter;
    List<Property> mPropertyList;

    OnFragmentDispatchListener mDispatchListener;

    UserViewModel mUserViewModel;
    PropertyViewModel mPropertyViewModel;

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

    private void configureViewModels() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());

        this.mUserViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(UserViewModel.class);

        this.mPropertyViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(PropertyViewModel.class);
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

        mPropertyAdapter = new PropertyAdapter(mPropertyList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mPropertyAdapter);

        configureViewModels();

        loadData();

        return view;
    }

    private void loadData() {
        mPropertyViewModel.getProperties().observe(this, this::onPropertyListLoaded);
    }

    private void onPropertyListLoaded(List<Property> propertyList) {
        mPropertyAdapter.setPropertyList(propertyList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnFragmentDispatchListener){
            mDispatchListener = (OnFragmentDispatchListener) context;
        }else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentDispatchListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDispatchListener =  null;
    }

    @Override
    public void onItemClick(Property property) {
        mDispatchListener.onItemClick(property);
    }

    public interface OnFragmentDispatchListener {
        void onItemClick(Property property);
    }

}
