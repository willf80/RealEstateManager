package com.openclassrooms.realestatemanager.services;

import android.content.Context;

import com.openclassrooms.realestatemanager.R;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MapApiService {
    private static final OkHttpClient CLIENT = new OkHttpClient();

    public static Call getAddressLatLong(Context context, String address) {
        String urlFormatted = context.getString(R.string.url_geocodeapi_mapquest);
        String url = String.format(urlFormatted, address, context.getString(R.string.mapquest_api_key));

        Request request = new Request
                .Builder()
                .url(url)
                .build();

        return CLIENT.newCall(request);
    }
}
