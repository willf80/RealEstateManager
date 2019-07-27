package com.openclassrooms.realestatemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Address;
import com.openclassrooms.realestatemanager.models.MediaTemp;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyDisplayAllInfo;
import com.openclassrooms.realestatemanager.models.PropertyType;
import com.openclassrooms.realestatemanager.utils.FileHelper;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {

    private List<PropertyDisplayAllInfo> mPropertyList;
    private final OnDispatchListener mDispatchListener;

    public PropertyAdapter(List<PropertyDisplayAllInfo> propertyList, OnDispatchListener dispatchListener) {
        mPropertyList = propertyList;
        mDispatchListener = dispatchListener;
    }

    public void setPropertyList(List<PropertyDisplayAllInfo> propertyList) {
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
        PropertyDisplayAllInfo propertyDisplayedInfo = mPropertyList.get(position);

        Property property = propertyDisplayedInfo.getProperty();
        assert property != null;

        PropertyType propertyType = propertyDisplayedInfo.getPropertyType();
        assert propertyType != null;

        Address address = propertyDisplayedInfo.getAddress();
        MediaTemp mediaTemp = propertyDisplayedInfo.getMediaTemp();

        String title = Utils.getPropertyTitle(property, propertyType);

        String price = Utils.getEnglishCurrencyFormatted(property.getPrice());


        if(address != null) {
            holder.addressLine1TextView.setText(address.getAddressLine1());
        }

        if(mediaTemp != null) {
            Picasso.get()
                    .load(FileHelper.getFile(holder.mContext, mediaTemp.photoPath))
                    .resize(124, 124)
                    .into(holder.imageView);
        }

        holder.priceTextView.setText(price);
        holder.titleTextView.setText(title);
        holder.itemView.setOnClickListener(v -> mDispatchListener.onItemClick(property));
    }

    @Override
    public int getItemCount() {
        return mPropertyList.size();
    }

    class PropertyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.imageView)
        ImageView imageView;

        @BindView(R.id.titleTextView)
        TextView titleTextView;

        @BindView(R.id.priceTextView)
        TextView priceTextView;

        @BindView(R.id.addressLine1TextView)
        TextView addressLine1TextView;

        Context mContext;

        PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
        }
    }

    public interface OnDispatchListener {
        void onItemClick(Property property);
    }
}
