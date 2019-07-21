package com.openclassrooms.realestatemanager;

import android.os.Bundle;
import android.util.Log;

import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.viewmodels.UserViewModel;

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
        Log.i("EditPropertyActivity", "CreatePropertyActivity save");
//        User user = mUserViewModel.getCurrentUser().getValue();

        //Save
        Property property = propertyInfo.property;
        property.setPropertyTypeId(propertyInfo.propertyType.getId());
        property.setUserId(UserViewModel.USER_ID);

        mPropertyViewModel.createProperty(property);

        finish();
    }
}
