package com.openclassrooms.realestatemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class AbstractPropertyEditionActivity extends BaseActivity {

    private Mode mMode;

    @BindView(R.id.btnCreateProperty)
    Button mCreatePropertyButton;

    @BindView(R.id.btnEditProperty)
    Button mEditPropertyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_edition);
        ButterKnife.bind(this);
        showReturnHome();

        mMode = definedMode();
        if(mMode == null) {
            throw new RuntimeException(toString() + " mode cannot be null.");
        }

        // 1.Configuration
        configureViews();
    }

    public abstract Mode definedMode();

    public abstract void save();

    private void configureViews() {
        if(mMode == Mode.Creation) {
            mCreatePropertyButton.setVisibility(View.VISIBLE);
            mEditPropertyButton.setVisibility(View.GONE);
        } else {
            mCreatePropertyButton.setVisibility(View.GONE);
            mEditPropertyButton.setVisibility(View.VISIBLE);
        }

        listeners();
    }

    private void listeners() {
        mCreatePropertyButton.setOnClickListener(v -> save());
        mEditPropertyButton.setOnClickListener(v -> save());
    }

    public enum Mode {
        Creation, Modification
    }
}
