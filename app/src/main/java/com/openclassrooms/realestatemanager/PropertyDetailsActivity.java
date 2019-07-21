package com.openclassrooms.realestatemanager;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import com.openclassrooms.realestatemanager.fragments.DetailsPropertyFragment;

public class PropertyDetailsActivity extends BaseActivity implements DetailsPropertyFragment.OnDetailsDispatchListener {

    public static final String EXTRA_PROPERTY_ID = "propertyId";

    long propertyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);
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

    @Override
    public void onPropertyMarkAsSell() {

    }
}
