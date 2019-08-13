package com.openclassrooms.realestatemanager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.dal.AppDatabase;
import com.openclassrooms.realestatemanager.provider.MediaContentProvider;
import com.openclassrooms.realestatemanager.provider.PropertyContentProvider;
import com.openclassrooms.realestatemanager.provider.PropertyTypeContentProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ContentProviderTest {

    private ContentResolver mContentResolver;

    @Before
    public void setUp(){
        Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().getContext(),
                AppDatabase.class)
            .allowMainThreadQueries().build();

        mContentResolver = InstrumentationRegistry.getInstrumentation()
                .getContext().getContentResolver();
    }

    @Test
    public void should_get_properties(){

        final Cursor cursor = mContentResolver
                .query(PropertyContentProvider.URI_PROPERTY, null, null, null);

        assertNotNull(cursor);
        assertTrue(cursor.getCount() > 0);
    }

    @Test
    public void should_get_propertyType_of_property(){
        // Arrange
        final String labelExpected = "Loft";

        // Act
        final Cursor cursorProperty = mContentResolver
                .query(PropertyContentProvider.URI_PROPERTY, null, null, null);

        assertNotNull(cursorProperty);
        cursorProperty.moveToFirst();
        int propertyTypeIdIndex = cursorProperty.getColumnIndex("propertyTypeId");
        int propertyTypeId = cursorProperty.getInt(propertyTypeIdIndex);

        final Cursor cursor = mContentResolver
                .query(ContentUris.withAppendedId(PropertyTypeContentProvider.URI_PROPERTY_TYPE, propertyTypeId), null, null, null);

        // Assert
        assertNotNull(cursor);

        cursor.moveToFirst();
        int labelIndex = cursor.getColumnIndex("label");
        String label = cursor.getString(labelIndex);

        assertEquals(labelExpected, label);
    }

    @Test
    public void should_get_property_media(){
        // Arrange
        final long propertyId = 1;
        final long mediaCountElements = 2;

        // Act
        final Cursor cursor = mContentResolver
                .query(ContentUris.withAppendedId(MediaContentProvider.URI_MEDIA, propertyId), null, null, null);

        // Assert
        assertNotNull(cursor);
        assertEquals(mediaCountElements, cursor.getCount());
    }

}
