package com.openclassrooms.realestatemanager;

import android.util.Log;

public class EditPropertyActivity extends AbstractPropertyEditionActivity {
    @Override
    public Mode definedMode() {
        return Mode.Modification;
    }

    @Override
    public void save() {
        Log.i("EditPropertyActivity", "EditPropertyActivity save");
    }
}
