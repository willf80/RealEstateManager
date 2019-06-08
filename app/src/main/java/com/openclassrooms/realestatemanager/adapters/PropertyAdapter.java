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
    OnDispatchListener mDispatchListener;

    public PropertyAdapter(List<Property> propertyList, OnDispatchListener dispatchListener) {
        mPropertyList = propertyList;
        mDispatchListener = dispatchListener;
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
        Property property = mPropertyList.get(position);

        holder.itemView.setOnClickListener(v -> mDispatchListener.onItemClick(property));
    }

    @Override
    public int getItemCount() {
        return mPropertyList.size();
    }

    class PropertyViewHolder extends RecyclerView.ViewHolder{
        PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface OnDispatchListener {
        void onItemClick(Property property);
    }
}
