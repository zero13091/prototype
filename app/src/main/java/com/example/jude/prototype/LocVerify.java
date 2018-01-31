package com.example.jude.prototype;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionApi;
//import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.Random;

public class LocVerify extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "";
    private static final int REQUEST_COARSE_LOCATION = 10;
    double x = 0, y = 0;
    GoogleMap mp;
    String sql_command,out,user;
    String check;
    int c;
    double xDefault=14.6042;
    double yDefault=120.9886;
    LocationManager locationManager;
    Button timeIn;
    Button timeOut;
    String timeoutcheck;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_verify);
       SupportMapFragment supportMapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        user= getIntent().getStringExtra("user");
        Random random=new Random();
        c=random.nextInt(5);
        getLocation();
        timeIn=(Button)findViewById(R.id.timein);
        timeOut=(Button)findViewById(R.id.timeout);
        CheckStatus();
        permCheck();
    }

public void getLocationB(View v)
{
    getLocation();
}

    public void getLocation()
    {
        try{
            locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener=new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    moveMap(mp,location.getLongitude(),location.getLatitude());
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, locationListener);
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        }
        catch(SecurityException e)
        {
            Toast.makeText(this,e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        Toast.makeText(this,x+" "+y, Toast.LENGTH_LONG).show();
    }






    public void moveMap(GoogleMap googleMap,double x,double y)
    {
        LatLng latLng=new LatLng(x,y);
        googleMap.addMarker(new MarkerOptions().position(latLng).title("I'm here"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,17.0f));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mp=googleMap;

        LatLng latLng=new LatLng(x,y);
        googleMap.addMarker(new MarkerOptions().position(latLng).title("I'm here"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,17.0f));
    }

    public void TimeIn(View v)
    {
        final String Time1=new Time_in().getTime();
        final String Date1=new Time_in().getDate();

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {

                Connector con=new Connector();
               final String late=con.checkLate(Time1,user);
               String sql_command="INSERT INTO Time (status,timein,date,username,late_hour) VALUES (1,'"+Time1+"','"+Date1+"','"+user+"','"+late+"')";
               // String sql_command="INSERT INTO Time (status,timein,date,username) VALUES (1,'"+Time1+"','"+Date1+"','"+user+"')";
                check=con.PerformTimeIn(sql_command);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LocVerify.this,late,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        thread.start();
        while(check==null)
        {

        }
        thread.interrupt();
        if(check=="okay")
        {
            new AlertDialog.Builder(this).setMessage("You are now timed in").show();
            finish();

        }
        else
        {
            Toast.makeText(this,out,Toast.LENGTH_SHORT).show();
        }
    }

    // TODO: 12/20/2017  
    public void CheckStatus()
    {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.attt);
        Thread thread= new Thread(new Runnable() {
            @Override
            public void run() {
                out=new Connector().CheckTimeIn(user);
                while(out==null)
                {

                }
                progressDialog.cancel();
                if (out=="1")
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timeIn.setEnabled(false);
                            timeOut.setEnabled(true);
                            builder.setMessage("You are Timed In.").show();
                        }
                    });



                }
                else if(out=="0")
                {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timeIn.setEnabled(true);
                            timeOut.setEnabled(false);
                            builder.setMessage("You are not Timed In").show();
                        }
                    });

                }
                else
                    Toast.makeText(LocVerify.this,out,Toast.LENGTH_SHORT).show();
            }
        });

        thread.start();
progressDialog.show();

    }

    public void Timeout(View v)
    {
        Thread thread= new Thread(new Runnable() {
            @Override
            public void run() {
                timeoutcheck=new Connector().performTimeOut(user);

            }
        });
        thread.start();
        while(timeoutcheck==null)
        {

        }
        thread.interrupt();
        if(timeoutcheck=="okay")
        {
            Toast.makeText(this,"Timed out",Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            Toast.makeText(this,timeoutcheck,Toast.LENGTH_SHORT).show();
        }

    }


    public void permCheck()
    {

        if((ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)||(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED))
        {


            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_COARSE_LOCATION);



        }
    }

    public boolean checkTimeDiff()
    {
        String time =new Time_in().getTime();
        return true;
    }
}

