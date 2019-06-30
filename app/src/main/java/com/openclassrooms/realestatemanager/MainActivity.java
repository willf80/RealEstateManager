package com.openclassrooms.realestatemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.openclassrooms.realestatemanager.fragments.DetailsPropertyFragment;
import com.openclassrooms.realestatemanager.fragments.PropertyListFragment;
import com.openclassrooms.realestatemanager.models.Property;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements PropertyListFragment.OnFragmentDispatchListener {

    @Nullable
    @BindView(R.id.details_property_fragment)
    FrameLayout mDetailsFrameLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_add:
                createNewProperty();
                return true;
            case R.id.app_bar_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createNewProperty() {
        Intent intent = new Intent(this, CreatePropertyActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(Property property) {
        manageViewing(property);
    }

    private void manageViewing(Property property) {
        if(mDetailsFrameLayout == null) {
            showDetailsInActivity(property.getId() + "");
            return;
        }

        showDetailsInFragment(property.getId() + "");
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
