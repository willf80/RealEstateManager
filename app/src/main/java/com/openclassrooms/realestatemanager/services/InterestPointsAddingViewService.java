package com.openclassrooms.realestatemanager.services;

import com.openclassrooms.realestatemanager.models.InterestPoint;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class InterestPointsAddingViewService {

    private Dictionary<String, Boolean> mInterestPointAddedState;
    private List<String> mTagList;
    private List<InterestPoint> mInterestPointList;

    public InterestPointsAddingViewService() {
        mInterestPointAddedState = new Hashtable<>();
        mTagList = new ArrayList<>();
        mInterestPointList = new ArrayList<>();
    }

    private boolean addTag(String tag) {
        if(isKeyExist(tag))return false;

        mTagList.add(tag);

        return true;
    }

    public boolean isKeyExist(String key){
        if(key == null)
            return false;

        boolean isExist = false;

        Enumeration<String> keyList = mInterestPointAddedState.keys();
        while (keyList.hasMoreElements()){
            String iKey = keyList.nextElement();

            if (!key.equalsIgnoreCase(iKey)) continue;

            isExist = true;
            break;
        }

        return isExist;
    }

    public void addTagWithState(String tag, boolean isNew) {
        if(!addTag(tag)) return;
        mInterestPointAddedState.put(tag, isNew);
    }

    public void removeTagInHashTable(String tag) {
        mInterestPointAddedState.remove(tag);
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

    public Dictionary<String, Boolean> getInterestPointAddedState() {
        return mInterestPointAddedState;
    }

    public List<String> getTagList() {
        return mTagList;
    }

    public void setInterestPointList(List<InterestPoint> interestPointList) {
        mInterestPointList = interestPointList;
    }
}
