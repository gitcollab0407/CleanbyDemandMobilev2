package com.ignis.cleanbydemandmobile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class HomeMapFragment extends Fragment implements GoogleMap.OnCameraMoveStartedListener,
                                                                 GoogleMap.OnCameraMoveListener,
                                                                 GoogleMap.OnCameraMoveCanceledListener,
                                                                 GoogleMap.OnCameraIdleListener,
                                                                 OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    SupportMapFragment mapFragment;
    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;
    LatLng userLocation;
    LatLng centerph = new LatLng(12.496333, 123.008514);// PH_Luzon
    LatLng lastKnown_userLocation;

    private Animation myFadeInAnimation, myFadeOutAnimation;

    int focus_location = 1;

    private AutoCompleteTextView mSearchText;

    private Marker currentMarker;

    int hideinfo = 0;

    private static final int REQUEST_LOCATION = 1;

    TextView navlocation, navuser;

    private static final String TAG = "MapsActivity"; // Log

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private Boolean mLocationPermissionsGranted = false;

    private Unbinder unbinder;

    ArrayList<String> Locations = new ArrayList<String>();

    @BindView(R.id.mylocation) ImageView mylocation;

    View view;
    public HomeMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_map, container, false);

        ButterKnife.bind(this, view);
        getLocationPermission();

        myFadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein_icon);
        myFadeOutAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fadeout_icon);

        return view;
    }

    @OnClick(R.id.infobar)
    public  void infobar(View view){
        try {
                android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.dialog_schedule_info, null);
                TextView btnconfirm = (TextView) mView.findViewById(R.id.accept);
                TextView messagecontent = (TextView) mView.findViewById(R.id.d_message_content);
                TextView addresscontent = (TextView) mView.findViewById(R.id.d_address_content);
                mBuilder.setView(mView);
                final android.support.v7.app.AlertDialog dialog = mBuilder.create();
                dialog.show();

            messagecontent.setMovementMethod(new ScrollingMovementMethod());
            addresscontent.setMovementMethod(new ScrollingMovementMethod());

            btnconfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "asdasd", Toast.LENGTH_SHORT).show();

                    dialog.hide();
                }
            });

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    hidenavbar();
                }
            });

        }catch(Exception e){}
    }

    @OnClick(R.id.mylocation)
    public void mylocation(View view) {
        if (userLocation != null) {
            CameraPosition position = new CameraPosition.Builder().target(userLocation).zoom(17).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
            // Log.d(TAG, "onLocationChanged 1:" + userLocation);
        } else if (lastKnown_userLocation != null) {
            CameraPosition position = new CameraPosition.Builder().target(lastKnown_userLocation).zoom(17).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
            //  Log.d(TAG, "lastKnown_userLocation 1:" + lastKnown_userLocation);
        }

        focus_location = 1;
        if (mylocation.getVisibility() == View.VISIBLE) {
            mylocation.startAnimation(myFadeOutAnimation);
            mylocation.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);
        mMap.setOnMarkerClickListener(this);

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity(), R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch(Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

//------------------------------------------------------------------------------------User Location

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                Log.d(TAG, "user location: " + userLocation);


                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();

                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    hidenavbar();

                    if (userLocation != null && focus_location == 1) {

                        CameraPosition position = new CameraPosition.Builder().target(userLocation).zoom(17).build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));

                        if(mMap.isMyLocationEnabled() == false) {
                            mMap.setMyLocationEnabled(true);

                            if (mylocation.getVisibility() == View.VISIBLE) {
                                mylocation.startAnimation(myFadeOutAnimation);
                                mylocation.setVisibility(View.INVISIBLE);
                            }
                        }

                    } else if (userLocation != null && focus_location == 0) {
                        mMap.setMyLocationEnabled(true);

                        if (mylocation.getVisibility() == View.INVISIBLE) {
                            mylocation.startAnimation(myFadeOutAnimation);
                            mylocation.setVisibility(View.VISIBLE);
                        }

                    }
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };
//------------------------------------------------------------------------------------
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(centerph));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerph, 5));

        if (location != null) {
            //NETWORK_PROVIDER
            double latti = location.getLatitude();
            double longi = location.getLongitude();
            lastKnown_userLocation = new LatLng(latti, longi);
            CameraPosition position = new CameraPosition.Builder().target(lastKnown_userLocation).zoom(17).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
            mMap.setMyLocationEnabled(true);

        } else if (location1 != null) {
            //GPS_PROVIDER
            double latti = location1.getLatitude();
            double longi = location1.getLongitude();
            lastKnown_userLocation = new LatLng(latti, longi);
            CameraPosition position = new CameraPosition.Builder().target(lastKnown_userLocation).zoom(17).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
            mMap.setMyLocationEnabled(true);

        } else if (location2 != null) {
            //PASSIVE_PROVIDER
            double latti = location2.getLatitude();
            double longi = location2.getLongitude();
            lastKnown_userLocation = new LatLng(latti, longi);
            CameraPosition position = new CameraPosition.Builder().target(lastKnown_userLocation).zoom(17).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
            mMap.setMyLocationEnabled(true);
        }
//------------------------------------------------------------------------------------Other Location
   /*
                    LatLng UsersCoordinate = new LatLng(lat, lng);

                    //  Log.d(TAG, "Coordinates: " + UsersCoordinate.toString());
                    OtherUser = mMap.addMarker(new MarkerOptions()
                                                       .position(UsersCoordinate)
                                                       .title(username)
                                                       .snippet("My Frequency: " + frequency)
                                                       .icon(BitmapDescriptorFactory.fromResource(R.drawable.sflag)));
                    OtherUser.setTag(id);
                    */

    }//end googlemap

    @Override
    public void onCameraMoveStarted(int reason) {
        //"The user gestured on the map."
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            // Log.d(TAG, "lastKnown_userLocation: " + lastKnown_userLocation + "userLocation: " + userLocation);

            if (userLocation != null || lastKnown_userLocation != null) {

                focus_location = 0;
                if (mylocation.getVisibility() == View.INVISIBLE) {
                    mylocation.startAnimation(myFadeInAnimation);
                    mylocation.setVisibility(View.VISIBLE);
                }

                if (hideinfo == 1) {
                    currentMarker.hideInfoWindow();
                    hideinfo = 0;
                }

            }

        } else if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION) {
            //"The user tapped something on the map."
        } else if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION) {
            //"The user moved the camera."
        }
    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onCameraMoveCanceled() {

    }

    @Override
    public void onCameraIdle() {
    }

    private void hidenavbar() {

        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void getLocationPermission() {
        String[] permissions = { Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION };

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void initMap() {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment == null){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
        }
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            //  Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    // Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

}
