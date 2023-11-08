package com.example.mapboxintegration.activities.polygon.ui;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mapboxintegration.R;
import com.example.mapboxintegration.activities.polygon.viewmodel.PolygonViewModel;
import com.example.mapboxintegration.base.BaseActivity;
import com.example.mapboxintegration.databinding.ActivityPolygonBinding;
import com.example.mapboxintegration.utilities.Helper;
import com.mapbox.geojson.Point;
import com.mapbox.maps.Style;
import com.mapbox.maps.extension.style.layers.LayerUtils;
import com.mapbox.maps.extension.style.layers.generated.FillLayer;
import com.mapbox.maps.extension.style.sources.SourceUtils;
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.GesturesPlugin;
import com.mapbox.maps.plugin.gestures.GesturesUtils;


public class PolygonActivity extends BaseActivity<PolygonViewModel, ActivityPolygonBinding> {

    private final String sourceId = "source-id";
    private final String layerId = "layer-id";
    private static final int DURATION = 5000;

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_polygon;
    }

    @Override
    protected ViewModel createViewModel() {
        return new ViewModelProvider(this).get(PolygonViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        setupMap();
    }

    private void setupUI() {
        viewDataBinding.drawPolygon.setOnClickListener(v -> drawCustomizePolygon());
    }

    private void drawCustomizePolygon() {
        if (viewModel.getSelectedPoints().size() < 3) {
            Toast.makeText(this, R.string.please_click_on_map_to_choose_location, Toast.LENGTH_SHORT).show();
        } else {
            viewModel.clickedCustomizePoints.add(viewModel.getSelectedPoints());

            viewDataBinding.mapView.getMapboxMap().getStyle(style -> {

                viewModel.addSourceId(Helper.getInstance().getRandomString(15));

                GeoJsonSource geoJsonSource = (GeoJsonSource) SourceUtils.getSource(style, viewModel.getLastSourceIdAdded());

                if (geoJsonSource == null) {
                    SourceUtils.addSource(style, Helper.getInstance().createSourceForPolygon(viewModel.getLastSourceIdAdded(), viewModel.clickedCustomizePoints));
                } else {
                    Helper.getInstance().updateSource(geoJsonSource, viewModel.clickedCustomizePoints);
                }

                viewModel.addLayerId(Helper.getInstance().getRandomString(15));

                LayerUtils.addLayer(style, Helper.getInstance().createLayer(viewModel.getLastLayerIdAdded(), viewModel.getLastSourceIdAdded()));

                viewModel.resetPoints();
            });
        }
    }

    private void setupMap() {
        viewDataBinding.mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);

        GesturesPlugin gesturesPlugin = GesturesUtils.getGestures(viewDataBinding.mapView);
        gesturesPlugin.addOnMapClickListener(this::onMapClick);
        gesturesPlugin.addOnMapLongClickListener(this::onLongMapClick);

        Helper.getInstance().animateToCameraOptions(viewDataBinding.mapView, Helper.getInstance().loadCameraOptions(-122.685699, 45.522585,11.0, -11.0), DURATION);
        drawPolygon();
    }

    private boolean onMapClick(Point point) {
        viewModel.addPoint(point);
        drawMarker(point);
        return true;
    }

    private boolean onLongMapClick(Point point) {
        viewDataBinding.mapView.getMapboxMap().getStyle(style -> {

            if (viewModel.layerIds.isEmpty()) {
                return;
            }

            FillLayer fillLayer = (FillLayer) LayerUtils.getLayer(style, viewModel.getLastLayerIdAdded());

            fillLayer.fillColor("#" + Helper.getInstance().getRandomColor(6));
        });
        return true;
    }

    private void drawMarker(Point point) {

        Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.marker);

        AnnotationPlugin annotationAPI = AnnotationPluginImplKt.getAnnotations(viewDataBinding.mapView);

        PointAnnotationManager pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationAPI, viewDataBinding.mapView);

        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions().withPoint(point).withIconImage(Helper.getInstance().getResizedBitmap(icon, 60, 60));

        pointAnnotationManager.create(pointAnnotationOptions);
    }

    private void drawPolygon() {
        viewDataBinding.mapView.getMapboxMap().getStyle(style -> {
            SourceUtils.addSource(style, Helper.getInstance().createSourceForPolygon(sourceId, viewModel.loadPoints()));
            LayerUtils.addLayer(style, Helper.getInstance().createLayer(layerId, sourceId));
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