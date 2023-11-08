# MapBox Integreation

# Android Mapbox Integration and Customization (JAVA)

This Android project demonstrates the integration of Mapbox, a versatile mapping and location platform, with customizable features. The goal of this project is to create an Android app that incorporates Mapbox and offers the following functionalities:

## Project Structure

- I used MVVM Architecture pattern to build out project because helps cleanly separate an application's business and presentation logic from its user interface (UI), it should be use Clean Architecure Pattern but we don't use it because our focusing on MapBox Integration.
- Trying to make Hight cohesion and Low coupling.
- Use Singleton Pattern to restricts the instantiation of a class and ensures that only one instance of the class exists in the Java Virtual Machine.
- Handle all points and Shapes in ViewModel with best practices.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Mapbox Integration](#mapbox-integration)
- [Display a Map](#display-a-map)
- [Draw a Polygon](#draw-a-polygon)
- [GeoJSON Integration](#geojson-integration)
- [Display Current Location](#display-current-location)
- [Custom Icon Image](#custom-icon-image)

## Prerequisites

Before you begin, make sure you have the following:

- Android Studio and the latest Android development tools installed.
- Mapbox API keys. You can obtain them from the Mapbox Developer Dashboard.

## Mapbox Integration

- Set up a new Android project using Android Studio.
- Integrate the Mapbox SDK into the project.
- Configure the Mapbox API keys in the project.

## Display a Map

- Implement a map view that displays a map using Mapbox.
- Two Button exists in home page draw poygon and load file.
- you will see the current location in this page with ask about permission.

## Draw a Polygon

- draw a polygon by default on the map.
- You can click on the map to make a custom markers and click on Customize Polygon to draw a customize one.
- Allow user long click to change last polygon color.
- make some Customizes the polygon style (stroke color, fill color, opacity).

## GeoJSON Integration

- Load GeoJSON data onto the map from locale resource.
- Visualize the GeoJSON data on the map with customizable styles.

## Display Current Location

- Integrate location services to display the user's current location on the map.
- Customize the location marker with a custom icon image.

## Custom Icon Image

- Allow users to select a custom icon image for their location marker.
- Implement a feature to choose an image from the device's gallery or provide a set of predefined icons to choose from.
- Ensure the selected icon is displayed as the user's location marker.

