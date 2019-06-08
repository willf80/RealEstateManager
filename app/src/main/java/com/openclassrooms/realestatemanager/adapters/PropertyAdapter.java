package com.openclassrooms.realestatemanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Property;

import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {

    List<Property> mPropertyList;

    public PropertyAdapter(List<Property> propertyList) {
        mPropertyList = propertyList;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.property_item_view, parent, false);

        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mPropertyList.size();
    }

    public class PropertyViewHolder extends RecyclerView.ViewHolder{

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
