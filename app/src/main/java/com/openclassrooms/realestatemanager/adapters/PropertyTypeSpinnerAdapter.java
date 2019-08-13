package com.openclassrooms.realestatemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.PropertyType;

import java.util.List;

public class PropertyTypeSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    private final Context context;
    private List<PropertyType> mPropertyTypes;

    public interface SpinnerListener {
        void itemSelected(PropertyType propertyType);
    }

    public PropertyTypeSpinnerAdapter(Context context, List<PropertyType> propertyTypes) {
        this.context = context;
        mPropertyTypes = propertyTypes;
    }

    public void setPropertyTypes(List<PropertyType> propertyTypes) {
        mPropertyTypes = propertyTypes;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mPropertyTypes.size();
    }

    @Override
    public Object getItem(int position) {
        return mPropertyTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEmpty() {
        return mPropertyTypes.isEmpty();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return configure(position, convertView, R.layout.custom_spinner_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return configure(position, convertView, R.layout.custom_spinner_item);
    }

    private View configure(int position, View convertView, int viewId) {
        final ViewHolder viewHolder;
        final PropertyType propertyType = mPropertyTypes.get(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(viewId, null);

            viewHolder = new ViewHolder();

            viewHolder.text = convertView.findViewById(R.id.text1);


            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.text.setText(propertyType.getLabel());

        return convertView;
    }

    static class ViewHolder {
        TextView text;
    }
}
