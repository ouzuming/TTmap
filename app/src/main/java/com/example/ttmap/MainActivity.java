package com.example.ttmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button activifyBtn1,activifyBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activifyBtn1 = (Button) findViewById(R.id.mbtn1);
        activifyBtn2 = (Button) findViewById(R.id.mbtn2);
        activifyBtn1.setOnClickListener(this);
        activifyBtn2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
       switch (v.getId()){
           case R.id.mbtn1:
                intent = new Intent(MainActivity.this, blueMapActivity.class);
               break;
           case R.id.mbtn2:
               intent = new Intent(MainActivity.this, mapFragmentActivity.class);
               break;
       }
        startActivity(intent);
    }
}
