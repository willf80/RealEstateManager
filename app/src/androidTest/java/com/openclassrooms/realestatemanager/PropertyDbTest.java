package com.openclassrooms.realestatemanager;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.realestatemanager.dal.AppDatabase;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyType;
import com.openclassrooms.realestatemanager.models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

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
                .build();
    }

    @After
    public void closeDb() {
        mAppDatabase.close();
    }

    private PropertyType getPropertyType() {
        PropertyType propertyType = new PropertyType();
        propertyType.setId(1);
        propertyType.setLabel("Home");

        return propertyType;
    }

    private User getUser() {
        User user = new User();
        user.setId(1);
        user.setEmail("willyfkouadio@gmail.com");

        return user;
    }

    @Test
    public void insertAndGetType() throws InterruptedException {
        // Arrange
        PropertyType propertyType = new PropertyType();
        propertyType.setId(1);
        propertyType.setLabel("Home");

        this.mAppDatabase.propertyTypeDao().insert(propertyType);

        // Action
        List<PropertyType> propertyTypeList =
                LiveDataTestUtil.getValue(this.mAppDatabase
                        .propertyTypeDao()
                        .getPropertyTypes());

        // Assert
        assertTrue(propertyTypeList.size() > 0);
        assertEquals("Home", propertyTypeList.get(0).getLabel());
    }

    @Test
    public void insertAndGetProperty() throws InterruptedException {
        // Arrange
        PropertyType propertyType = getPropertyType();
        User user = getUser();

        long propertyTypeId = this.mAppDatabase.propertyTypeDao().insert(propertyType);
        long userId = this.mAppDatabase.userDao().insert(user);

        Property property = new Property();
        property.setId(1);
        property.setPropertyTypeId(propertyTypeId);
        property.setUserId(userId);

        this.mAppDatabase.propertyDao().insert(property);

        // Action
        List<Property> propertyList = LiveDataTestUtil.getValue(this.mAppDatabase.propertyDao().getProperties());

        // Assert
        assertTrue(propertyList.size() > 0);
        assertEquals(1, propertyList.get(0).getUserId());
    }
}
