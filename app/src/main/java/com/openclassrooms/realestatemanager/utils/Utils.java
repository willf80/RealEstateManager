package com.openclassrooms.realestatemanager.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.SettingsActivity;
import com.openclassrooms.realestatemanager.models.Address;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyType;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    public static String convertDateToString(){
        return convertDateToString(new Date());
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return la date du jour au format jour/mois/année
     */
    public static String convertDateToString(Date date){
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

        return String.format(context.getString(R.string.url_static_mapquest),
                fullAddress, fullAddress,
                context.getString(R.string.mapquest_api_key));
    }

    public static boolean getCurrencySettings(Context context) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(SettingsActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return  sharedPreferences
                .getBoolean(SettingsActivity.SHARED_PREF_KEY_IS_EURO, false);
    }

    public static String getCurrencyMoney(double money, boolean isEuro) {
        if(isEuro) {
            return getFrenchCurrencyFormatted(convertDollarToEuro((int) money));
        }

        return getEnglishCurrencyFormatted(money);
    }

    private static String getFrenchCurrencyFormatted(double money){
        return String.format("%s%s", getCurrency(Locale.FRENCH, money), "€");
    }

    private static String getEnglishCurrencyFormatted(double money){
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
                "%s, %s\n%s",
                address.getAddressLine1(),
                address.getPostalCode(),
                property.getAddressLine2());
    }

    public static String getLocationAddressForStaticMap(Address address){
        return getLocationAddressForStaticMap(address.getAddressLine1(), address.getPostalCode());
    }

    public static String getLocationAddressForStaticMap(String addressLine1, String postalCode){
        String fullAddress = addressLine1  + ", " + postalCode;
        try {
            return URLEncoder.encode(fullAddress, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void showNotification(Context context, String notificationMessage){
        String CHANNEL_ID = "default";

        createNotificationChannel(context, CHANNEL_ID);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(notificationMessage)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationMessage))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        // notify user
        notificationManagerCompat.notify(0, builder.build());
    }

    private static void createNotificationChannel(Context context, String channelId) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "default";
            String description = "default";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static double simulateMortgageLoan(double capital, double rate, int years){
        double ratePerMonth = (rate / 100.0) / 12.0;
        int totalMonth = years * 12;

        double result =  (capital * ratePerMonth) / (1 - Math.pow(1 + ratePerMonth, -totalMonth));

        return  ((long)(result * 100) / 100.0);
    }
}
