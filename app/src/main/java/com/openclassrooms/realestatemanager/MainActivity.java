package com.openclassrooms.realestatemanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

public class MainActivity extends AppCompatActivity {

    @Nullable
    @BindView(R.id.details_bien_fragment)
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

//    private void configureTextViewMain(){
//        this.textViewMain.setTextSize(15);
//        this.textViewMain.setText("Le premier bien immobilier enregistré vaut ");
//    }
//
//    private void configureTextViewQuantity(){
//        int quantity = Utils.convertDollarToEuro(100);
//        this.textViewQuantity.setTextSize(20);
//        this.textViewQuantity.setText(String.format(Locale.getDefault(), "%d", quantity));
//    }
}
