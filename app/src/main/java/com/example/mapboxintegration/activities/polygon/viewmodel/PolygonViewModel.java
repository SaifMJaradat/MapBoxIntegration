package com.example.mapboxintegration.activities.polygon.viewmodel;

import androidx.lifecycle.ViewModel;

import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;

public class PolygonViewModel extends ViewModel {

    public List<String> sourceIds = new ArrayList<>();
    public List<String> layerIds = new ArrayList<>();

    private final List<List<Point>> points = new ArrayList<>();
    private final List<Point> outerPoints = new ArrayList<>();

    private List<Point> clickedPoints = new ArrayList<>();
    public List<List<Point>> clickedCustomizePoints = new ArrayList<>();

    public void addPoint(Point point) {
        clickedPoints.add(point);
    }

    public void addSourceId(String sourceId) {
        sourceIds.add(sourceId);
    }

    public void addLayerId(String layerId) {
        layerIds.add(layerId);
    }

    public String getLastLayerIdAdded() {
        return layerIds.get(layerIds.size() - 1);
    }

    public String getLastSourceIdAdded() {
        return sourceIds.get(sourceIds.size() - 1);
    }

    public List<Point> getSelectedPoints() {
        return clickedPoints;
    }

    public void resetPoints() {
        clickedPoints = new ArrayList<>();
        clickedCustomizePoints = new ArrayList<>();
    }

    public List<List<Point>> loadPoints() {
        outerPoints.add(Point.fromLngLat(-122.685699, 45.522585));
        outerPoints.add(Point.fromLngLat(-122.708873, 45.534611));
        outerPoints.add(Point.fromLngLat(-122.678833, 45.530883));
        outerPoints.add(Point.fromLngLat(-122.667503, 45.547115));
        outerPoints.add(Point.fromLngLat(-122.660121, 45.530643));
        outerPoints.add(Point.fromLngLat(-122.636260, 45.533529));
        outerPoints.add(Point.fromLngLat(-122.659091, 45.521743));
        outerPoints.add(Point.fromLngLat(-122.648792, 45.510677));
        outerPoints.add(Point.fromLngLat(-122.664070, 45.515008));
        outerPoints.add(Point.fromLngLat(-122.669048, 45.502496));
        outerPoints.add(Point.fromLngLat(-122.678489, 45.515369));
        outerPoints.add(Point.fromLngLat(-122.702007, 45.506346));
        outerPoints.add(Point.fromLngLat(-122.685699, 45.522585));
        points.add(outerPoints);
        return points;
    }
}
