package com.openclassrooms.realestatemanager;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.realestatemanager.dal.AppDatabase;
import com.openclassrooms.realestatemanager.dal.PrepopulateHelper;
import com.openclassrooms.realestatemanager.models.InterestPoint;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyInterestPoints;
import com.openclassrooms.realestatemanager.models.PropertyInterestPointsDisplayInfo;
import com.openclassrooms.realestatemanager.models.PropertyType;
import com.openclassrooms.realestatemanager.utils.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PropertyDbTest {

    private AppDatabase mAppDatabase;

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() {
        this.mAppDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                AppDatabase.class)
                .allowMainThreadQueries()
                .addCallback(prepopulateDatabase())
                .build();
    }

    private static RoomDatabase.Callback prepopulateDatabase() {
        return new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                PrepopulateHelper.prepopulateUser(db);
                PrepopulateHelper.prepopulateInterestPointList(db);
                PrepopulateHelper.prepopulatePropertyTypeList(db);
            }
        };
    }

    @After
    public void closeDb() {
        mAppDatabase.close();
    }

    @Test
    public void insertAndGetType() throws InterruptedException {
        // Arrange

        // Action
        List<PropertyType> propertyTypeList =
                LiveDataTestUtil.getValue(this.mAppDatabase
                        .propertyTypeDao()
                        .getPropertyTypes());

        // Assert
        assertTrue(propertyTypeList.size() > 0);
        assertEquals("Apartment", propertyTypeList.get(0).getLabel());
    }

    private Property createNewProperty() {
        Property property = new Property();
        property.setDescription("Test");
        property.setPropertyTypeId(1);
        property.setUserId(1);

        return property;
    }

    @Test
    public void insertAndGetProperty() throws InterruptedException {
        // Arrange
        Property property = createNewProperty();

        // Action
        this.mAppDatabase.propertyDao().insert(property);
        List<Property> propertyList = LiveDataTestUtil.getValue(this.mAppDatabase.propertyDao().getProperties());

        // Assert
        assertTrue(propertyList.size() > 0);
        assertEquals(1, propertyList.get(0).getUserId());
    }

    @Test
    public void should_create_two_propertyInterestPoints() throws InterruptedException {
        // Arrange
        final long INTEREST_POINT_1_ID = 1;
        final long INTEREST_POINT_2_ID = 2;

        Property property = createNewProperty();

        // Action
        long propertyId = this.mAppDatabase.propertyDao().insert(property);

        PropertyInterestPoints propertyInterestPoints1 =
                new PropertyInterestPoints(propertyId, INTEREST_POINT_1_ID);

        PropertyInterestPoints propertyInterestPoints2 =
                new PropertyInterestPoints(propertyId, INTEREST_POINT_2_ID);

        long ipId1 = this.mAppDatabase.interestPointDao().insert(propertyInterestPoints1);
        long ipId2 = this.mAppDatabase.interestPointDao().insert(propertyInterestPoints2);

        List<Long> propertyInterestPointIds
                = LiveDataTestUtil.getValue(this.mAppDatabase
                    .interestPointDao()
                    .getPropertyInterestPointsIds(propertyId));

        List<InterestPoint> interestPointsList = LiveDataTestUtil.getValue(
                this.mAppDatabase
                        .interestPointDao()
                        .getInterestPointList(propertyInterestPointIds));

        // Assert
        assertEquals(1, ipId1);
        assertEquals(2, ipId2);
        assertEquals(2, interestPointsList.size());
        assertEquals(2, interestPointsList.get(1).getId());
    }
}
