package com.openclassrooms.realestatemanager;

import android.os.Bundle;
import android.util.Log;

public class EditPropertyActivity extends AbstractPropertyEditionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Edit property");
    }

    @Override
    public Mode definedMode() {
        return Mode.Modification;
    }

    @Override
    public void save(PropertyInfo propertyInfo) {
        Log.i("EditPropertyActivity", "EditPropertyActivity save");
    }
}
