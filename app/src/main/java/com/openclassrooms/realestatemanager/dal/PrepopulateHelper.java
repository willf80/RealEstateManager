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

        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("id", 1);
        contentValues1.put("label", "Metro");

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("id", 2);
        contentValues2.put("label", "School");

        ContentValues contentValues3 = new ContentValues();
        contentValues3.put("id", 3);
        contentValues3.put("label", "Shops");

        ContentValues contentValues4 = new ContentValues();
        contentValues4.put("id", 4);
        contentValues4.put("label", "Park");


        db.insert(tableName, OnConflictStrategy.IGNORE, contentValues1);
        db.insert(tableName, OnConflictStrategy.IGNORE, contentValues2);
        db.insert(tableName, OnConflictStrategy.IGNORE, contentValues3);
        db.insert(tableName, OnConflictStrategy.IGNORE, contentValues4);
    }

}

