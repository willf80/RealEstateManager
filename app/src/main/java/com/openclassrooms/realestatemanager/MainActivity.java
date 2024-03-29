package com.openclassrooms.realestatemanager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.openclassrooms.realestatemanager.fragments.DetailsPropertyFragment;
import com.openclassrooms.realestatemanager.fragments.PropertyListFragment;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.List;

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

        setPermissions();
    }

    private void setPermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                })
                .withErrorListener(error -> {

                })
                .check();
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

            case R.id.app_bar_loan:
                mortgageLoanSimulatorActivity();
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

    private void mortgageLoanSimulatorActivity() {
        Intent intent = new Intent(this, MortgageSimulatorActivity.class);
        startActivity(intent);
    }

    private void settingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, SettingsActivity.REQUEST_CODE);
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

    private void reloadPropertyListInFragment() {
        PropertyListFragment fragment = (PropertyListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.list_property_fragment);
        if(fragment == null) return;
        fragment.updateCurrencyShowed();
    }

    @OnClick(R.id.mapFab)
    public void onMapFabClick() {
        if(!Utils.isInternetAvailable(this)) {
            Toast.makeText(this,
                    this.getString(R.string.internet_not_available),
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == SettingsActivity.RESULT_CODE && requestCode == SettingsActivity.REQUEST_CODE) {
            reloadPropertyListInFragment();
        }
    }
}
