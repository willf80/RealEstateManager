package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.injection.ServicesInjection;
import com.openclassrooms.realestatemanager.services.InterestPointsAddingViewService;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InterestPointsAddingViewServiceTest {

    private InterestPointsAddingViewService mPointsAddingViewService;

    @Before
    public void setUp() throws IllegalArgumentException {
        mPointsAddingViewService = ServicesInjection
                .provideServiceFactory()
                .create(InterestPointsAddingViewService.class);
    }

    @Test
    public void should_create_interestPointsService(){
        // Arrange

        // Act

        // Assert
        assertNotNull(mPointsAddingViewService);
    }

    @Test
    public void should_add_newTag(){
        // Arrange
        String expectedText = "Hello";

        // Act
        mPointsAddingViewService.addTag("Hello");

        // Assert
        assertEquals(expectedText, mPointsAddingViewService.getTagList().get(0));
    }

    @Test
    public void should_does_not_add_duplicated_tag(){
        // Arrange
        int expectedTotalCount = 1;

        // Act
        mPointsAddingViewService.addTag("Hello");
        mPointsAddingViewService.addTag("Hello");

        // Assert
        assertEquals(expectedTotalCount, mPointsAddingViewService.getTagList().size());
    }

    @Test
    public void should_remove_existingTag(){
        // Arrange

        // Act
        mPointsAddingViewService.addTag("Hello");
        mPointsAddingViewService.removeTagInHashTable("Hello");

        // Assert
        assertEquals(0, mPointsAddingViewService.getInterestPointSelectedList().size());
    }
}
