package com.openclassrooms.realestatemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.realestatemanager.fragments.DetailsPropertyFragment;
import com.openclassrooms.realestatemanager.fragments.PropertyListFragment;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements PropertyListFragment.OnFragmentDispatchListener {

    @Nullable
    @BindView(R.id.details_property_fragment)
    FrameLayout mDetailsFrameLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.mapFab)
    FloatingActionButton mapFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        if(mDetailsFrameLayout != null) {
            Log.i("Estate", "mDetailsFrameLayout cool !");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(Utils.isInternetAvailable(this)){
            mapFab.show();
        }else{
            mapFab.hide();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_add:
                createNewPropertyActivity();
                return true;

            case R.id.app_bar_search:
                searchPropertyActivity();
                return true;

            case R.id.app_bar_settings:
                settingsActivity();
                return true;

            default:
                return false;
        }
    }

    private void createNewPropertyActivity() {
        Intent intent = new Intent(this, CreatePropertyActivity.class);
        startActivity(intent);
    }

    private void searchPropertyActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    private void settingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
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

    private void showDetailsInActivity(long propertyId) {
        Intent intent = new Intent(this, PropertyDetailsActivity.class);
        intent.putExtra(PropertyDetailsActivity.EXTRA_PROPERTY_ID, propertyId);
        startActivity(intent);
    }

    private void showDetailsInFragment(long propertyId) {
        if(mDetailsFrameLayout == null) return;

        DetailsPropertyFragment fragment = DetailsPropertyFragment.newInstance(propertyId);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.details_property_fragment, fragment);
        transaction.commit();
    }

    @OnClick(R.id.mapFab)
    public void onMapFabClick() {
        if(!Utils.isInternetAvailable(this)) {
            Toast.makeText(this,
                    "Internet is not available, please make sure it has been activated",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

}
