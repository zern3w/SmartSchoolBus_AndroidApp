package com.project.ischoolbus.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.project.ischoolbus.R;
import com.project.ischoolbus.model.Driver;
import com.project.ischoolbus.tools.EventManager;
import com.project.ischoolbus.tools.Tools;
import com.squareup.picasso.Picasso;
import com.vstechlab.easyfonts.EasyFonts;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Created by Puttipong New on 9/2/2559.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
//    Button btnAddParent, btnEditProfile;
    Button btnStudentList, btnQrScan;
    Intent intent = new Intent();
    TextView tvName, tvPlateNo, tvSpeed;
    ImageView imgPhoto;
    AsyncHttpClient client;
    ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
    private GoogleApiClient googleApiClient;

    RequestParams params;
    String driverId, txt;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String DRIVERId = "driver_id";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInstance();

        params = new RequestParams();
        client = new AsyncHttpClient();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        driverId = sharedpreferences.getString(DRIVERId, "");
        params.put("driver_id", driverId);

        showData(driverId);

        if(isGPSEnable()==false){
            //Toast.makeText(getApplicationContext(),"Please turn on the location services!.", Toast.LENGTH_LONG).show();
            tvSpeed.setTextSize(20);
            tvSpeed.setText("Click HERE to turn on \nthe location services.");
            tvSpeed.setOnClickListener(this);
        }
    }

    private void showData(String driver_id) {
        params = new RequestParams();
        params.put("driver_id", driver_id);

        EventManager eventManager = new EventManager();
        eventManager.getDriverProfile(params, new EventManager.DriverProfileListener() {

            @Override
            public void onInternetDown() {

            }

            @Override
            public void onSuccess(Driver driver) {
                txt = driver.getDriver_firstname() + "  " + driver.getDriver_lastname();
                tvName.setText(txt);
                tvPlateNo.setText(driver.getPlate_number());

                Picasso.with(getApplicationContext()).load(R.drawable.sb1482262463).into(imgPhoto);
            }

            @Override
            public void onFailed() {

            }

            @Override
            public void onFailedError(String errorMsg) {

            }
        });
    }

    private void initInstance() {
        tvName = (TextView) findViewById(R.id.tvName);
        imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
//        btnSpecialCases = (Button) findViewById(R.id.btnSpecialCases);
//        btnSpecialCases.setOnClickListener(this);
//        btnAddParent = (Button) findViewById(R.id.btnAddParent);
//        btnAddParent.setOnClickListener(this);
//        btnParentList = (Button) findViewById(R.id.btnParentList);
//        btnParentList.setOnClickListener(this);
        btnStudentList = (Button) findViewById(R.id.btnStudentList);
        btnStudentList.setOnClickListener(this);
//        btnEditProfile = (Button) findViewById(R.id.btnEditProfile);
//        btnEditProfile.setOnClickListener(this);
        btnQrScan = (Button) findViewById(R.id.btnQrScan);
        btnQrScan.setOnClickListener(this);

        tvName.setTypeface(EasyFonts.droidSerifBold(this));
        tvPlateNo = (TextView) findViewById(R.id.tvPlateNo);
        tvSpeed = (TextView) findViewById(R.id.tvSpeed);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onClick(View v) {
//        if (v == btnAddParent) {
//            intent = new Intent(getApplicationContext(), AddParentActivity.class);
////            intent.putExtra("DRIVER_ID", driverId);
//            startActivity(intent);
//        if (v == btnParentList) {
//            intent = new Intent(getApplicationContext(), ParentListActivity.class);
//            startActivity(intent);
        if (v == btnStudentList) {
            intent = new Intent(getApplicationContext(), StudentListActivity.class);
            startActivity(intent);
//        } else if (v == btnEditProfile) {
//            intent = new Intent(getApplicationContext(), EditProfileActivity.class);
//            startActivity(intent);
        } else if (v == btnQrScan) {
            intent = new Intent(getApplicationContext(), CheckAttendanceActivity.class);
            startActivity(intent);
        } else if (v == tvSpeed){
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
           // tvSpeed.setTextSize(85);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);
        if (locationAvailability.isLocationAvailable()) {
            LocationRequest locationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(2000);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } else {
            // Do something when location provider not available
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("SpeedAlertActivity", "Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("SpeedAlertActivity", "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        double speed = location.getSpeed();

        speed = (speed * 3600) / 1000;      // speed in km/minute

        DecimalFormat df = new DecimalFormat("###.##");
        speed = Double.valueOf(df.format(speed));

        tvSpeed.setText(String.valueOf(speed));

        Tools.speedLimitAlert(speed,tg);

    }


    public boolean isGPSEnable(){
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            return false;
        }else {return true;}

    }

    private void logOut() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        Log.d("LogOut", "Now log out and start the activity login");
        Intent intent = new Intent(MainActivity.this,
                LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        Toast.makeText(getApplicationContext(), "You have successfully logged out!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                logOut();
                return true;
            default:
                return false;
        }
    }
  }
