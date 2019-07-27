package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.net.wifi.WifiManager;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Address;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * 0.812);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    public static String getTodayDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    public static Boolean isInternetAvailable(Context context){
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        return wifi.isWifiEnabled();
    }

    public static String buildFullAddressMapImageUrl(Context context, String fullAddress){

        return String.format(context.getString(R.string.url_static_map), fullAddress,
                context.getString(R.string.google_api_key));
    }

    public static String getFrenchCurrencyFormatted(double money){
        return String.format("%s%s", getCurrency(Locale.FRENCH, money), "€");
    }

    public static String getEnglishCurrencyFormatted(double money){
        return String.format("%s%s", "$", getCurrency(Locale.US, money));
    }

    private static String getCurrency(Locale locale, double money) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;

        return decimalFormat.format(money);
    }

    public static String getPropertyTitle(Property property, PropertyType propertyType) {
        if(property == null || propertyType == null ) return "";
        return String.format(Locale.getDefault(),
                "%s with %d rooms %.0f sq m", propertyType.getLabel(),
                property.getNumberOfRooms(), property.getArea());
    }

    public static String getPropertyCompleteAddress(Property property, Address address) {
        return String.format(Locale.getDefault(),
                "%s, %s, New York, NY %s, United States",
                address.getAddressLine1(),
                property.getAddressLine2(),
                address.getPostalCode());
    }
}
