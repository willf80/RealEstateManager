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
import com.openclassrooms.realestatemanager.models.QueryData;

import java.util.List;

public class SearchQueryBuilderView extends LinearLayout {
    private static final int NOTHING = 0;
    private static final int LESS_SIGN = 1;
    private static final int GREATER_SIGN = 2;
    private static final int BETWEEN_SIGN = 3;

    String mTitle;

    Spinner mSpinner;
    LinearLayout mSearchOptionLayout;
    TextView searchTitleView;
    TextView errorTextView;
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
        errorTextView = view.findViewById(R.id.errorTextView);

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

    public boolean isValidInputValues(){
        String maxText = maxValueEditText.getText().toString();
        String minText = minValueEditText.getText().toString();
        int min = convertStringToInt(minText);
        int max = convertStringToInt(maxText);

        switch (mSpinner.getSelectedItemPosition()){
            case LESS_SIGN:
            case GREATER_SIGN:
                return !minText.isEmpty();

            case BETWEEN_SIGN:
                return !(minText.isEmpty() && maxText.isEmpty()) && max >= min;

            default:
                return false;
        }
    }

    private void showErrorMessage(){
        if(!isValidInputValues()){
            errorTextView.setVisibility(VISIBLE);
        }else{
            errorTextView.setVisibility(GONE);
        }
    }

    private void minValueEditTextListener() {
        minValueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showErrorMessage();
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
                showErrorMessage();
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
            case NOTHING:
                mSearchOptionLayout.setVisibility(GONE);
                break;

            case LESS_SIGN:
            case GREATER_SIGN:
                mSearchOptionLayout.setVisibility(VISIBLE);
                maxValueEditText.setVisibility(GONE);
                maxValueEditText.setText("");
                minValueEditText.setHint("Value");
                break;

            case BETWEEN_SIGN:
                mSearchOptionLayout.setVisibility(VISIBLE);
                maxValueEditText.setVisibility(VISIBLE);
                minValueEditText.setHint(getContext().getString(R.string.min));
                break;
        }

        showErrorMessage();
    }

    public String getSign() {
        switch (mSpinner.getSelectedItemPosition()){
            case LESS_SIGN:
                return " <= ";

            case GREATER_SIGN:
                return " >= ";

            case BETWEEN_SIGN:
                return " BETWEEN ";

            default:
                return null;
        }
    }

    public QueryData getData() {
        switch (mSpinner.getSelectedItemPosition()){
            case LESS_SIGN:
            case GREATER_SIGN:
                String valueText = minValueEditText.getText().toString();
                return new QueryData(convertStringToInt(valueText));

            case BETWEEN_SIGN:
                String minText = minValueEditText.getText().toString();
                String maxText = maxValueEditText.getText().toString();

                return new QueryData(convertStringToInt(minText),
                        convertStringToInt(maxText));

            default:
                return null;
        }
    }

}
