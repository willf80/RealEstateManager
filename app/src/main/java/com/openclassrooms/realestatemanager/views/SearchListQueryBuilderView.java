package com.openclassrooms.realestatemanager.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.ISearchListItem;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class SearchListQueryBuilderView extends LinearLayout {

    private String mTitle;
    private String mTableName;
    private String mPropertyName;

    private LinearLayout searchListOptionLayout;
    private TextView searchTitleView;

    private List<? extends ISearchListItem> mSearchListItems;
    private List<Integer> mSelectedItemList;

    public SearchListQueryBuilderView(Context context) {
        super(context);
        init(null, 0);
    }

    public SearchListQueryBuilderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SearchListQueryBuilderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle){
        // Load view
        View view = LayoutInflater.from(getContext()).inflate(R.layout.search_list_query_builder_view, this);

        searchListOptionLayout = view.findViewById(R.id.searchListOptionLayout);
        searchTitleView = view.findViewById(R.id.searchTitleView);

        // Load attributes
        @SuppressLint("CustomViewStyleable")
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.SearchQuery, defStyle, 0);

        mTitle = a.getString(
                R.styleable.SearchQuery_query_view_title);

        mTableName = a.getString(
                R.styleable.SearchQuery_query_table_name);

        mPropertyName = a.getString(
                R.styleable.SearchQuery_query_property_name);

        mSelectedItemList = new ArrayList<>();

        a.recycle();

        invalidateView();
    }

    private void invalidateView() {
        searchTitleView.setText(mTitle);
    }

    private CheckBox createItemView(ISearchListItem searchListItem) {
        CheckBox checkBox = new CheckBox(getContext());
        checkBox.setText(searchListItem.getTitle());
        checkBox.setTag(searchListItem.getId());

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Integer tag = (int)buttonView.getTag();

            if(isChecked){
                mSelectedItemList.add(tag);
            }else {
                mSelectedItemList.remove(tag);
            }
        });

        return checkBox;
    }

    private void addSearchItemView() {
        if(mSearchListItems == null) return;

        searchListOptionLayout.removeAllViews();
        for (int i = 0; i < mSearchListItems.size(); i++) {
            searchListOptionLayout.addView(createItemView(mSearchListItems.get(i)));
        }
    }

    public String buildQuery() {
        if(mTableName == null || mPropertyName == null) {
            throw new InvalidParameterException("Please set table name and property name");
        }

        if(mSelectedItemList.size() <= 0) {
            return null;
        }

        StringBuilder selected = new StringBuilder();
        for (int i = 0; i < mSelectedItemList.size(); i++) {
            selected.append(mSelectedItemList.get(i));

            if(i < mSearchListItems.size() - 1)
                selected.append(",");
        }

        String query = mTableName + "." + mPropertyName;// + " IN(";
        if(mSelectedItemList.size() == 1){
            query += " = " + mSelectedItemList.get(0);
        }else{
            query += " IN(" + selected.toString() + ")";
        }

        return query;
    }

    public String getSign() {
        if(mSelectedItemList.size() == 0) return null;
        if(mSelectedItemList.size() == 1) return " = ";
        return " IN ";
    }

    public Object getData() {
        if(mSelectedItemList.size() == 0) return null;

        if(mSelectedItemList.size() == 1){
            return mSelectedItemList.get(0);
        }

        return mSelectedItemList;
    }

    public void setSearchList(List<? extends ISearchListItem> searchListList) {
        mSearchListItems = searchListList;
        addSearchItemView();
    }

    public boolean isUsed() {
        return mSelectedItemList.size() > 0;
    }

    public void setTitle(String title) {
        mTitle = title;
        invalidateView();
    }

    public void setTableName(String tableName) {
        mTableName = tableName;
    }

    public void setPropertyName(String propertyName) {
        mPropertyName = propertyName;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getTableName() {
        return mTableName;
    }

    public String getPropertyName() {
        return mPropertyName;
    }
}
