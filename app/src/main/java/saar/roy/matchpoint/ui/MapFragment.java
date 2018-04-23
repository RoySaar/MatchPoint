package saar.roy.matchpoint.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.firestore.DocumentReference;

import java.util.Map;

import dmax.dialog.SpotsDialog;
import saar.roy.matchpoint.R;
import saar.roy.matchpoint.data.Court;
import saar.roy.matchpoint.services.Callback;
import saar.roy.matchpoint.services.MapServices;

/**
 * Created by roy on 05/02/18.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private SpotsDialogHandler dialogHandler;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        dialogHandler = SpotsDialogHandler.getInstance();
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        View v = inflater.inflate(saar.roy.matchpoint.R.layout.fragment_map, container,
                false);
        // Obtain the MapView and get notified when the map is ready to be used.
        mapView = v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstance);
        // Initialize the map
        MapsInitializer.initialize(this.getActivity());
        mapView.getMapAsync(this);
        locationManager = (LocationManager) getActivity().getSystemService(Context
                .LOCATION_SERVICE);
        return v;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Enable current location button
        mMap.setMyLocationEnabled(true);
        // Enable zoom controls
        //mMap.getUiSettings().setZoomControlsEnabled(true);
        final Bitmap smallMarker = getMarkerIcon();
        Callback callback = new Callback<Map<DocumentReference,Court>>() {
            @Override
            public void onCallback(Map<DocumentReference,Court> map) {
                if (getContext() == null)
                    return;
                for (Map.Entry<DocumentReference,Court> entry : map.entrySet()) {
                    // Add a marker for each court
                    Marker m = mMap.addMarker(entry.getValue().toMarkerOptions(smallMarker));
                    m.setTag(entry.getKey());
                }
            }
        };
        MapServices.getInstance().getCourtMarkers(callback,smallMarker);
        // Snippet on click
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            // Show the create match fragment
            public void onInfoWindowClick(Marker marker) {
                final CreateMatchFragment matchDialogFragment = CreateMatchFragment
                        .newInstance();
                matchDialogFragment.setCourt(marker.getTitle(), marker.getSnippet(),(DocumentReference)marker.getTag());
                FragmentManager fm = getActivity().getSupportFragmentManager();
                dialogHandler.show(getContext());
                ((MainActivity)getActivity()).changeFragment(matchDialogFragment);
            }
        });
        moveCameraToCurrentLocation();
    }

    private Bitmap getMarkerIcon() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getContext().getDrawable(R.drawable.marker_icon);
        Bitmap b = bitmapDrawable.getBitmap();
        int ICON_HEIGHT = 210;
        int ICON_WIDTH = 110;
        return Bitmap.createScaledBitmap(b, ICON_WIDTH, ICON_HEIGHT, false);
    }

    @Override
    public void onResume() {
        mapView.onResume();
        dialogHandler = SpotsDialogHandler.getInstance();
        if (dialogHandler.isShowing())
            dialogHandler.hide();
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