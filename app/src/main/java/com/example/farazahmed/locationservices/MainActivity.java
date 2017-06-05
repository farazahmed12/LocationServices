package com.example.farazahmed.locationservices;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String LOG_TAG = "farazApp";

    private static final int MY_REQUEST_PERMISSION = 200;

    private TextView tvoutput;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationRequest mLocationrRequest;
    private ConstraintLayout constraintlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        constraintlayout = (ConstraintLayout) findViewById(R.id.constraintlayout);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            tvoutput = (TextView) findViewById(R.id.txtLocation);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

       updateui();

    }
    @Override
    public void onLocationChanged(Location location) {

        updateui();
    }

    private void updateui()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        else {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            startLocationUpdates();
            if (mLocation != null) {
                tvoutput.setText(String.valueOf(mLocation.getLatitude()));
            }
        }
    }

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
        } else
            createLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationrRequest, this);
    }

    private void createLocationRequest() {
        mLocationrRequest = new LocationRequest();
        mLocationrRequest.setInterval(5000);
        mLocationrRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        requestPermissions();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            tvoutput.setTextColor(Color.YELLOW);
            tvoutput.setBackgroundColor(Color.BLACK);
            tvoutput.setTextSize(22);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void requestPermissions() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermissions(this, permissions)) {
                ActivityCompat.requestPermissions(this, permissions, MY_REQUEST_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_REQUEST_PERMISSION:
                if (grantResults.length > 0) {


                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    } else if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        Snackbar snackbar = Snackbar.make(constraintlayout, "Location Must Enable", BaseTransientBottomBar.LENGTH_INDEFINITE)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        requestPermissions();
                                    }
                                });
                        snackbar.show();
                    } else {
                        Snackbar snackbar = Snackbar.make(constraintlayout, "Location Must Enable", BaseTransientBottomBar.LENGTH_INDEFINITE)
                                .setAction("GOTO SETTINGS", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    }
                                });
                        snackbar.show();

                    }
                    if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {


                    } else if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                        Snackbar snackbar = Snackbar.make(constraintlayout, "Camera Must Enable", BaseTransientBottomBar.LENGTH_INDEFINITE)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        requestPermissions();
                                    }
                                });
                        snackbar.show();
                    } else {
                        Snackbar snackbar = Snackbar.make(constraintlayout, "Camera Must Enable", BaseTransientBottomBar.LENGTH_INDEFINITE)
                                .setAction("GOTO SETTINGS", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    }
                                });
                        snackbar.show();

                    }
                    if (grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                    } else if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Snackbar snackbar = Snackbar.make(constraintlayout, "Storage Must Enable", BaseTransientBottomBar.LENGTH_INDEFINITE)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        requestPermissions();
                                    }
                                });
                        snackbar.show();
                    } else {
                        Snackbar snackbar = Snackbar.make(constraintlayout, "Storage Must Enable", BaseTransientBottomBar.LENGTH_INDEFINITE)
                                .setAction("GOTO SETTINGS", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    }
                                });
                        snackbar.show();

                    }
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        tvoutput.setTextColor(Color.YELLOW);
                        tvoutput.setBackgroundColor(Color.BLACK);
                        tvoutput.setTextSize(22);
                    }

                }
                break;

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
