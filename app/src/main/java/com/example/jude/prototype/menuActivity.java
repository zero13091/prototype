package com.example.jude.prototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class menuActivity extends AppCompatActivity {
    ListView list;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        list=(ListView)findViewById(R.id.myList);
        String[] items={"Time In/Out","Profile","Check History"};
        final menuActivity hey =this;
      //  Integer[] icons={R.mipmap.time,R.mipmap.profile,R.mipmap.work};
        list.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    Intent intent=getIntent();
                    String user=intent.getStringExtra("user");
                    intent=new Intent(menuActivity.this,LocVerify.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                }
                else if (position==1)
                {
                    Intent intent=getIntent();
                    String user=intent.getStringExtra("user");
                    intent=new Intent(menuActivity.this,ProfileActivity.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                }
                else if(position==2)
                {

                }
            }
        });
    }


}
