package com.openclassrooms.realestatemanager;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.openclassrooms.realestatemanager.fragments.DetailsPropertyFragment;
import com.openclassrooms.realestatemanager.fragments.PropertyListFragment;
import com.openclassrooms.realestatemanager.models.Property;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements PropertyListFragment.OnFragmentDispatchListener {

    @Nullable
    @BindView(R.id.details_property_fragment)
    FrameLayout mDetailsFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(mDetailsFrameLayout != null) {
            Log.i("Estate", "mDetailsFrameLayout cool !");
        }
    }

    @Override
    public void onItemClick(Property property) {
        manageViewing(property);
    }

    private void manageViewing(Property property) {
        if(mDetailsFrameLayout == null) {
            showDetailsInActivity(property.getId());
            return;
        }

        showDetailsInFragment(property.getId());
    }

    private void showDetailsInActivity(String id) {
        Intent intent = new Intent(this, PropertyDetailsActivity.class);
        intent.putExtra(PropertyDetailsActivity.EXTRA_PROPERTY_ID, id);
        startActivity(intent);
    }

    private void showDetailsInFragment(String id) {
        if(mDetailsFrameLayout == null) return;

        DetailsPropertyFragment fragment = DetailsPropertyFragment.newInstance(id);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.details_property_fragment, fragment);
        transaction.commit();
    }

}
