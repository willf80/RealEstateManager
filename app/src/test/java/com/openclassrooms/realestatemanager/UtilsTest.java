package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;

import java.util.Calendar;

import static junit.framework.TestCase.assertEquals;

public class UtilsTest {

    @Test
    public void should_show_date_in_french_format() {
        // Arrange
        Calendar now = Calendar.getInstance();
        now.clear();
        now.set(2019, 7, 7); // 07/08/2019

        // Act
        String currentDateString = Utils.getTodayDate(now.getTime());

        // Assert
        assertEquals("07/08/2019", currentDateString);
    }

    @Test
    public void should_convert_euros_to_dollars() {
        // Arrange

        // Act

        // Assert
        assertEquals(1, Utils.convertEuroToDollar(1));
        assertEquals(25, Utils.convertEuroToDollar(20));
        assertEquals(369, Utils.convertEuroToDollar(300));
        assertEquals(119458, Utils.convertEuroToDollar(97000));
    }

    @Test
    public void should_convert_dollars_to_euros() {
        // Arrange

        // Act

        // Assert
        assertEquals(1, Utils.convertDollarToEuro(1));
        assertEquals(20, Utils.convertDollarToEuro(25));
        assertEquals(300, Utils.convertDollarToEuro(369));
        assertEquals(97000, Utils.convertDollarToEuro(119458));
    }

}


