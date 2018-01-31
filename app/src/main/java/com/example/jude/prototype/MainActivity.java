package com.example.jude.prototype;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Thread thread;
    String s;
    String user;
    String pass;
    EditText userText;
    EditText passText;
    Toast toast;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userText=(EditText)findViewById(R.id.userField);
        passText=(EditText)findViewById(R.id.passField);

    }

    private boolean getData()
    {
        user=userText.getText().toString();
        pass=passText.getText().toString();
        if(user.length()==0||pass.length()==0)
        {
            Toast.makeText(this,"Fields must be filled up",Toast.LENGTH_LONG).show();

            return false;
        }
        else if(user.length()>0||pass.length()>0)
        {
            return true;
        }
        return false;

    }
    public void login(View v) {
       final Button button=(Button)findViewById(R.id.verify);
        button.setClickable(false);
        s=null;
        getData();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Connecting");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                s="2";
            }
        });
        progressDialog.show();
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    s = new Connector().requestLogin(user, pass);
                    Handler handler=new Handler(Looper.getMainLooper());
                   Runnable runnable=new Runnable() {
                       @Override
                       public void run() {

                       }
                   };
                    while (s == null) {

                    }
                    if(s.length()==1) {


                        if (Integer.parseInt(s) == 1) {
                            openMenu();
                        }
                        else if (Integer.parseInt(s) == 0) {
                            s = null;
                           runnable=new Runnable() {
                                @Override
                                public void run() {
                                    displayMessage("Invalid Username/Password");
                                    button.setClickable(true);
                                }

                            };
                        }
                        else if (Integer.parseInt(s) == 2) {
                            s = null;
                            runnable=new Runnable() {
                                @Override
                                public void run() {
                                    displayMessage("Cancelled");
                                    button.setClickable(true);
                                }

                            };
                        }
                        else {
                           runnable=new Runnable() {
                                @Override
                                public void run() {
                                    displayMessage("s");
                                    button.setClickable(true);
                                }

                            };
                        }

                    }
                    else
                    {
                        runnable=new Runnable() {
                            @Override
                            public void run() {
                                displayMessage(s);
                                button.setClickable(true);
                            }

                        };
                    }





                    handler.post(runnable);
                }
            });
            thread.start();

        //progressDialog.dismiss();





    }
    public void openMenu()
    {
        s = null;
        finish();
        Intent intent = new Intent(MainActivity.this, menuActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    public void displayMessage(String message)
    {
        //toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.show();

    }

    public void openLocator(View v)
    {
        startActivity(new Intent(this,LocCheck.class));
    }
/*
    public boolean checkPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
            {
                return true;
            }
            else
            {

            }
        }
    }*/
}
