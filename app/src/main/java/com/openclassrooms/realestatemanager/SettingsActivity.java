package com.openclassrooms.realestatemanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class SettingsActivity extends BaseActivity {
    public static final String SHARED_PREF_NAME = "settings";
    public static final String SHARED_PREF_KEY_IS_EURO = "isEuro";
    public static final int REQUEST_CODE = 100;
    public static final int RESULT_CODE = 101;

    @BindView(R.id.switchCurrencyChange)
    Switch switchCurrencyChange;

    @BindView(R.id.currentCurrencyTextView)
    TextView currentCurrencyTextView;

    SharedPreferences sharedPreferences;
    boolean isEuro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        showReturnHome();

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        loadSettings();
    }

    private void loadSettings() {
        isEuro = sharedPreferences.getBoolean(SHARED_PREF_KEY_IS_EURO, false);
        applyDescriptionText(isEuro);
        switchCurrencyChange.setChecked(isEuro);
    }

    private void applyDescriptionText(boolean isEuro) {
        if (isEuro) {
            currentCurrencyTextView.setText(getText(R.string.current_currency_is_the_euro));
        }else {
            currentCurrencyTextView.setText(getText(R.string.current_currency_is_the_dollar));
        }
    }

    @OnCheckedChanged(R.id.switchCurrencyChange)
    public void onChangeCurrentSwitch(CompoundButton buttonView, boolean isChecked) {

        applyDescriptionText(isChecked);

        sharedPreferences.edit()
                .putBoolean(SHARED_PREF_KEY_IS_EURO, isChecked)
                .apply();

        if(isChecked != isEuro) setResult(RESULT_CODE);
    }
}
