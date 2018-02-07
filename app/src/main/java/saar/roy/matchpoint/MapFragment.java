package saar.roy.matchpoint;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.security.Permission;
import java.util.List;

/**
 * Created by roy on 05/02/18.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap mMap;
    private MapServices services;
    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        services = new MapServices();
        // Obtain the MapView and get notified when the map is ready to be used.
        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstance);
        // Initialize the map
        MapsInitializer.initialize(this.getActivity());
        mapView.getMapAsync(this);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return v;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Enable current location button
        //mMap.setMyLocationEnabled(true);
        // Enable zoom controls
        //mMap.getUiSettings().setZoomControlsEnabled(true);
        Callback callback = new Callback<List<Court>>() {
            @Override
            public void onCallback(List<Court> courts) {
                for (Court court : courts) {
                    // Add a marker for each court
                    mMap.addMarker(court.toMarkerOptions(getContext()));
                }
            }
        };
        services.getCourts(callback);
        // Snippet on click
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            // Show the create match dialog
            public void onInfoWindowClick(Marker marker) {
                CreateMatchDialogFragment matchDialogFragment = CreateMatchDialogFragment.newInstance();
                matchDialogFragment.setCourt(marker.getTitle(), marker.getSnippet());
                FragmentManager fm = getActivity().getSupportFragmentManager();
                matchDialogFragment.show(fm, "Dialog");
            }
        });
        moveCameraToCurrentLocation();
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void moveCameraToCurrentLocation() {
        if (checkLocationPermission()) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE,
                    new LocationListener() {
                        @Override
                        // Move camera to current location
                        public void onLocationChanged(Location location) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
                            mMap.moveCamera(cameraUpdate);
                            locationManager.removeUpdates(this);
                        }

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {

                        }

                        @Override
                        public void onProviderEnabled(String s) {

                        }

                        @Override
                        public void onProviderDisabled(String s) {

                        }
                    });
        }
    }

    public boolean checkLocationPermission() {
        return (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION));
    }

    private boolean checkPermission(String accessFineLocation) {
        return ActivityCompat.checkSelfPermission(getContext(),
                accessFineLocation) != PackageManager.PERMISSION_GRANTED;
    }
}