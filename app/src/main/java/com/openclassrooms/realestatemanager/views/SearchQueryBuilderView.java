package com.openclassrooms.realestatemanager.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.R;

import java.util.Locale;

public class SearchQueryBuilderView extends LinearLayout {

    String mTitle;
    String mTableName;
    String mPropertyName;

    Spinner mSpinner;
    LinearLayout mSearchOptionLayout;
    TextView searchTitleView;
    EditText minValueEditText;
    EditText maxValueEditText;

    public SearchQueryBuilderView(Context context) {
        super(context);
        init(null, 0);
    }

    public SearchQueryBuilderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SearchQueryBuilderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle){
        // Load view
        View view = LayoutInflater.from(getContext()).inflate(R.layout.search_query_builder_view, this);
        mSpinner = view.findViewById(R.id.spinner);
        mSearchOptionLayout = view.findViewById(R.id.searchOptionLayout);
        searchTitleView = view.findViewById(R.id.searchTitleView);
        minValueEditText = view.findViewById(R.id.minValueEditText);
        maxValueEditText = view.findViewById(R.id.maxValueEditText);

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


        a.recycle();

        invalidateView();
    }

    private void invalidateView() {
        searchTitleView.setText(mTitle);

        minValueEditTextListener();
        maxValueEditTextListener();

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                performSpinnerActions(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private long convertStringToLong(String value){
        if(value == null || value.equals("")|| value.equals("-")){
            return 0;
        }

        return Long.parseLong(value);
    }

    private void minValueEditTextListener() {
        minValueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String maxText = maxValueEditText.getText().toString();
                long min = convertStringToLong(s.toString());
                long max = convertStringToLong(maxText);

                if(min > max){
                    maxValueEditText.setText(String.format(Locale.getDefault(), "%d", min));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().contains("-")) minValueEditText.setText("0");
            }
        });
    }

    private void maxValueEditTextListener() {
        maxValueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String minText = minValueEditText.getText().toString();
                long max = convertStringToLong(s.toString());
                long min = convertStringToLong(minText);

                if(min > max){
                    minValueEditText.setText(String.format(Locale.getDefault(), "%d", max));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().contains("-")) maxValueEditText.setText("0");
            }
        });
    }

    private void performSpinnerActions(int position) {
        switch (position) {
            case 0:
                mSearchOptionLayout.setVisibility(GONE);
                break;

            case 1:
            case 2:
                mSearchOptionLayout.setVisibility(VISIBLE);
                maxValueEditText.setVisibility(GONE);
                maxValueEditText.setText("");
                minValueEditText.setHint("Value");
                break;

            case 3:
                mSearchOptionLayout.setVisibility(VISIBLE);
                maxValueEditText.setVisibility(VISIBLE);
                minValueEditText.setHint(getContext().getString(R.string.min));
                break;
        }
    }

}
