package com.openclassrooms.realestatemanager.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;

public class PropertyOptionView extends LinearLayout {
    private String mTitleString;
    private String mDescriptionString;
    private Drawable mIconDrawable;

    TextView titleTextView;
    TextView descriptionTextView;

    public PropertyOptionView(Context context) {
        super(context);
        init(null, 0);
    }

    public PropertyOptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PropertyOptionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load view
        View view = LayoutInflater.from(getContext()).inflate(R.layout.property_option_view, this);
        titleTextView = view.findViewById(R.id.titleTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.PropertyOptionView, defStyle, 0);

        mTitleString = a.getString(
                R.styleable.PropertyOptionView_prop_opt_title);

        mDescriptionString = a.getString(
                R.styleable.PropertyOptionView_prop_opt_description);

        if (a.hasValue(R.styleable.PropertyOptionView_prop_opt_icon)) {
            mIconDrawable = a.getDrawable(R.styleable.PropertyOptionView_prop_opt_icon);
            if(mIconDrawable != null) {
                mIconDrawable.setCallback(this);
            }
        }

        a.recycle();

        // Update text from attributes
        invalidateView();
    }

    private void invalidateView() {
        titleTextView.setText(mTitleString);
        descriptionTextView.setText(mDescriptionString);

        if(mIconDrawable != null) {
            titleTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(mIconDrawable, null, null, null);
        }
    }
}
