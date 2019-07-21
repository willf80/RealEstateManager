package com.openclassrooms.realestatemanager.services;

import com.openclassrooms.realestatemanager.models.InterestPoint;

import java.util.ArrayList;
import java.util.List;

public class InterestPointsAddingViewService {

    private List<String> mTagList;
    private List<InterestPoint> mInterestPointList;
    private List<InterestPoint> mInterestPointSelectedList;

    public InterestPointsAddingViewService() {
        mInterestPointSelectedList = new ArrayList<>();
        mTagList = new ArrayList<>();
        mInterestPointList = new ArrayList<>();
    }

    private boolean addNewTag(String tag) {
        if(isAlreadyAdded(tag))return false;
        mTagList.add(tag);

        return true;
    }

    public boolean isAlreadyAdded(String tag){
        if(tag == null)
            return false;

        boolean isExist = false;

        for (InterestPoint interestPoint : mInterestPointSelectedList) {
            String label = interestPoint.getLabel();

            if (!tag.equalsIgnoreCase(label)) continue;

            isExist = true;
            break;
        }

        return isExist;
    }

    public void addTag(String tag) {
        if(!addNewTag(tag)) return;

        InterestPoint interestPoint = searchSelectedInterestPoint(tag);
        if(interestPoint == null) {
            interestPoint = new InterestPoint();
            interestPoint.setLabel(tag);
        }

        mInterestPointSelectedList.add(interestPoint);
    }

    private InterestPoint searchSelectedInterestPoint(String tag) {
        InterestPoint interestPoint = null;
        for (InterestPoint ip : mInterestPointSelectedList) {
            if (!ip.getLabel().equalsIgnoreCase(tag)) continue;
            interestPoint = ip;
            break;
        }

        return interestPoint;
    }

    public void removeTagInHashTable(String tag) {
        InterestPoint interestPoint = searchSelectedInterestPoint(tag);
        if(interestPoint == null) return;

        mInterestPointSelectedList.remove(interestPoint);
    }

    public boolean isNewInterestPoint(String text) {
        boolean isNew = true;

        for (InterestPoint point : mInterestPointList) {
            if (!text.equalsIgnoreCase(point.getLabel())) continue;

            isNew = false;
            break;
        }

        return isNew;
    }

    public List<InterestPoint> getInterestPointSelectedList() {
        return mInterestPointSelectedList;
    }

    public List<String> getTagList() {
        return mTagList;
    }

    public void setInterestPointList(List<InterestPoint> interestPointList) {
        mInterestPointList = interestPointList;
    }
}
