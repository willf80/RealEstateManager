package com.openclassrooms.realestatemanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {

    List<Property> mPropertyList;
    OnDispatchListener mDispatchListener;

    public PropertyAdapter(List<Property> propertyList, OnDispatchListener dispatchListener) {
        mPropertyList = propertyList;
        mDispatchListener = dispatchListener;
    }

    public void setPropertyList(List<Property> propertyList) {
        mPropertyList = propertyList;
        notifyDataSetChanged();
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

        String price = Utils.getEnglishCurrencyFormatted(property.getPrice());
        holder.priceTextView.setText(price);

        holder.itemView.setOnClickListener(v -> mDispatchListener.onItemClick(property));
    }

    @Override
    public int getItemCount() {
        return mPropertyList.size();
    }

    class PropertyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.priceTextView)
        TextView priceTextView;

        PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnDispatchListener {
        void onItemClick(Property property);
    }
}
