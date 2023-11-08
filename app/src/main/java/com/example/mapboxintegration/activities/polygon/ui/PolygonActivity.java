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

        /*
        We Checking is valid to draw or not because we are trying to draw (Polygon).
        The minimum number of sides a polygon can have is 3 because it needs a minimum of 3 sides to be a closed shape or else it will be open.
         */
        if (viewModel.isValidToDraw()) {

            viewModel.getClickedCustomizePoints().add(viewModel.getSelectedPoints());

            viewDataBinding.mapView.getMapboxMap().getStyle(style -> {

                // We create a custom SourceId and we added to ViewModel to saved it in our list.
                viewModel.addSourceId(Helper.getInstance().getRandomString(15));

                // We get GeoJsonSource from latest SourceId created.
                GeoJsonSource geoJsonSource = (GeoJsonSource) SourceUtils.getSource(style, viewModel.getLastSourceIdAdded());


                // We checking if the source exist or not, if doesn't we will create a new source for polygon if it is exist we will update the current source.
                if (geoJsonSource == null) {
                    SourceUtils.addSource(style, Helper.getInstance().createSourceForPolygon(viewModel.getLastSourceIdAdded(), viewModel.getClickedCustomizePoints()));
                } else {
                    Helper.getInstance().updateSource(geoJsonSource, viewModel.getClickedCustomizePoints());
                }

                // We create a custom LayerId and we added to ViewModel to saved it in our list.
                viewModel.addLayerId(Helper.getInstance().getRandomString(15));

                // For sure we will add layer with latest LayerId Added to the latest SourceId.
                LayerUtils.addLayer(style, Helper.getInstance().createLayer(viewModel.getLastLayerIdAdded(), viewModel.getLastSourceIdAdded()));

                // We shouldn't forget reset our list by below method :D;
                viewModel.resetPoints();
            });
        } else {
            Toast.makeText(this, R.string.please_click_on_map_to_choose_location, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupMap() {
        viewDataBinding.mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);

        // We created GesturesPlugin to make some customize clicks.
        GesturesPlugin gesturesPlugin = GesturesUtils.getGestures(viewDataBinding.mapView);
        gesturesPlugin.addOnMapClickListener(this::onMapClick);
        gesturesPlugin.addOnMapLongClickListener(this::onLongMapClick);

        // We focused to first Points, it's static because we have a dummy data in ViewModel :D.
        Helper.getInstance().animateToCameraOptions(viewDataBinding.mapView, Helper.getInstance().loadCameraOptions(-122.685699, 45.522585, 11.0, -11.0), DURATION);
        drawPolygon();
    }

    private boolean onMapClick(Point point) {
        // We added our clicked points to draw Polygon.
        viewModel.addPoint(point);
        drawMarker(point);
        return true;
    }

    // We changed the latest layer we drew
    private boolean onLongMapClick(Point point) {
        viewDataBinding.mapView.getMapboxMap().getStyle(style -> {

            if (viewModel.layerIds.isEmpty()) {
                return;
            }

            FillLayer fillLayer = (FillLayer) LayerUtils.getLayer(style, viewModel.getLastLayerIdAdded());

            fillLayer.fillColor(Helper.getInstance().getRandomColor(6));
        });
        return true;
    }

    // We will draw a custom marker with custom point in out mapView, we also make resize for Bitmap.
    private void drawMarker(Point point) {

        Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.marker);

        AnnotationPlugin annotationAPI = AnnotationPluginImplKt.getAnnotations(viewDataBinding.mapView);

        PointAnnotationManager pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationAPI, viewDataBinding.mapView);

        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions().withPoint(point).withIconImage(Helper.getInstance().getResizedBitmap(icon, 60, 60));

        pointAnnotationManager.create(pointAnnotationOptions);
    }

    // Drawing Custom Polygon from our dummy data.
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