package com.example.jude.prototype;

import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    ImageView profilePic;
    TextView nameView;
    TextView idView;
    TextView statusView;
    Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nameView=(TextView)findViewById(R.id.nameView);
        idView=(TextView)findViewById(R.id.nameView);
        statusView=(TextView)findViewById(R.id.statusView);
        Intent intent=getIntent();
        final String user=intent.getStringExtra("user");
        idView.setText(user);
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
               final String status=new Connector().CheckTimeIn(user);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(status=="1")
                        {
                            statusView.setText("Timed In");
                        }
                        else if(status=="0")
                        {
                            statusView.setText("Timed Out");
                        }
                        else
                            statusView.setText("Network Error");
                    }
                });
            }
        });
        thread.start();

    }
    @Override
    public void onBackPressed()
    {

        thread.interrupt();
        this.finish();
    }
}
