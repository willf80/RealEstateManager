package com.openclassrooms.realestatemanager.views;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.QueryData;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.Calendar;
import java.util.List;

public class SearchDateQueryBuilderView extends LinearLayout {
    private static final int NOTHING = 0;
    private static final int LESS_SIGN = 1;
    private static final int GREATER_SIGN = 2;
    private static final int BETWEEN_SIGN = 3;

    private String mTitle;

    private Spinner spinner;
    private LinearLayout searchOptionLayout;
    private TextView searchTitleView;
    private TextView dateMinTextView;
    private TextView dateMaxTextView;

    private Calendar dateMin;
    private Calendar dateMax;
    private TextView errorTextView;

    public SearchDateQueryBuilderView(Context context) {
        super(context);
        init(null, 0);
    }

    public SearchDateQueryBuilderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SearchDateQueryBuilderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle){
        // Load view
        View view = LayoutInflater.from(getContext()).inflate(R.layout.search_date_query_builder_view, this);
        spinner = view.findViewById(R.id.spinner);
        searchOptionLayout = view.findViewById(R.id.searchOptionLayout);
        searchTitleView = view.findViewById(R.id.searchTitleView);
        dateMinTextView = view.findViewById(R.id.dateMinTextView);
        dateMaxTextView = view.findViewById(R.id.dateMaxTextView);
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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                performSpinnerActions(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dateMinListener();
        dateMaxListener();
        inputsChangeListener();
    }

    private void inputsChangeListener(){
        dateMinTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showErrorMessage();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dateMaxTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showErrorMessage();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void dateMinListener() {
        dateMinTextView.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view, year, month, dayOfMonth) -> {
                        dateMin = Calendar.getInstance();
                        dateMin.clear();
                        dateMin.set(year, month, dayOfMonth);

                        updateDateView();
                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH));


            if(dateMax != null) {
                datePickerDialog.getDatePicker().setMaxDate(dateMax.getTimeInMillis());
            }

            datePickerDialog.show();
        });
    }

    private void dateMaxListener() {
        dateMaxTextView.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view, year, month, dayOfMonth) -> {
                        dateMax = Calendar.getInstance();
                        dateMax.clear();
                        dateMax.set(year, month, dayOfMonth);

                        if(dateMax.before(dateMin)){
                            dateMin = dateMax;
                        }

                        updateDateView();
                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH));

            if(dateMin == null) {
                Toast.makeText(getContext(),
                        "Please enter start date before max", Toast.LENGTH_LONG).show();
                return;
            }

            datePickerDialog.getDatePicker().setMinDate(dateMin.getTimeInMillis());
            datePickerDialog.show();
        });
    }

    private void updateDateView() {
        if(dateMin == null) dateMinTextView.setText("");
        else dateMinTextView.setText(Utils.convertDateToString(dateMin.getTime()));

        if(dateMax == null) dateMaxTextView.setText("");
        else dateMaxTextView.setText(Utils.convertDateToString(dateMax.getTime()));
    }

    private void performSpinnerActions(int position) {
        switch (position) {
            case NOTHING:
                searchOptionLayout.setVisibility(GONE);
                dateMin = null;
                dateMax = null;
                updateDateView();
                break;

            case LESS_SIGN:
            case GREATER_SIGN:
                searchOptionLayout.setVisibility(VISIBLE);
                dateMaxTextView.setVisibility(GONE);
                dateMaxTextView.setText("");
                dateMinTextView.setHint("Date");
                dateMax = null;
                updateDateView();
                break;

            case BETWEEN_SIGN:
                searchOptionLayout.setVisibility(VISIBLE);
                dateMaxTextView.setVisibility(VISIBLE);
                dateMinTextView.setHint(getContext().getString(R.string.date_min));
                break;
        }

        showErrorMessage();
    }

    public String buildConditionsFromQuery(String propertyName, List<Object> queryParams) {
        StringBuilder builder = new StringBuilder();
        builder.append(" ( ");
        builder.append(propertyName);
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

    private void showErrorMessage(){
        if(!isUsed()){
            errorTextView.setVisibility(VISIBLE);
        }else{
            errorTextView.setVisibility(GONE);
        }
    }

    public boolean isUsed() {
        switch (spinner.getSelectedItemPosition()){
            case LESS_SIGN:
            case GREATER_SIGN:
                return dateMin != null;

            case BETWEEN_SIGN:
                return dateMin != null && dateMax != null;

            default:
                return false;
        }
    }

    public String getSign() {
        switch (spinner.getSelectedItemPosition()){
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
        switch (spinner.getSelectedItemPosition()){
            case LESS_SIGN:
            case GREATER_SIGN:
                return new QueryData(dateMin.getTimeInMillis());

            case BETWEEN_SIGN:
                return new QueryData(dateMin.getTimeInMillis(), dateMax.getTimeInMillis());

            default:
                return null;
        }
    }


//    public class QueryData{
//        private long startDate;
//        private long endDate;
//
//        QueryData(long startDate) {
//            this.startDate = startDate;
//        }
//
//        QueryData(long startDate, long endDate) {
//            this.startDate = startDate;
//            this.endDate = endDate;
//        }
//
//        long getStartDate() {
//            return startDate;
//        }
//
//        long getEndDate() {
//            return endDate;
//        }
//    }

}
