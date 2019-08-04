package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Address;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyType;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
     * Conversion d'un prix d'un bien immobilier (Euros vers Dollars)
     * @param euros
     * @return
     */
    public static int convertEuroToDollar(int euros){
        return (int) Math.round(euros / 0.812);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return la date du jour au format jour/mois/année
     */
    public static String getTodayDate(){
        return getTodayDate(new Date());
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return la date du jour au format jour/mois/année
     */
    public static String getTodayDate(Date date){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context application context
     * @return true s'il y a connexion internet et false dans le cas contraire
     */
    public static boolean isInternetAvailable(Context context){
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
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

    public static String getPropertySoldMessage(Date date, double money) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd',' yyyy", Locale.getDefault());

        String moneyText = getEnglishCurrencyFormatted(money);
        String dateString = simpleDateFormat.format(date);

        return String.format("This property was sold for %s in %s", moneyText, dateString);
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
