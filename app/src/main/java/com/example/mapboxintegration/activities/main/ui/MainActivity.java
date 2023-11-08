package com.example.mapboxintegration.activities.main.ui;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mapboxintegration.R;
import com.example.mapboxintegration.activities.load_file.ui.LoadFileActivity;
import com.example.mapboxintegration.activities.main.viewmodel.MainViewModel;
import com.example.mapboxintegration.activities.polygon.ui.PolygonActivity;
import com.example.mapboxintegration.base.BaseActivity;
import com.example.mapboxintegration.databinding.ActivityMainBinding;
import com.example.mapboxintegration.utilities.Helper;
import com.mapbox.geojson.Point;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;

import java.io.IOException;

public class MainActivity extends BaseActivity<MainViewModel, ActivityMainBinding> {

    private final int SELECT_PICTURE = 200;

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected ViewModel createViewModel() {
        return new ViewModelProvider(this).get(MainViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupMap();
        setupUI();
    }

    private void setupMap() {
        viewDataBinding.mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);
        getCurrentLocation();
    }

    private void setupUI() {
        viewDataBinding.drawPolygon.setOnClickListener(v -> {
            startActivity(new Intent(this, PolygonActivity.class));
        });

        viewDataBinding.loadFile.setOnClickListener(v -> {
            startActivity(new Intent(this, LoadFileActivity.class));
        });
    }

    // We request location update to get current location but we should ask about permission first.
    private void getCurrentLocation() {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            return;
        }
        int LOCATION_REFRESH_TIME = 15000;
        int LOCATION_REFRESH_DISTANCE = 500;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, locationListener);
    }

    // This is locationListener to get latest location points then draw our marker.
    private final LocationListener locationListener = location -> {
        int DURATION = 5000;
        Helper.getInstance().animateToCameraOptions(viewDataBinding.mapView, Helper.getInstance().loadCameraOptions(location.getLongitude(), location.getLatitude(), 15.0, -15.0), DURATION);
        viewModel.longitude = location.getLongitude();
        viewModel.latitude = location.getLatitude();
        drawMarker(false, null);
    };

    // We use this method multi time because we have a feature to change our marker from gallery.
    // We make it optional to change our icon.
    private void drawMarker(boolean isMarkClicked, Bitmap newIcon) {
        Bitmap icon;
        if (isMarkClicked) {
            icon = newIcon;
        } else {
            icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.marker);
        }
        AnnotationPlugin annotationAPI = AnnotationPluginImplKt.getAnnotations(viewDataBinding.mapView);

        PointAnnotationManager pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationAPI, viewDataBinding.mapView);

        Point point = Point.fromLngLat(viewModel.longitude, viewModel.latitude);

        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions().withPoint(point).withTextField(getString(R.string.current_location)).withIconImage(Helper.getInstance().getResizedBitmap(icon, 75, 75));

        pointAnnotationManager.create(pointAnnotationOptions);

        pointAnnotationManager.addClickListener(pointAnnotation -> {
            imageChooser();
            return false;
        });
    }

    // Intent for go to Gallery or image Provider.
    void imageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        drawMarker(true, bitmap);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    @SuppressLint("Lifecycle")
    @Override
    protected void onStart() {
        super.onStart();
        viewDataBinding.mapView.onStart();
    }

    @SuppressLint("Lifecycle")
    @Override
    protected void onStop() {
        super.onStop();
        viewDataBinding.mapView.onStop();
    }

    @SuppressLint("Lifecycle")
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        viewDataBinding.mapView.onLowMemory();
    }

    @SuppressLint("Lifecycle")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewDataBinding.mapView.onDestroy();
    }
}