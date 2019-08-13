package com.openclassrooms.realestatemanager.dal;

import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.room.OnConflictStrategy;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class PrepopulateHelper {

    public static void prepopulateUser(@NonNull SupportSQLiteDatabase db) {
        final String tableName = "User";

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", 1);

        db.insert(tableName, OnConflictStrategy.IGNORE, contentValues);
    }

    public static void prepopulatePropertyTypeList(@NonNull SupportSQLiteDatabase db) {
        final String tableName = "PropertyType";

        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("id", 1);
        contentValues1.put("label", "Flat");

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("id", 2);
        contentValues2.put("label", "Loft");

        ContentValues contentValues3 = new ContentValues();
        contentValues3.put("id", 3);
        contentValues3.put("label", "Manor");

        db.insert(tableName, OnConflictStrategy.IGNORE, contentValues1);
        db.insert(tableName, OnConflictStrategy.IGNORE, contentValues2);
        db.insert(tableName, OnConflictStrategy.IGNORE, contentValues3);
    }

    public static void prepopulateInterestPointList(@NonNull SupportSQLiteDatabase db) {
        final String tableName = "InterestPoint";

        String[] listOfPointOfInterest = new String[]{
                "Airport", "Restaurant", "Bank", "ATM", "Hotel", "Pub", "Bus station",
                "Railway station", "Cinema", "Hospital", "College", "School", "Pharmacy",
                "Supermarket", "Fuel", "Gym", "Place of worship", "Toilet", "Park", "stadium"
        };

        for (int i = 0; i < listOfPointOfInterest.length; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", i + 1);
            contentValues.put("label", listOfPointOfInterest[i]);

            db.insert(tableName, OnConflictStrategy.IGNORE, contentValues);
        }
    }

}

