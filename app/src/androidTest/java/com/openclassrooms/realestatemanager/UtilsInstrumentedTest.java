package com.openclassrooms.realestatemanager;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class UtilsInstrumentedTest {
    private Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getContext();
    }

    @Test
    public void useAppTest() {
        assertEquals("com.openclassrooms.realestatemanager.test", context.getPackageName());
    }


    @Test
    public void should_have_internet() {

        // Arrange


        // Act
        boolean isInternetAvailable = Utils.isInternetAvailable(context);

        // Assert
        assertTrue(isInternetAvailable);
    }
}
