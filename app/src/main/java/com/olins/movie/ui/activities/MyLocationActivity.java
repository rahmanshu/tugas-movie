package com.olins.movie.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.turf.TurfMeasurement;
import com.olins.movie.R;
import com.olins.movie.utils.Constant;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;


public class MyLocationActivity extends BaseActivity implements OnMapReadyCallback,
        MapboxMap.OnMapClickListener, PermissionsListener {

    @BindView(R.id.mapView)
    MapView mapView;

    @BindView(R.id.tv_distance)
    TextView tvDistance;

    @BindView(R.id.tv_address)
    TextView tvAddress;

    @BindView(R.id.ll_distance)
    LinearLayout llDistance;

    @BindView(R.id.ll_address)
    LinearLayout llAddress;

    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    private List<Point> pointList = new ArrayList<>();
    private double latitude = 0, longitude = 0, totalLineDistance = 0;
    private MainActivityLocationCallback callback = new MainActivityLocationCallback(this);

    public static void startActivity(BaseActivity sourceActivity) {
        Intent intent = new Intent(sourceActivity, MyLocationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        sourceActivity.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_mylocation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(callback);
            mapboxMap.removeOnMapClickListener(this);
        }
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            if (mapboxMap.getStyle() != null) {
                enableLocationComponent(mapboxMap.getStyle());
            }
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        tvDistance.setText(String.valueOf(totalLineDistance) + " KM");
        mapboxMap.setStyle(new Style.Builder()
                        .fromUri(Constant.STYLE_URI)
                        .withSource(new GeoJsonSource(Constant.SOURCE_ID))
                        .withLayer(new CircleLayer(Constant.CIRCLE_LAYER_ID, Constant.SOURCE_ID).withProperties(
                                PropertyFactory.circleColor(Constant.CIRCLE_COLOR),
                                PropertyFactory.circleRadius(Constant.CIRCLE_RADIUS)
                        ))
                        .withLayerBelow(new LineLayer(Constant.LINE_LAYER_ID, Constant.SOURCE_ID).withProperties(
                                PropertyFactory.lineColor(Constant.LINE_COLOR),
                                PropertyFactory.lineWidth(Constant.LINE_WIDTH),
                                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND)
                        ), Constant.CIRCLE_LAYER_ID), new Style.OnStyleLoaded() {

                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        mapboxMap.addOnMapClickListener(MyLocationActivity.this);
                        enableLocationComponent(style);
                    }
                }
        );
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {

        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .useDefaultLocationEngine(false)
                            .build();
            locationComponent.activateLocationComponent(locationComponentActivationOptions);
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.NORMAL);
            initLocationEngine();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);
        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();
        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnClick(R.id.ll_distance)
    void calculateDistance(){
        totalLineDistance = 0;
        LatLng latLng = new LatLng(new LatLng(-6.2071339, 106.8213189)); //menara astra
        addClickPointToLine(latLng);
    }

    @OnClick(R.id.ll_address)
    void getCurrentLocation(){
        LatLng latLng = new LatLng();
        latLng.setLatitude(latitude);
        latLng.setLongitude(longitude);
        getLocationInfo(latLng);
    }

    private void addClickPointToLine(final LatLng latLng) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                // Get the source from the map's style
                GeoJsonSource geoJsonSource = style.getSourceAs(Constant.SOURCE_ID);
                if (geoJsonSource != null) {

                    pointList.add(Point.fromLngLat(longitude, latitude));
                    pointList.add(Point.fromLngLat(latLng.getLongitude(),latLng.getLatitude()));

                    int pointListSize = pointList.size();

                    double distanceBetweenLastAndSecondToLastClickPoint = 0;

                    // Make the Turf calculation between the last tap point and the second-to-last tap point.
                    if (pointList.size() >= 2) {
                        distanceBetweenLastAndSecondToLastClickPoint = TurfMeasurement.distance(
                                pointList.get(pointListSize - 2), pointList.get(pointListSize - 1));
                    }

                    // Re-draw the new GeoJSON data
                    if (pointListSize >= 2 && distanceBetweenLastAndSecondToLastClickPoint > 0) {

                        // Add the last TurfMeasurement#distance calculated distance to the total line distance.
                        totalLineDistance += distanceBetweenLastAndSecondToLastClickPoint;

                        // Adjust the TextView to display the new total line distance.
                        // DISTANCE_UNITS must be equal to a String found in the TurfConstants class
                        tvDistance.setText(String.valueOf(Math.round(totalLineDistance)) + " KM");

                        // Set the specific source's GeoJSON data
                        geoJsonSource.setGeoJson(Feature.fromGeometry(LineString.fromLngLats(pointList)));
                    }
                }
            }
        });
    }


    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void getLocationInfo(final LatLng latLng) {
        tvAddress.setText(getAddreeByGeocoder(latLng));
        animateCameraToNewPosition(latLng);
    }

    private String getAddreeByGeocoder(LatLng latLng){
        String result = "";
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> address = geocoder.getFromLocation(latLng.getLatitude(),
                    latLng.getLongitude(),
                    1);
            result = address.get(0).getAddressLine(0);
        } catch (Exception servicesException) {
            servicesException.printStackTrace();
        }
        return result;
    }

    private void animateCameraToNewPosition(LatLng latLng) {
        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(17)
                        .build()), 1500);
    }

    private class MainActivityLocationCallback implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<MyLocationActivity> activityWeakReference;

        MainActivityLocationCallback(MyLocationActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        @Override
        public void onSuccess(LocationEngineResult result) {
            MyLocationActivity activity = activityWeakReference.get();

            if (activity != null) {
                Location location = result.getLastLocation();

                if (location == null) {
                    return;
                }

                // Pass the new location to the Maps SDK's LocationComponent
                if (activity.mapboxMap != null && result.getLastLocation() != null) {
                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());

                    activity.latitude = location.getLatitude();
                    activity.longitude = location.getLongitude();
                    activity.mapboxMap.clear();
                    CameraPosition position = new CameraPosition.Builder()
                            .target(new LatLng(location.getLatitude(), location.getLongitude()))
                            .zoom(10)
                            .build();

                    activity.mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 200);
                    activity.mapboxMap.addMarker(new MarkerOptions()
                            .title("My Position")
                            .snippet(getAddreeByGeocoder(new LatLng(location.getLatitude(), location.getLongitude())))
                            .position(new LatLng(location.getLatitude(), location.getLongitude())));
                }
            }
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location can not be captured
         *
         * @param exception the exception message
         */
        @Override
        public void onFailure(@NonNull Exception exception) {
            Log.d("LocationChangeActivity", exception.getLocalizedMessage());
            MyLocationActivity activity = activityWeakReference.get();
            if (activity != null) {
                Toast.makeText(activity, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

}
