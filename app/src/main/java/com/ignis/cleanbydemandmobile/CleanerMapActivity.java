package com.ignis.cleanbydemandmobile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

//AppCompatActivity
public class CleanerMapActivity extends AppCompatActivity implements GoogleMap.OnCameraMoveStartedListener,
                                                                             GoogleMap.OnCameraMoveListener,
                                                                             GoogleMap.OnCameraMoveCanceledListener,
                                                                             GoogleMap.OnCameraIdleListener,
                                                                             OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    LatLng userLocation;
    LatLng centerph = new LatLng(12.496333, 123.008514);// PH_Luzon
    LatLng lastKnown_userLocation;
    LatLng locationnow;
    String locationnow_lat, locationnow_lng;

    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private DrawerLayout mdrawelayout;
    private Toolbar mtoolbar;

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

    private Boolean mLocationPermissionsGranted = false;

    private Unbinder unbinder;

    private Marker OtherUser;

    ArrayList<String> Locations = new ArrayList<String>();
    @BindView(R.id.mylocation)
    ImageView mylocation;

    Intent i;

    @BindView(R.id.nojobschedule)
    TextView nojobschedule;
    @BindView(R.id.infobar)
    RelativeLayout infobar;

    TextView h_email, h_username;
    CircleImageView h_profile1;
    RatingBar MyRating;

    SharedPreferences sharedPreferences;
    TextView timeleftnow;
    android.support.v7.app.AlertDialog dialogtime;

    LinearLayout first_section, second_section;

    private List<String> listdata = new ArrayList<>();

    String transaction_id = "";

    private ProgressDialog progressDialog;

    String infobar_data;

    String transaction_id1;
    String bldg_info;
    String type_clean;
    String cleaners;
    String hours;
    String price;
    String date_time;
    String time;
    String remarks;
    String transaction_status;
    String payment_status;
    String location;
    String cleaner;
    String method;
    String profile;
    String name;
    String contact1;

    String date_time_click;

    TextView btnconfirm;

    String date_time_start, coordinates_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaner_map);
        ButterKnife.bind(this);
        getLocationPermission();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        progressDialog = new ProgressDialog(this);
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                int screenHeight = getWindow().getDecorView().getRootView().getHeight();

                int keypadHeight = screenHeight - r.bottom;

                //Log.d(TAG, "keypadHeight = " + keypadHeight);

                if (keypadHeight > screenHeight * 0.15) {
                    //Keyboard is opened

                } else {
                    // keyboard is closed
                    hidenavbar();
                }
            }
        });

        mtoolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mtoolbar);

        mdrawelayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mdrawelayout, R.string.open, R.string.close);

        mdrawelayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView action_title = (TextView) findViewById(R.id.action_title);
        action_title.setText("Home");


        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        h_username = (TextView) headerView.findViewById(R.id.h_username);
        h_profile1 = (CircleImageView) headerView.findViewById(R.id.h_profile);
        h_email = (TextView) headerView.findViewById(R.id.h_email);
        MyRating = (RatingBar) headerView.findViewById(R.id.MyRating);


        try {

            h_username.setText(sharedPreferences.getString("username", "").toString().trim());
            h_email.setText(sharedPreferences.getString("email", "").toString());
            double rate = Double.parseDouble(sharedPreferences.getString("rating", "").trim());
            int finalrate = (int) rate;
            MyRating.setRating(finalrate);

            Picasso.with(this)
                    .load(sharedPreferences.getString("profile", "").toString())
                    .into(h_profile1);

              /*  Picasso.with(getActivity())
                    .load(sharedPreferences.getString("profile", ""))
                    .into(h_profile);*/

        } catch(Exception e) {
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.d_home:

                        item.setChecked(true);
                        mdrawelayout.closeDrawers();
                        break;

                    case R.id.d_myinfo:

                        i = new Intent(getBaseContext(), MainActivityFragment.class);
                        i.putExtra("fragment_state", "myinfo");
                        startActivity(i);

                        item.setChecked(true);
                        mdrawelayout.closeDrawers();
                        break;

                    case R.id.d_schedules:

                        i = new Intent(getBaseContext(), MainActivityFragment.class);
                        i.putExtra("fragment_state", "schedule");
                        startActivity(i);

                        item.setChecked(true);
                        mdrawelayout.closeDrawers();
                        break;

                    case R.id.d_history:

                        i = new Intent(getBaseContext(), MainActivityFragment.class);
                        i.putExtra("fragment_state", "history");
                        startActivity(i);

                        item.setChecked(true);
                        mdrawelayout.closeDrawers();
                        break;

                 /*   case R.id.d_settings:

                        i = new Intent(getBaseContext(), MainActivityFragment.class);
                        i.putExtra("fragment_state", "settings");
                        startActivity(i);

                        item.setChecked(true);
                        mdrawelayout.closeDrawers();
                        break;*/

                    case R.id.d_aboutus:

                        i = new Intent(getBaseContext(), MainActivityFragment.class);
                        i.putExtra("fragment_state", "aboutus");
                        startActivity(i);
                        item.setChecked(true);
                        mdrawelayout.closeDrawers();
                        break;

                    case R.id.d_logout:

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("id", "");
                        editor.putString("role", "");
                        editor.commit();

                        Intent v = new Intent(getBaseContext(), Login_SignupActivity.class);
                        startActivity(v);
                        finish();
                        item.setChecked(true);
                        mdrawelayout.closeDrawers();
                        break;

                }
                return true;
            }
        });

        myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein_icon);
        myFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fadeout_icon);


    }//end oncreate

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

    @OnClick(R.id.infobar)
    public void infobar(View view) {
        String transac = sharedPreferences.getString("transaction", "");
        if (transac.toString().trim().equals("yes")) {
            timeleft();
        } else {

            try {
                android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_schedule_info, null);

                TextView call = (TextView) mView.findViewById(R.id.call);
                final TextView contact = (TextView) mView.findViewById(R.id.contact);

                TextView messagecontent = (TextView) mView.findViewById(R.id.d_message_content);
                TextView addresscontent = (TextView) mView.findViewById(R.id.d_address_content);
                TextView d_date = (TextView) mView.findViewById(R.id.d_date);
                TextView d_time = (TextView) mView.findViewById(R.id.d_time);
                TextView d_cleaner = (TextView) mView.findViewById(R.id.d_cleaner);
                TextView d_payment = (TextView) mView.findViewById(R.id.d_payment);
                TextView d_username = (TextView) mView.findViewById(R.id.d_username);
                TextView d_price = (TextView) mView.findViewById(R.id.d_price);
                TextView d_service = (TextView) mView.findViewById(R.id.d_service);


                LinearLayout servicebg = (LinearLayout) mView.findViewById(R.id.servicebg);

                mBuilder.setView(mView);
                final android.support.v7.app.AlertDialog dialog = mBuilder.create();
                dialog.show();

                messagecontent.setMovementMethod(new ScrollingMovementMethod());
                addresscontent.setMovementMethod(new ScrollingMovementMethod());
                d_date.setText(date_time);
                d_time.setText(time);
                d_cleaner.setText(cleaners);
                d_payment.setText(method);
                d_username.setText(name);
                d_service.setText(type_clean + " (" + hours + " Hours)");
                d_price.setText("Total : ₱ " + price);
                messagecontent.setText(remarks);
                addresscontent.setText(location);


                try {
                    if (type_clean.trim().contains("Deluxe Cleaning")){
                        servicebg.setBackgroundResource(R.drawable.d_deluxe);
                    }else if(type_clean.trim().contains("Premium Cleaning")){
                        servicebg.setBackgroundResource(R.drawable.d_premium);
                    }else if(type_clean.trim().contains("Yaya for a day")){
                        servicebg.setBackgroundResource(R.drawable.d_yaya);
                    }

                } catch(Exception e) {

                }

                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("transaction", "yes");
                        editor.commit();

                        hidenavbar();
                        dialog.hide();

                        getlocationnow();

                    }
                });


                contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+contact1));
                        startActivity(intent);

                    }
                });

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        hidenavbar();
                    }
                });

            } catch(Exception e) {
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

      /*  String transac = sharedPreferences.getString("transaction", "");

        if (transac.toString().trim().equals("yes")) {
            timeleft();
        } else {

        }*/

        BackGround3 booknow = new BackGround3();
        booknow.execute();

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
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch(Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

//------------------------------------------------------------------------------------User Location

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                Log.d(TAG, "user location: " + userLocation);


                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();

                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    hidenavbar();

                    if (userLocation != null && focus_location == 1) {

                        CameraPosition position = new CameraPosition.Builder().target(userLocation).zoom(17).build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));

                        if (mMap.isMyLocationEnabled() == false) {
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

        BackGround booknow = new BackGround();
        booknow.execute();


    }//end googlemap

    public void getlocationnow() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        if (location != null) {
            //NETWORK_PROVIDER
            double latti = location.getLatitude();
            double longi = location.getLongitude();
            locationnow = new LatLng(latti, longi);

        } else if (location1 != null) {
            //GPS_PROVIDER
            double latti = location1.getLatitude();
            double longi = location1.getLongitude();
            locationnow = new LatLng(latti, longi);

        } else if (location2 != null) {
            //PASSIVE_PROVIDER
            double latti = location2.getLatitude();
            double longi = location2.getLongitude();
            locationnow = new LatLng(latti, longi);
        }

      /*  SharedPreferences.Editor editor = sharedPreferences.edit();
        locationnow_lat = "" + locationnow.latitude;
        locationnow_lng = "" + locationnow.longitude;
        editor.putString("coordinates", locationnow_lat + "," + locationnow_lng);

*/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String currentDateandTime = sdf.format(new Date());


        coordinates_start = locationnow_lat + "," + locationnow_lng;
        date_time_start = currentDateandTime;

        String transac = sharedPreferences.getString("transaction", "");

        if (transac.toString().trim().equals("yes")) {
            BackGround5 booking_accept = new BackGround5();
            booking_accept.execute();

        }else if (transac.toString().trim().equals("no")){
            BackGround6 booking_accept = new BackGround6();
            booking_accept.execute();
        }
    }

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

    @Override
    public void onResume() {
        super.onResume();
        hidenavbar();
        super.onResume();
        registerReceiver(br, new IntentFilter(BroadcastService.COUNTDOWN_BR));
        Log.i(TAG, "Registered broacast receiver");


    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUI(intent); // or whatever method used to update your GUI fields
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(br);
        Log.i(TAG, "Unregistered broacast receiver");
    }

    @Override
    public void onStop() {
        try {
            unregisterReceiver(br);
        } catch(Exception e) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, BroadcastService.class));

        Log.i(TAG, "Stopped service");
        Toast.makeText(this, ""+timeleftnow.getText(), Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    private void updateGUI(Intent intent) {
        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 0);

            long millis = millisUntilFinished;
            //Convert milliseconds into hour,minute and seconds
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            Log.i(TAG, "Countdown seconds remaining: " + hms);

            timeleftnow.setText(hms);

            if (timeleftnow.getText().toString().contains("00:00:00")) {

                timeleftnow.setText("Finish!");

                stopService(new Intent(this, BroadcastService.class));
                first_section.setVisibility(View.INVISIBLE);
                second_section.setVisibility(View.VISIBLE);

                second_section.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                second_section.requestLayout();

            }

            dialogtime.setCancelable(false);
            dialogtime.setCanceledOnTouchOutside(false);

            // String transac = sharedPreferences.getString("transaction", "");
            // Toast.makeText(this, "" + transac, Toast.LENGTH_SHORT).show();
        }
    }

    private void hidenavbar() {

        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        currentMarker = marker;
        transaction_id = "" + marker.getTag();

        progressDialog.setMessage("Please wait");
        progressDialog.show();


        BackGround1 booknow = new BackGround1();
        booknow.execute();

        // Toast.makeText(this, transaction_id, Toast.LENGTH_SHORT).show();

        return false;
    }

    @Override
    public void onBackPressed() {

    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Log.d(TAG, "onRequestPermissionsResult: called.");
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

    private void getLocationPermission() {
        // Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = { Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION };

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public void timeleft() {
        try {
            android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_transactionstart, null);

            timeleftnow = (TextView) mView.findViewById(R.id.timeleft);
            first_section = (LinearLayout) mView.findViewById(R.id.first_section);
            second_section = (LinearLayout) mView.findViewById(R.id.second_section);
            Button finishtransaction = (Button) mView.findViewById(R.id.finishtransaction);

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            String currentDateandTime = sdf.format(new Date());

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("transaction", "no");
            editor.commit();

            ViewGroup.LayoutParams params = second_section.getLayoutParams();
            params.height = 100;
            second_section.setLayoutParams(params);

            String deluxe = "120";
            String premium = "240";
            String yaya = "480"; //todo

            Intent serviceIntent = new Intent(this, BroadcastService.class);

            if(type_clean.contains("Deluxe")) {
                serviceIntent.putExtra("time", deluxe);
                startService(serviceIntent);
            } else if(type_clean.contains("Premium")) {
                serviceIntent.putExtra("time", premium);
                startService(serviceIntent);
            } else if(type_clean.contains("Yaya")) {
                serviceIntent.putExtra("time", yaya);
                startService(serviceIntent);
            }

            Log.i(TAG, "Started service");

            timeleftnow.setText("Finish!");

            finishtransaction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(CleanerMapActivity.this, "finish transaction", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("transaction", "no");

                    editor.commit();
                    getlocationnow();

                    dialogtime.hide();
                    hidenavbar();
                }
            });

            mBuilder.setView(mView);
            dialogtime = mBuilder.create();
            dialogtime.show();


            dialogtime.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {

                }
            });

        } catch(Exception e) {
        }
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";
            int tmp;

            try {
                URL url = new URL("http://cleanbydemand.com/php/m_function.php");
                String urlParams = "id=" + 10;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch(MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch(IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String err = null;

            try {
                JSONArray jsonArray = new JSONArray(s);
                int count = jsonArray.length();

                //Toast.makeText(getActivity(), "" + s, Toast.LENGTH_SHORT).show();

                for (int v = 0; v < count; v++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(v);
                    listdata.add(jsonObject.getString("transaction_id") + "_-/" +
                                         jsonObject.getString("coordinate") + "_-/");


                }

                //  Toast.makeText(CleanerMapActivity.this, ""+listdata.size(), Toast.LENGTH_SHORT).show();

                for (int a = 0; a < listdata.size(); a++) {

                    final String[] separated = listdata.get(a).split("_-/");

                    final String[] location = separated[1].split(",");


                    Double lat = Double.parseDouble(location[0]);
                    Double lng = Double.parseDouble(location[1]);

                    LatLng UsersCoordinate = new LatLng(lat, lng);

                    //  Log.d(TAG, "Coordinates: " + UsersCoordinate.toString());
                    OtherUser = mMap.addMarker(new MarkerOptions()
                                                       .position(UsersCoordinate)
                                                       .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1)));
                    OtherUser.setTag(separated[0]);
                }


            } catch(JSONException er) {

            }


        }
    }

    class BackGround1 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";
            int tmp;

            try {
                URL url = new URL("http://cleanbydemand.com/php/m_function.php");
                String urlParams = "id=" + 12 + "&trans_id=" + transaction_id;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch(MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch(IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String err = null;

            progressDialog.dismiss();
            final String data;

            try {
                JSONArray jsonArray = new JSONArray(s);
                int count = jsonArray.length();

                JSONObject jsonObject = jsonArray.getJSONObject(0);
                data = (jsonObject.getString("name") + "_-/" +
                                jsonObject.getString("type_clean") + "_-/" +
                                jsonObject.getString("date_time") + "_-/" +
                                jsonObject.getString("hours_from") + "_-/" +
                                jsonObject.getString("remarks") + "_-/" +
                                jsonObject.getString("payment_method") + "_-/" +
                                jsonObject.getString("cleaners") + "_-/" +
                                jsonObject.getString("transaction_id") + "_-/" +
                                jsonObject.getString("hours") + "_-/" +
                                jsonObject.getString("profile") + "_-/" +
                                jsonObject.getString("location"));

                String[] value = data.split("_-/");

                String name = value[0];
                String type_clean = value[1];
                String date_time = value[2];
                String hours_from = value[3];
                String remarks = value[4];
                String payment_method = value[5];
                String cleaners = value[6];
                String transaction_id = value[7];
                String hours = value[8];
                String profile = value[9];
                String location = value[10];
                date_time_click = date_time;


                android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(CleanerMapActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_booking_info, null);
                btnconfirm = (TextView) mView.findViewById(R.id.accept);
                final TextView btnclose = (TextView) mView.findViewById(R.id.close);
                final TextView messagecontent = (TextView) mView.findViewById(R.id.d_message_content);
                final TextView d_service1 = (TextView) mView.findViewById(R.id.d_service);
                final TextView d_date1 = (TextView) mView.findViewById(R.id.d_date);
                final TextView d_time1 = (TextView) mView.findViewById(R.id.d_time);
                final TextView d_cleaner1 = (TextView) mView.findViewById(R.id.d_cleaner);
                final TextView d_payment1 = (TextView) mView.findViewById(R.id.d_payment);
                final TextView d_username1 = (TextView) mView.findViewById(R.id.d_username);
                final TextView address = (TextView) mView.findViewById(R.id.d_address_content);
                final CircleImageView h_profile1 = (CircleImageView) mView.findViewById(R.id.h_profile);

                Picasso.with(CleanerMapActivity.this)
                        .load("http://cleanbydemand.com/php/profile/" + profile)
                        .into(h_profile1);

                String[] username = name.split(",");


                d_username1.setText(" " + username[0] + " " + username[1]);
                d_service1.setText(" " + type_clean + " (" + hours + " Hours)");
                d_date1.setText(" " + date_time);
                d_time1.setText(" " + hours_from);
                d_cleaner1.setText(" " + cleaners);
                messagecontent.setText(" " + remarks);
                d_payment1.setText(" " + payment_method);
                address.setText(location);

                if (payment_method.contains("CASH")) {
                    d_payment1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.d_cash, 0, 0, 0);
                } else {
                    d_payment1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.d_credit_card, 0, 0, 0);

                }

                mBuilder.setView(mView);
                final android.support.v7.app.AlertDialog dialog = mBuilder.create();
                dialog.show();
                messagecontent.setMovementMethod(new ScrollingMovementMethod());


                btnconfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressDialog.setMessage("Proccessing");
                        progressDialog.show();

                        BackGround2 booking_accept = new BackGround2();
                        booking_accept.execute();


                        hidenavbar();
                        dialog.hide();


                    }
                });

                btnclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hidenavbar();
                        dialog.hide();
                    }
                });

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        hidenavbar();
                    }
                });

                BackGround4 booking_accept = new BackGround4();
                booking_accept.execute();

            } catch(JSONException er) {
            }


        }
    }

    class BackGround2 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String user_id = sharedPreferences.getString("id", "").toString();

            String data = "";
            int tmp;

            try {
                URL url = new URL("http://cleanbydemand.com/php/m_function.php");
                String urlParams = "id=" + 14 + "&trans_id=" + transaction_id + "&user_id=" + user_id;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch(MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch(IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String err = null;
            progressDialog.dismiss();

           // Toast.makeText(CleanerMapActivity.this, s.trim(), Toast.LENGTH_LONG).show();

            if (!s.contains("Maximum Cleaner Reach") || !s.contains("Transaction Already Accepted")) {
                i = new Intent(getBaseContext(), MainActivityFragment.class);
                i.putExtra("fragment_state", "schedule");
                startActivity(i);
            } else {

                Toast.makeText(CleanerMapActivity.this, s.trim(), Toast.LENGTH_LONG).show();
            }


        }
    }

    class BackGround3 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(CleanerMapActivity.this);
            String id = sharedPreferences.getString("id", "");

            String data = "";
            int tmp;
            try {
                URL url = new URL("http://cleanbydemand.com/php/m_function.php");
                String urlParams = "id=" + 8 + "&user_id=" + id;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch(MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch(IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String err = null;

            try {
                JSONArray jsonArray = new JSONArray(s);
                int count = jsonArray.length();

                if (count > 0) {

                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    infobar_data = (jsonObject.getString("transaction_id") + " _-/" +
                                            jsonObject.getString("bldg_info") + " _-/" +
                                            jsonObject.getString("type_clean") + " _-/" +
                                            jsonObject.getString("cleaners") + " _-/" +
                                            jsonObject.getString("hours") + " _-/" +
                                            jsonObject.getString("price") + " _-/" +
                                            jsonObject.getString("date_time") + " _-/" +
                                            jsonObject.getString("time") + " _-/" +
                                            jsonObject.getString("remarks") + " _-/" +
                                            jsonObject.getString("transaction_status") + " _-/" +
                                            jsonObject.getString("payment_status") + " _-/" +
                                            jsonObject.getString("location") + " _-/" +
                                            jsonObject.getString("cleaner") + " _-/" +
                                            jsonObject.getString("payment_method") + " _-/" +
                                            jsonObject.getString("rate") + " _-/" +
                                            jsonObject.getString("profile") + " _-/" +
                                            jsonObject.getString("name")+ " _-/" +
                                            jsonObject.getString("contact"));

                    String[] value = infobar_data.split("_-/");

                    transaction_id1 = value[0];
                    bldg_info = value[1];
                    type_clean = value[2];
                    cleaners = value[3];
                    hours = value[4];
                    price = value[5];
                    date_time = value[6];
                    time = value[7];
                    remarks = value[8];
                    transaction_status = value[9];
                    payment_status = value[10];
                    location = value[11];
                    cleaner = value[12];
                    method = value[13];
                    profile = value[15];
                    name = value[16];
                    contact1 = value[17];

                    String[] username = name.split(",");
                    name = username[0] + " " + username[1];
                    TextView b_username2 = (TextView) findViewById(R.id.b_username);
                    TextView b_service2 = (TextView) findViewById(R.id.b_clean);
                    TextView b_date2 = (TextView) findViewById(R.id.b_date);
                    TextView b_time2 = (TextView) findViewById(R.id.b_time);
                    CircleImageView h_profile2 = (CircleImageView) findViewById(R.id.h_profile);

                    b_username2.setText(name);
                    b_service2.setText(type_clean);
                    b_date2.setText(date_time);
                    b_time2.setText(time);


                    Picasso.with(CleanerMapActivity.this)
                            .load("http://cleanbydemand.com/php/profile/" + profile)
                            .into(h_profile2);

                    infobar.setVisibility(View.VISIBLE);
                    nojobschedule.setVisibility(View.INVISIBLE);

                } else {

                    infobar.setVisibility(View.INVISIBLE);
                    nojobschedule.setVisibility(View.VISIBLE);
                }
            } catch(JSONException er) {

            }


        }
    }

    class BackGround4 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String user_id = sharedPreferences.getString("id", "").toString();

            String data = "";
            int tmp;

            try {
                URL url = new URL("http://cleanbydemand.com/php/m_function.php");
                String urlParams = "id=" + 11 + "&trans_id=" + transaction_id + "&user_id=" + user_id
                                           + "&date_time=" + date_time_click;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch(MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch(IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String err = null;
            progressDialog.dismiss();

            try {
                JSONArray jsonArray = new JSONArray(s);
                int count = jsonArray.length();




                if(count == 0){
                    btnconfirm.setEnabled(false);
                }else{
                    btnconfirm.setEnabled(true);
                }

            }
            catch(JSONException e){

            }
        }
    }

    class BackGround5 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String user_id = sharedPreferences.getString("id", "").toString();

            String data = "";
            int tmp;

            try {
                URL url = new URL("http://cleanbydemand.com/php/m_function.php");
                String urlParams = "id=" + 15 + "&trans_id=" + transaction_id1 + "&user_id=" + user_id
                                           + "&date_time=" + date_time_start +"&scoordinate=" + coordinates_start;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch(MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch(IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String err = null;

           // Toast.makeText(CleanerMapActivity.this, ""+s, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();

            timeleft();

        }
    }

    class BackGround6 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String user_id = sharedPreferences.getString("id", "").toString();

            String data = "";
            int tmp;

            try {
                URL url = new URL("http://cleanbydemand.com/php/m_function.php");
                String urlParams = "id=" + 16 + "&trans_id=" + transaction_id1 + "&user_id=" + user_id
                                           + "&date_time=" + date_time_start +"&scoordinate=" + coordinates_start;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch(MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch(IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String err = null;

          // Toast.makeText(CleanerMapActivity.this, ""+s, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }
}
