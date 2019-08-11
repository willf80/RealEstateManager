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

import java.util.ArrayList;
import java.util.List;

public class SearchListQueryBuilderView extends LinearLayout {

    private String mTitle;

    private LinearLayout searchListOptionLayout;
    private TextView searchTitleView;

    private List<? extends ISearchListItem> mSearchListItems;
    private List<Long> mSelectedItemList;

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
            Long tag = (Long) buttonView.getTag();

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

        invalidate();
    }

    public String getSign() {
        if(mSelectedItemList.size() == 0) return null;
        if(mSelectedItemList.size() == 1) return " = ";
        return " IN ";
    }

    public String buildConditionsFromQuery(String columnName, List<Object> queryParams){
        StringBuilder builder = new StringBuilder();
        builder.append(" ( ");
        builder.append(columnName);
        builder.append(" ");
        builder.append(getSign());
        if(" IN ".equalsIgnoreCase(getSign())){

            builder.append("(");
            for (int i = 0; i < mSelectedItemList.size(); i++) {
                builder.append("'");
                builder.append(mSelectedItemList.get(i));
                builder.append("'");

                if(i + 1 < mSelectedItemList.size()){
                    builder.append(",");
                }
            }
            builder.append(")");

        }else{
            builder.append(" ? ");
            queryParams.add(getData());
        }
        builder.append(" ) ");

        return builder.toString();
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

    public String getTitle() {
        return mTitle;
    }
}
