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

import java.util.List;
import java.util.Locale;

public class SearchQueryBuilderView extends LinearLayout {

    String mTitle;

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

    private int convertStringToInt(String value){
        if(value == null || value.equals("")|| value.equals("-")){
            return 0;
        }

        return Integer.parseInt(value);
    }

    private void minValueEditTextListener() {
        minValueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String maxText = maxValueEditText.getText().toString();
                int min = convertStringToInt(s.toString());
                int max = convertStringToInt(maxText);

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
                int max = convertStringToInt(s.toString());
                int min = convertStringToInt(minText);

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

    public String buildConditionsFromQuery(String columnName, List<Object> queryParams){
        StringBuilder builder = new StringBuilder();
        builder.append(" ( ");
        builder.append(columnName);
        builder.append(" ");
        builder.append(getSign());

        QueryData queryData = getData();

        if(" BETWEEN ".equalsIgnoreCase(getSign())){
            builder.append(" ? AND ? ");
            queryParams.add(queryData.getMinValue());
            queryParams.add(queryData.getMaxValue());
        }else{
            builder.append(" ? ");
            queryParams.add(queryData.getMinValue());
        }
        builder.append(" ) ");

        return builder.toString();
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

    public boolean isUsed() {
        switch (mSpinner.getSelectedItemPosition()){
            case 1:
            case 2:
                return !minValueEditText.getText().toString().isEmpty();

            case 3:
                return !minValueEditText.getText().toString().isEmpty()
                        &&
                        !maxValueEditText.getText().toString().isEmpty();

            default:
                return false;
        }
    }

    public String getSign() {
        switch (mSpinner.getSelectedItemPosition()){
            case 1:
                return " <= ";

            case 2:
                return " >= ";

            case 3:
                return " BETWEEN ";

            default:
                return null;
        }
    }

    public QueryData getData() {
        switch (mSpinner.getSelectedItemPosition()){
            case 1:
            case 2:
                String valueText = minValueEditText.getText().toString();
                return new QueryData(convertStringToInt(valueText));

            case 3:
                String minText = minValueEditText.getText().toString();
                String maxText = maxValueEditText.getText().toString();

                return new QueryData(convertStringToInt(minText),
                        convertStringToInt(maxText));

            default:
                return null;
        }
    }


    public class QueryData{
        private int minValue;
        private int maxValue;

        public QueryData() {
        }

        QueryData(int minValue) {
            this.minValue = minValue;
        }

        QueryData(int minValue, int maxValue) {
            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        public int getMinValue() {
            return minValue;
        }

        public void setMinValue(int minValue) {
            this.minValue = minValue;
        }

        public int getMaxValue() {
            return maxValue;
        }

        public void setMaxValue(int maxValue) {
            this.maxValue = maxValue;
        }
    }

}
