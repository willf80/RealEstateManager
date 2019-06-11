package com.openclassrooms.realestatemanager;

import android.os.Bundle;
import android.util.Log;

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
    public void save() {
        Log.i("CreatePropertyActivity", "CreatePropertyActivity save");
    }
}
