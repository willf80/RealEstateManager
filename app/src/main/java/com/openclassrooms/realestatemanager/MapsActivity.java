package com.openclassrooms.realestatemanager;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.Address;
import com.openclassrooms.realestatemanager.models.MediaTemp;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyDisplayAllInfo;
import com.openclassrooms.realestatemanager.models.PropertyType;
import com.openclassrooms.realestatemanager.services.MapApiService;
import com.openclassrooms.realestatemanager.utils.FileHelper;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.viewmodels.ViewModelFactory;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback {
    private static final float DEFAULT_ZOOM = 14.0f;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private GoogleMap mMap;
    private PropertyViewModel mPropertyViewModel;

    @BindView(R.id.cardView)
    CardView mCardView;

    @BindView(R.id.addressLine1TextView)
    TextView addressLine1TextView;

    @BindView(R.id.soldTextView)
    TextView soldTextView;

    @BindView(R.id.priceTextView)
    TextView priceTextView;

    @BindView(R.id.titleTextView)
    TextView titleTextView;

    @BindView(R.id.imageView)
    ImageView imageView;

    boolean isCurrencyEuro;
    PropertyDisplayAllInfo currentAllInfo;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    private LatLng mDefaultLocation = new LatLng(40.6971494,-74.2598642);
    private Location mLastKnownLocation;
    private CameraPosition mCameraPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        showReturnHome();

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        isCurrencyEuro = Utils.getCurrencySettings(this);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        configureViewModels();

        listeners();
    }

    private void configureViewModels() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);

        this.mPropertyViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(PropertyViewModel.class);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Maps Settings
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setAllGesturesEnabled(true);
        uiSettings.setZoomControlsEnabled(true);


        // Add a marker and move the camera
        float zoom = DEFAULT_ZOOM;
        if(mCameraPosition != null){
            zoom = mCameraPosition.zoom;
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, zoom));

        mMap.setOnMarkerClickListener((marker) -> {
            currentAllInfo = (PropertyDisplayAllInfo) marker.getTag();
            if(currentAllInfo == null) return false;

            loadPropertyDetailsView(currentAllInfo);
            mCardView.setVisibility(View.VISIBLE);

            return false;
        });

        mMap.setOnInfoWindowClickListener((marker) -> {
            PropertyDisplayAllInfo allInfo = (PropertyDisplayAllInfo) marker.getTag();
            if(allInfo == null) return;

            Property property = allInfo.getProperty();
            if(property == null) return;

            showDetailsInActivity(property.getId());
        });

        updateLocationUI();

        getDeviceLocation();

        loadData();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission(){
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mLocationPermissionGranted = true;
                        updateLocationUI();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .onSameThread()
                .check();
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = (Location) task.getResult();
                        if(mLastKnownLocation == null) return;
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                    } else {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    private void listeners(){
        mCardView.setOnClickListener(v -> {
            if(currentAllInfo == null) return;

            Property property = currentAllInfo.getProperty();
            if(property == null) return;

            showDetailsInActivity(property.getId());
        });
    }

    private void loadPropertyDetailsView(PropertyDisplayAllInfo propertyDisplayedInfo) {
        Property property = propertyDisplayedInfo.getProperty();
        assert property != null;

        PropertyType propertyType = propertyDisplayedInfo.getPropertyType();
        assert propertyType != null;

        Address address = propertyDisplayedInfo.getAddress();
        MediaTemp mediaTemp = propertyDisplayedInfo.getMediaTemp();

        String title = Utils.getPropertyTitle(property, propertyType);
        String price = Utils.getCurrencyMoney(property.getPrice(), isCurrencyEuro);

        if(address != null) {
            String fullAddress = String.format(Locale.getDefault(),
                    "%s, %s", address.getAddressLine1(), address.getPostalCode());
            addressLine1TextView.setText(fullAddress);
        }

        if(mediaTemp != null) {
            File file = FileHelper.getFile(this, mediaTemp.getFileName());
            if(file == null) return;

            Picasso.get()
                    .load(file)
                    .resize(124, 124)
                    .into(imageView);
        }

        priceTextView.setText(price);
        titleTextView.setText(title);

        if(property.isSold()){
            soldTextView.setVisibility(View.VISIBLE);
        }else{
            soldTextView.setVisibility(View.GONE);
        }
    }

    private void showDetailsInActivity(long propertyId) {
        Intent intent = new Intent(this, PropertyDetailsActivity.class);
        intent.putExtra(PropertyDetailsActivity.EXTRA_PROPERTY_ID, propertyId);
        startActivity(intent);
    }

    private void drawMarker(PropertyDisplayAllInfo allInfo, String title, LatLng latLng, boolean isSold){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(title);
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        if(isSold){
            markerOptions.icon(BitmapDescriptorFactory
            .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        }

        Marker maker = mMap.addMarker(markerOptions);
        maker.setTag(allInfo);

    }

    private void loadData() {
        mPropertyViewModel.getAllPropertyDisplayedInfo()
                .observe(this, this::onPropertyListLoaded);
    }

    private void cleanMapMaker(){
        mMap.clear();
    }

    private void getAddressLocationInfo(PropertyDisplayAllInfo allInfo, String address){
        MapApiService.getAddressLatLong(this, address)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if(!response.isSuccessful()) return;
                        ResponseBody responseBody = response.body();
                        if(responseBody == null) return;

                        Property property = allInfo.getProperty();
                        if(property == null) return;

                        String title = getMarkerTitle(allInfo);
                        if(title == null) return;

                        final String markerTitle = title;
                        final LatLng location = getLatLng(responseBody.string());

                        runOnUiThread(() -> drawMarker(allInfo, markerTitle, location, property.isSold()));
                    }
                });
    }

    private String getMarkerTitle(PropertyDisplayAllInfo allInfo){
        Property property = allInfo.getProperty();
        if(property == null) return null;

        Address propertyAddress = allInfo.getAddress();
        if(propertyAddress == null) return null;


        String title = Utils.getPropertyTitle(property, allInfo.getPropertyType());

        if(property.isSold()) {
            title = title + " (sold)";
        }

        return title;
    }

    private LatLng getLatLng(String data){
        String[] addressList = data.split("\n");
        if(addressList.length < 2) return null;

        String addressData = addressList[1];
        String [] addressDataList = addressData.split("\\|");
        if(addressDataList.length < 16) return null;

        int latIndex = addressDataList.length - 2;
        int lngIndex = addressDataList.length - 1;

        final double lat = Double.parseDouble(addressDataList[latIndex]);
        final double lng = Double.parseDouble(addressDataList[lngIndex]);

        return new LatLng(lat, lng);
    }

    private void loadPropertyAddress(PropertyDisplayAllInfo allInfo, Property property){
        mPropertyViewModel.getPropertyAddress(property.getId())
                .observe(this, addressDisplayedInfo ->
                {
                    if(addressDisplayedInfo == null) return;

                    Iterator<Address> it = addressDisplayedInfo.getAddress().iterator();
                    Address address = it.next();
                    allInfo.setAddress(address);

                    if(address == null) return;
                    String fullAddress = address.getAddressLine1() + ", " + address.getPostalCode();
                    getAddressLocationInfo(allInfo, fullAddress);
                });
    }

    private void loadPropertyType(PropertyDisplayAllInfo allInfo, Property property){
        mPropertyViewModel.getPropertyType(property.getPropertyTypeId())
                .observe(this, propertyType ->
                {
                    allInfo.setPropertyType(propertyType);

                    loadPropertyAddress(allInfo, property);
                });
    }

    private void loadMedia(PropertyDisplayAllInfo allInfo, Property property){
        mPropertyViewModel.getSelectedMedia(property.getId())
                .observe(this, media -> {
                    if(media != null) {
                        MediaTemp mediaTemp = new MediaTemp();
                        mediaTemp.setId(media.getId());
                        mediaTemp.setCover(media.isCover());
                        mediaTemp.setLabel(media.getLabel());
                        mediaTemp.setFileName(media.getFileName());

                        allInfo.setMediaTemp(mediaTemp);
                    }
                });
    }

    private void onPropertyListLoaded(List<PropertyDisplayAllInfo> propertyList) {
        // Clean all markers
        cleanMapMaker();

        for (PropertyDisplayAllInfo allInfo : propertyList) {
            Property property = allInfo.getProperty();
            if(property == null) continue;

            loadPropertyType(allInfo, property);
            loadMedia(allInfo, property);
        }
    }
}
