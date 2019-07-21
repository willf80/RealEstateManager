package com.openclassrooms.realestatemanager.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injection.ServicesInjection;
import com.openclassrooms.realestatemanager.models.InterestPoint;
import com.openclassrooms.realestatemanager.services.InterestPointsAddingViewService;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class InterestPointsAddingView extends LinearLayout {

    AutoCompleteTextView completeTextView;
    TagContainerLayout tagLayout;
    Button btnAdd;

    private List<String> mPointOfInterestStringList;
    private ArrayAdapter<String> mPointOfInterestAdapter;
    private InterestPointsAddingViewService mPointsAddingViewService;

    public InterestPointsAddingView(Context context) {
        super(context);

        init();
    }

    public InterestPointsAddingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public InterestPointsAddingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mPointsAddingViewService = ServicesInjection.provideServiceFactory()
                .create(InterestPointsAddingViewService.class);

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.interest_points_view, this);

        completeTextView = view.findViewById(R.id.pointsOfInterestAutoComplete);
        btnAdd = view.findViewById(R.id.addPointOfInterestTagButton);
        tagLayout = view.findViewById(R.id.tagContainerLayout);

        mPointOfInterestStringList = new ArrayList<>();

        mPointOfInterestAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.select_dialog_item, mPointOfInterestStringList);

        completeTextView.setThreshold(1);
        completeTextView.setAdapter(mPointOfInterestAdapter);

        listeners();
    }

    private void listeners() {
        tagLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) { }

            @Override
            public void onTagLongClick(int position, String text) { }

            @Override
            public void onSelectedTagDrag(int position, String text) { }

            @Override
            public void onTagCrossClick(int position) {
                String tag = tagLayout.getTagText(position);
                mPointsAddingViewService.removeTagInHashTable(tag);
                tagLayout.removeTag(position);
            }
        });

        btnAdd.setOnClickListener((v) -> {
            String tag = completeTextView.getText().toString();

            if(tag.isEmpty()){
                return;
            }

            if(mPointsAddingViewService.isKeyExist(tag)) {
                Toast.makeText(getContext(),
                        String.format("Can't add duplicated interest point : [%s] already added", tag),
                        Toast.LENGTH_LONG)
                        .show();
                return;
            }

            if(mPointsAddingViewService.isNewInterestPoint(tag)) {
                showConfirmToAddNewInterestPointDialog(tag);
                return;
            }

            addTagView(tag, false);
        });
    }

    private void addTagView(String tag, boolean isNew) {
        tagLayout.addTag(tag);
        mPointsAddingViewService.addTagWithState(tag, isNew);
        completeTextView.setText("");
    }

    public void loadInterestPointAutoCompleteList(List<InterestPoint> interestPointList) {
        mPointsAddingViewService.setInterestPointList(interestPointList);

        mPointOfInterestStringList = new ArrayList<>();
        for (InterestPoint point : interestPointList) {
            mPointOfInterestStringList.add(point.getLabel());
        }

        mPointOfInterestAdapter.clear();
        mPointOfInterestAdapter.addAll(mPointOfInterestStringList);
        mPointOfInterestAdapter.notifyDataSetChanged();
    }

    private void showConfirmToAddNewInterestPointDialog(String interestPoint) {
        String message = String
                .format("%s does not exist, may you want to add this interest point ?", interestPoint);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("New interest point")
                .setMessage(message)
                .setNegativeButton("No", null)
                .setPositiveButton("Yes, add",
                        (dialog1, which) -> addTagView(interestPoint, true))
                .create();
        dialog.show();
    }

    public Dictionary<String, Boolean> getInterestPointList() {
        return mPointsAddingViewService.getInterestPointAddedState();
    }
}
