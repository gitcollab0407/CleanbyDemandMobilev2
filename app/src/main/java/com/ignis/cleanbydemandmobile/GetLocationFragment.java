package com.ignis.cleanbydemandmobile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class GetLocationFragment extends Fragment implements GoogleMap.OnCameraMoveStartedListener,
                                                                     GoogleMap.OnCameraMoveListener,
                                                                     GoogleMap.OnCameraMoveCanceledListener,
                                                                     GoogleMap.OnCameraIdleListener,
                                                                     OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    SupportMapFragment mapFragment;
    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;
    LatLng userLocation;
    LatLng centerph = new LatLng(12.496333, 123.008514);// PH_Luzon
    LatLng lastKnown_userLocation;

    private Animation myFadeInAnimation, myFadeOutAnimation;

    int focus_location = 1;

    private Marker currentMarker;
    int hideinfo = 0;

    private static final int REQUEST_LOCATION = 1;

    TextView navlocation, navuser;

    private static final String TAG = "MapsActivity"; // Log

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private static final LatLngBounds LAT_LNG_BOUNDS  =  new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    private Boolean mLocationPermissionsGranted = false;

    private Unbinder unbinder;

    ArrayList<String> Locations = new ArrayList<String>();

    String coordinates = "";
    String address = "";
    String Latitude, Longitude;
    LatLng current_location;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @BindView(R.id.mylocation) ImageView mylocation;
    @BindView(R.id.setlocation) Button setlocation;
    @BindView(R.id.searchbar) AutoCompleteTextView mSearchText;


    View view;

    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;

    String set_address, set_coordinates;

    public GetLocationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_location, container, false);

        ButterKnife.bind(this, view);



        getLocationPermission();

        myFadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein_icon);
        myFadeOutAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fadeout_icon);

        return view;
    }
    @OnClick(R.id.mylocation)
    public void mylocation(View view) {
        String location;
        String locationrep1;
        String locationrep2;
        String[] coordinate_separated;


        if (userLocation != null) {
            CameraPosition position = new CameraPosition.Builder().target(userLocation).zoom(17).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
            Log.d(TAG, "onLocationChanged 1:" + userLocation);

            location = "" + userLocation;
            locationrep1 = location.replace("lat/lng: (", "");
            locationrep2 = locationrep1.replace(")", "");
            coordinate_separated = locationrep2.split(",");
            coordinates = locationrep2.toString();
            Latitude = coordinate_separated[0];
            Longitude = coordinate_separated[1];
            getCompleteAddressString(Double.parseDouble(coordinate_separated[0]), Double.parseDouble(coordinate_separated[1]));

        }else if(lastKnown_userLocation != null) {
            CameraPosition position = new CameraPosition.Builder().target(lastKnown_userLocation).zoom(17).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
            Log.d(TAG, "lastKnown_userLocation 1:" + lastKnown_userLocation);

            location = "" + lastKnown_userLocation;
            locationrep1 = location.replace("lat/lng: (", "");
            locationrep2 = locationrep1.replace(")", "");
            coordinate_separated = locationrep2.split(",");
            coordinates = locationrep2.toString();
            Latitude = coordinate_separated[0];
            Longitude = coordinate_separated[1];
            getCompleteAddressString(Double.parseDouble(coordinate_separated[0]), Double.parseDouble(coordinate_separated[1]));

        }
        focus_location = 1;
        if (mylocation.getVisibility() == View.VISIBLE) {
            mylocation.startAnimation(myFadeOutAnimation);
            mylocation.setVisibility(View.INVISIBLE);

        }
    }

/*    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mGoogleApiClient.disconnect();
    }*/

    @Override
    public void onPause() {
        super.onPause();

        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @OnClick(R.id.setlocation)
    public void setlocation(View view){

        try {

            ((ClientMainActivityFragment) getActivity()).action_title.setText("Booking");
        }catch(Exception ex){
          //  Toast.makeText(getActivity(), ""+ex, Toast.LENGTH_SHORT).show();
        }



        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        BookingFragment bookingFragment = new BookingFragment();

        PublicVariables.B_address = set_address;
        PublicVariables.B_coordinates = set_coordinates;

        fragmentTransaction.replace(R.id.fragment_container,bookingFragment, null);
        fragmentTransaction.addToBackStack(null).commit();

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

        init();
    }//end googlemap


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                address = strReturnedAddress.toString();
                mSearchText.setText(strReturnedAddress.toString().trim());

          /*      Snackbar snackbar = Snackbar.make(view.findViewById(android.R.id.content),
                        ""+ Latitude +","+ Longitude,
                        Snackbar.LENGTH_SHORT
                );
                snackbar.show();*/
                Log.d("GetLocationActivity", ""+ Latitude +","+ Longitude);

                //Toast.makeText(getActivity(),"Address: " + strReturnedAddress.toString(), Toast.LENGTH_SHORT).show();
                if (setlocation.getVisibility() == View.INVISIBLE) {
                    setlocation.startAnimation(myFadeInAnimation);
                    setlocation.setVisibility(View.VISIBLE);
                }
                set_address = strReturnedAddress.toString();
                set_coordinates = Latitude +","+Longitude;
                Log.d("GetLocationActivity", strReturnedAddress.toString());
            } else {
                Log.w("Get", "No Address returned!");
            }
        } catch(Exception e) {
            e.printStackTrace();
            Log.w("GetLocationActivity", "Canont get Address!");
        }
        return strAdd;


    }

    @OnClick(R.id.searchbar)
    public void searchbar(View view){
        mSearchText.setText("");
    }

    private void init() {


        try {

            mGoogleApiClient = new GoogleApiClient
                    .Builder(getContext())
                    .enableAutoManage(getActivity(), this)

                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();

            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setCountry("PH")
                    .build();

         /*   mGoogleApiClient = new GoogleApiClient
                    .Builder(this)
                    .enableAutoManage(this *//* FragmentActivity *//*,
                            this *//* OnConnectionFailedListener *//*)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .build();*/



            placeAutocompleteAdapter = new PlaceAutocompleteAdapter(getActivity(), mGoogleApiClient, LAT_LNG_BOUNDS, typeFilter);

            mSearchText.setAdapter(placeAutocompleteAdapter);

        }catch (Exception ex){
            Toast.makeText(getActivity(), ""+ ex, Toast.LENGTH_SHORT).show();
        }

            mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH
                            || actionId == EditorInfo.IME_ACTION_DONE
                            || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                            || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                        //execute our method for searching
                        geoLocate();
                    }

                    return false;


                }
            });

    }

    @Override
    public void onCameraMoveStarted(int reason) {

        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            //"The user gestured on the map."
            focus_location = 0;
            if (mylocation.getVisibility() == View.INVISIBLE) {
                mylocation.startAnimation(myFadeInAnimation);
                mylocation.setVisibility(View.VISIBLE);
            }
            if (setlocation.getVisibility() == View.VISIBLE) {
                setlocation.startAnimation(myFadeOutAnimation);
                setlocation.setVisibility(View.INVISIBLE);
            }

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            hidenavbar();
        } else if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION) {
            //"The user tapped something on the map."
        } else if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION) {
            //"The app moved the camera."
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

        //"The camera has stopped moving."
        String location = "" + mMap.getCameraPosition().target;
        current_location = mMap.getCameraPosition().target;

        String locationrep1 = location.replace("lat/lng: (", "");
        String locationrep2 = locationrep1.replace(")", "");
        String[] coordinate_separated = locationrep2.split(",");
        coordinates = locationrep2.toString();
        Latitude = coordinate_separated[0];
        Longitude = coordinate_separated[1];
        getCompleteAddressString(Double.parseDouble(coordinate_separated[0]), Double.parseDouble(coordinate_separated[1]));

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

    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString().trim();

        Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchString, 20);
        } catch(IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }


        for(int i=0; i<list.size(); i++){

            Address address = list.get(i);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            CameraPosition position = new CameraPosition.Builder().target(new LatLng(address.getLatitude(), address.getLongitude())).zoom(15).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));

            hidenavbar();

        }
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



    private void initMap() {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment == null){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
        }
        mapFragment.getMapAsync(this);

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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

