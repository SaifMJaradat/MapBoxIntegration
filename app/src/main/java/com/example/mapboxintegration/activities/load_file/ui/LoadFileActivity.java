package com.example.mapboxintegration.activities.load_file.ui;


import static com.mapbox.maps.extension.style.expressions.dsl.generated.ExpressionDslKt.get;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mapboxintegration.R;
import com.example.mapboxintegration.activities.load_file.viewmodel.LoadFileViewModel;
import com.example.mapboxintegration.base.BaseActivity;
import com.example.mapboxintegration.databinding.ActivityLoadFileBinding;
import com.example.mapboxintegration.utilities.Helper;
import com.mapbox.maps.Style;
import com.mapbox.maps.extension.style.expressions.dsl.generated.ExpressionDslKt;
import com.mapbox.maps.extension.style.expressions.generated.Expression;
import com.mapbox.maps.extension.style.layers.LayerUtils;
import com.mapbox.maps.extension.style.layers.generated.FillLayer;
import com.mapbox.maps.extension.style.layers.generated.LineLayer;
import com.mapbox.maps.extension.style.layers.properties.generated.LineCap;
import com.mapbox.maps.extension.style.layers.properties.generated.LineJoin;
import com.mapbox.maps.extension.style.sources.SourceUtils;
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource;


public class LoadFileActivity extends BaseActivity<LoadFileViewModel, ActivityLoadFileBinding> {

    private static final int DURATION = 5000;
    private String sourceId = "sourceId";

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_load_file;
    }

    @Override
    protected ViewModel createViewModel() {
        return new ViewModelProvider(this).get(LoadFileViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupMap();
    }

    private void setupMap() {
        viewDataBinding.mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);

        viewDataBinding.mapView.getMapboxMap().getStyle(style -> {

            Helper.getInstance().animateToCameraOptions(viewDataBinding.mapView, Helper.getInstance().loadCameraOptions(46.63903759253634, 24.760498010841783, 17.0, -17.0), DURATION);

            GeoJsonSource geoJsonSource = new GeoJsonSource.Builder(sourceId).url(Helper.getInstance().filePath).build();

            SourceUtils.addSource(style, geoJsonSource);
            LayerUtils.addLayer(style, Helper.getInstance().createFillLayerWithFill(Helper.getInstance().getRandomString(6), sourceId));
            LayerUtils.addLayer(style, Helper.getInstance().createLineLayer(Helper.getInstance().getRandomString(6), sourceId));
        });
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