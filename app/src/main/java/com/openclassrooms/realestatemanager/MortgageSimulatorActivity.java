package com.openclassrooms.realestatemanager;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MortgageSimulatorActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.bottomSheetContainer)
    CardView bottomSheetContainer;

    @BindView(R.id.cardView)
    CardView cardView;

    @BindView(R.id.simulationResultLayout)
    LinearLayout simulationResultLayout;

    @BindView(R.id.rangeSeekBar)
    RangeSeekBar rangeSeekBar;

    @BindView(R.id.expandImageView)
    ImageView expandImageView;

    @BindView(R.id.amountEditText)
    TextInputEditText amountEditText;

    @BindView(R.id.interestRateEditText)
    TextInputEditText interestRateEditText;

    @BindView(R.id.amountPerMonthTextView)
    TextView amountPerMonthTextView;

    @BindView(R.id.simulationInstructionsTextView)
    TextView simulationInstructionsTextView;

    BottomSheetBehavior bottomSheetBehavior;
    int year = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mortgage_simulator);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        showReturnHome();

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer);
        rangeSeekBar.setIndicatorTextDecimalFormat("0");

        listener();

        // Set default calculator view to expanded
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void listener(){
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    setExpandToLessConfigurations();
                }else{
                    setExpandToMoreConfigurations();
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        rangeSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                year = Math.round(leftValue);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });
    }

    @OnClick(R.id.expandImageView)
    public void onExpand(){
        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }else{
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    private void setExpandToLessConfigurations(){
        expandImageView.setImageResource(R.drawable.ic_expand_less_black_24dp);
        changeLayoutGravity(Gravity.CENTER);
    }

    private void setExpandToMoreConfigurations(){
        expandImageView.setImageResource(R.drawable.ic_expand_more_black_24dp);
        changeLayoutGravity(Gravity.TOP);
    }

    private void changeLayoutGravity(int gravity){
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) cardView.getLayoutParams();
        layoutParams.gravity = gravity;
        cardView.setLayoutParams(layoutParams);
    }

    @OnClick(R.id.simulateButton)
    public void simulate(){
        String amountString = amountEditText.getText().toString();
        String interestRateString = interestRateEditText.getText().toString();

        if(amountString.isEmpty() || interestRateString.isEmpty()){
            Toast.makeText(this, "Please enter a valid information", Toast.LENGTH_LONG).show();
            return;
        }

        double capital = Double.parseDouble(amountString);
        double interestRate = Double.parseDouble(interestRateString);

        double amountMonth = Utils.simulateMortgageLoan(capital, interestRate, year);
        amountPerMonthTextView.setText(String.format(Locale.getDefault(), "%.2f $/month", amountMonth));

        showResultLayout();
    }

    private void showResultLayout(){
        simulationInstructionsTextView.setVisibility(View.GONE);
        simulationResultLayout.setVisibility(View.VISIBLE);
    }

}
