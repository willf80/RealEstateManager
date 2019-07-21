package com.openclassrooms.realestatemanager;

import android.os.Bundle;

import com.openclassrooms.realestatemanager.models.PropertyInfo;

public class CreatePropertyActivity extends AbstractPropertyEditionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("New property");
    }

    @Override
    public Mode definedMode() {
        return Mode.Creation;
    }

    @Override
    public void save(PropertyInfo propertyInfo) {
        mPropertyViewModel.createProperty(this, propertyInfo);
        finish();
    }
}
