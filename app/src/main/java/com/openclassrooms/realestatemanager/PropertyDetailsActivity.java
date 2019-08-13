package com.openclassrooms.realestatemanager;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.openclassrooms.realestatemanager.fragments.DetailsPropertyFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PropertyDetailsActivity extends BaseActivity {

    public static final String EXTRA_PROPERTY_ID = "propertyId";

    long propertyId;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        showReturnHome();

        propertyId = getIntent().getLongExtra(EXTRA_PROPERTY_ID, 0);

        configFrameLayout();
    }

    private void configFrameLayout() {
        DetailsPropertyFragment detailsPropertyFragment = DetailsPropertyFragment.newInstance(propertyId);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, detailsPropertyFragment);
        fragmentTransaction.commit();
    }
}
