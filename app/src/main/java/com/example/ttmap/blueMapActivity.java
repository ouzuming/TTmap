package com.example.ttmap;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;

public class blueMapActivity extends AppCompatActivity implements LocationSource,AMapLocationListener{
    private MapView mMapView;
    private AMap aMap;
    private AMapLocationClient mapLocationClient;
    private AMapLocationClientOption mLocationOption;
    private OnLocationChangedListener mListener;
    private boolean isFirstLoc = true;
    TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_map);
        mTextView = (TextView) findViewById(R.id.blue_tv1);
        mMapView  = (MapView) findViewById(R.id.bluemap);
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();

        UiSettings settings = aMap.getUiSettings();
        aMap.setLocationSource(this);
        //settings.setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
        ininLoc();
    }

    private void  ininLoc()
    {
        mapLocationClient = new AMapLocationClient(getApplicationContext());
        mapLocationClient.setLocationListener(this);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(10000);
        mLocationOption.setSensorEnable(true);
        mLocationOption.setNeedAddress(true);
        mLocationOption.setOnceLocationLatest(false);
        mapLocationClient.setLocationOption(mLocationOption);
        mapLocationClient.setLocationListener(mLocationListener);
        mapLocationClient.startLocation();

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;

    }

    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            Toast.makeText(blueMapActivity.this,"onLocationChanged",Toast.LENGTH_SHORT).show();
            mTextView.setText("start loading....");
        }
    };
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if((aMapLocation != null) && (aMapLocation.getErrorCode() == 0)){
            Toast.makeText(blueMapActivity.this,"onLcationChanged",Toast.LENGTH_SHORT).show();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("经纬度:").append(aMapLocation.getLatitude())
                    .append(  aMapLocation.getLongitude());
            mTextView.setText(stringBuilder);

            if(isFirstLoc){
                aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                aMap.moveCamera(CameraUpdateFactory.
                        changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                mListener.onLocationChanged(aMapLocation);
                aMap.addMarker(getMarkerOptions(aMapLocation));
                isFirstLoc =false;
            }
        }else{
            Toast.makeText(blueMapActivity.this,"location fail",Toast.LENGTH_SHORT).show();
        }

    }

    private MarkerOptions getMarkerOptions(AMapLocation amapLocation){
        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_default2d));
        options.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() +  "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
        //标题
        options.title(buffer.toString());
        return options;
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
