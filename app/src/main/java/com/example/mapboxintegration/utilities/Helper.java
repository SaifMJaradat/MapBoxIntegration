package com.example.mapboxintegration.utilities;

import static com.mapbox.maps.extension.style.expressions.dsl.generated.ExpressionDslKt.get;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.extension.style.expressions.generated.Expression;
import com.mapbox.maps.extension.style.layers.generated.FillExtrusionLayer;
import com.mapbox.maps.extension.style.layers.generated.FillLayer;
import com.mapbox.maps.extension.style.layers.generated.LineLayer;
import com.mapbox.maps.extension.style.layers.properties.generated.LineCap;
import com.mapbox.maps.extension.style.layers.properties.generated.LineJoin;
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource;
import com.mapbox.maps.plugin.animation.CameraAnimationsPlugin;
import com.mapbox.maps.plugin.animation.CameraAnimationsUtils;
import com.mapbox.maps.plugin.animation.MapAnimationOptions;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

// Singleton Pattern to make sure one instnace here.
public class Helper {

    private static Helper helper = null;

    public boolean isAnimating = false;
    public String filePath = "asset://map.geojson";
    private final LineCap lineCap = LineCap.ROUND;
    private final LineJoin lineJoin = LineJoin.ROUND;

    private Helper() {

    }

    // Create GeoJsonSource with custom Source and List of List of points.
    public GeoJsonSource createSourceForPolygon(String sourceId, List<List<Point>> points) {
        return new GeoJsonSource.Builder(sourceId).feature(Feature.fromGeometry(Polygon.fromLngLats(points))).build();
    }

    // Create update current source with replace list of list of points.
    public void updateSource(GeoJsonSource geoJsonSource, List<List<Point>> points) {
        geoJsonSource.feature(Feature.fromGeometry(Polygon.fromLngLats(points)));
    }

    // Create Fill Layer with custom layerId and sourceId with random color.
    public FillLayer createLayer(String layerId, String sourceId) {
        return new FillLayer(layerId, sourceId).fillColor(Color.parseColor(getRandomColor(6)));
    }

    // Create Fill Layer with custom layerId and sourceId and get property.
    public FillLayer createFillLayerWithFill(String layerId, String sourceId) {
        return new FillLayer(layerId, sourceId).fillColor(Expression.toString(get("fill")));
    }

    // Create LineLayer with custom layerId and sourceId with read properties from json file.
    public LineLayer createLineLayer(String layerId, String sourceId) {
        LineLayer lineLayer = new LineLayer(layerId, sourceId);
        String STROKE_EXPRESSION_KEY = "stroke";
        String STROKE_WIDTH_EXPRESSION_KEY = "stroke-width";
        String STROKE_OPACITY_EXPRESSION_KEY = "stroke-opacity";
        lineLayer.lineJoin(lineJoin).lineCap(lineCap).lineOpacity(Expression.toNumber(get(STROKE_OPACITY_EXPRESSION_KEY))).lineWidth(Expression.toNumber(get(STROKE_WIDTH_EXPRESSION_KEY))).lineColor(Expression.toString(get(STROKE_EXPRESSION_KEY)));
        return lineLayer;
    }

    // Make dynamic camera options with custom longitude, latitude, zoom and bearing.
    public CameraOptions loadCameraOptions(double longitude, double latitude, double zoom, double bearing) {
        return new CameraOptions.Builder().center(Point.fromLngLat(longitude, latitude)).pitch(45.0).zoom(zoom).bearing(bearing).build();
    }

    // Make animation with custom camera option object and duration.
    public void animateToCameraOptions(MapView mapView, CameraOptions cameraOptions, int duration) {
        isAnimating = true;
        CameraAnimationsPlugin camera = CameraAnimationsUtils.getCamera(mapView);

        camera.flyTo(cameraOptions, new MapAnimationOptions.Builder().interpolator(new FastOutSlowInInterpolator()).duration(duration).build());

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isAnimating = false;
            }
        }, duration);
    }

    // create getResizedBitmap to customize width and height.
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public String getRandomString(Integer length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) {
            int index = (int) (rnd.nextFloat() * characters.length());
            salt.append(characters.charAt(index));
        }
        return salt.toString();
    }


    public String getRandomColor(Integer length) {
        String characters = "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) {
            int index = (int) (rnd.nextFloat() * characters.length());
            salt.append(characters.charAt(index));
        }
        return "#" + salt.toString();
    }

    // Synchronized cheking to prevent two creation at the same time.
    public static synchronized Helper getInstance() {
        if (helper == null) helper = new Helper();
        return helper;
    }
}