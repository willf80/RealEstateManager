package com.openclassrooms.realestatemanager.views;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
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
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.Calendar;
import java.util.Date;

public class SearchDateQueryBuilderView extends LinearLayout {

    String mTitle;
    String mTableName;
    String mPropertyName;

    Spinner spinner;
    LinearLayout searchOptionLayout;
    TextView searchTitleView;
    TextView dateMinTextView;
    TextView dateMaxTextView;

    Calendar dateMin;
    Calendar dateMax;

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
            case 0:
                searchOptionLayout.setVisibility(GONE);
                dateMin = null;
                dateMax = null;
                updateDateView();
                break;

            case 1:
            case 2:
                searchOptionLayout.setVisibility(VISIBLE);
                dateMaxTextView.setVisibility(GONE);
                dateMaxTextView.setText("");
                dateMinTextView.setHint("Date");
                dateMax = null;
                updateDateView();
                break;

            case 3:
                searchOptionLayout.setVisibility(VISIBLE);
                dateMaxTextView.setVisibility(VISIBLE);
                dateMinTextView.setHint(getContext().getString(R.string.date_min));
                break;
        }
    }

    public boolean isUsed() {
        switch (spinner.getSelectedItemPosition()){
            case 1:
            case 2:
                return dateMin != null;

            case 3:
                return dateMin != null && dateMax != null;

            default:
                return false;
        }
    }

    public String getSign() {
        switch (spinner.getSelectedItemPosition()){
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
        switch (spinner.getSelectedItemPosition()){
            case 1:
            case 2:
                return new QueryData(dateMin.getTime());

            case 3:
                return new QueryData(dateMin.getTime(), dateMax.getTime());

            default:
                return null;
        }
    }


    public class QueryData{
        private Date startDate;
        private Date endDate;

        public QueryData() {
        }

        QueryData(Date startDate) {
            this.startDate = startDate;
        }

        QueryData(Date startDate, Date endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }
    }

}
