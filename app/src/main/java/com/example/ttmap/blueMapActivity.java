package com.example.ttmap;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.GroundOverlay;
import com.amap.api.maps.model.GroundOverlayOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import java.util.ArrayList;

import static android.icu.text.Normalizer.YES;

public class blueMapActivity extends AppCompatActivity implements LocationSource,AMapLocationListener
,AMap.OnMarkerClickListener,AMap.InfoWindowAdapter,AMap.OnInfoWindowClickListener,
        AMap.OnMapClickListener,AMap.OnMapLongClickListener,View.OnClickListener{
    private int markCounter = 0;
    private MapView mMapView;
    private AMap aMap;
    private AMapLocationClient mapLocationClient;
    private AMapLocationClientOption mLocationOption;
    private OnLocationChangedListener mListener;
    private boolean isFirstLoc = true;
    private  static int LocationCounter = 0;
    TextView mTextView;
    Button mapTypeBtn;
    Button mapClearBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_map);
        mTextView = (TextView) findViewById(R.id.blue_tv1);
        mapTypeBtn = (Button) findViewById(R.id.mapTypeBtn);
        mapClearBtn = (Button) findViewById(R.id.mapClearBtn);
        mMapView  = (MapView) findViewById(R.id.bluemap);
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();

        UiSettings settings = aMap.getUiSettings();
        aMap.setLocationSource(this);
        settings.setMyLocationButtonEnabled(true);
        settings.setCompassEnabled(true);
        settings.setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
        //aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        aMap.setOnMarkerClickListener(this);
        ininLoc();
        setMapLocationStyle();
        aMap.setOnMapClickListener(this);
        aMap.setOnMapLongClickListener(this);
        aMap.setInfoWindowAdapter(this);
        mapTypeBtn.setOnClickListener(this);
        mapClearBtn.setOnClickListener(this);

//        mapTypeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mapTypeBtn.getText().equals("卫星地图")){
//                    Toast.makeText(blueMapActivity.this,"卫星地图",Toast.LENGTH_SHORT).show();
//                    mapTypeBtn.setText("平面地图");
//                    aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
//                }else if(mapTypeBtn.getText().equals("平面地图")){
//                    Toast.makeText(blueMapActivity.this,"平面地图",Toast.LENGTH_SHORT).show();
//                    mapTypeBtn.setText("卫星地图");
//                    aMap.setMapType(AMap.MAP_TYPE_NORMAL);
//                }
//               // addOverlayToMap();
//            }
//        });
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
        mapLocationClient.startLocation();
    }
    private  void setMapLocationStyle(){
        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
//                fromResource(R.drawable.maps_dav_compass_needle_large2d));
        myLocationStyle.strokeColor(Color.RED);   // 边框颜色
        myLocationStyle.strokeWidth(2);
        aMap.setMyLocationStyle(myLocationStyle);
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
            Toast.makeText(blueMapActivity.this,"mLocationListener",Toast.LENGTH_SHORT).show();
            //mTextView.setText("start loading....");
        }
    };

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if((aMapLocation != null) && (aMapLocation.getErrorCode() == 0)){
            LocationCounter++;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("经纬度:").append((long) aMapLocation.getLatitude())
                    .append("  "+(long)aMapLocation.getLongitude()).append("\nLocationCounter:"+LocationCounter);
            mTextView.setText(stringBuilder);

            if(isFirstLoc){
                Toast.makeText(blueMapActivity.this,"siFirstLoc",Toast.LENGTH_SHORT).show();
                aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                aMap.moveCamera(CameraUpdateFactory.
                        changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                mListener.onLocationChanged(aMapLocation);
                aMap.addMarker(getMarkerOptions(aMapLocation));
                isFirstLoc =false;
                mListener.onLocationChanged(aMapLocation);
            }
        }else{
            Toast.makeText(blueMapActivity.this,"location fail",Toast.LENGTH_SHORT).show();
        }
    }

    private MarkerOptions getMarkerOptions(AMapLocation amapLocation){
        ArrayList<BitmapDescriptor> iconList = new ArrayList<>();
        iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.yellow));
        iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.violet2));
        MarkerOptions options = new MarkerOptions();
        options.anchor(0.5f,0.5f);
        options.icons(iconList);
        options.draggable(true);
        options.period(50);
        options.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() +  "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
        //标题
        options.title("position:").snippet(buffer.toString());
        options.draggable(true);

        return options;
    }
    // 添加图片到地图上
    private void addOverlayToMap(){
        GroundOverlayOptions groudOverlayOptions = new GroundOverlayOptions();
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.90886,116.39739),15));
        groudOverlayOptions.anchor(0.5f, 0.5f);
        groudOverlayOptions.transparency(0f);
        groudOverlayOptions.position(new LatLng(39.90886,116.39739),512,512);
        groudOverlayOptions.image(BitmapDescriptorFactory.fromResource(R.drawable.mapworld));
        aMap.addGroundOverlay(groudOverlayOptions);
        Toast.makeText(blueMapActivity.this,"addOVerlayTOMap",Toast.LENGTH_SHORT).show();
    }

    public void mapAddMark(LatLng mLatlng){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.violet2));
        markerOptions.anchor(0.5f,0.5f);
        markerOptions.draggable(true);
        markerOptions.title("position");
        StringBuilder mBuilder = new StringBuilder();
        mBuilder.append(mLatlng.longitude).append("  ").append(mLatlng.latitude);
        markerOptions.snippet(mBuilder.toString());
        markerOptions.position(mLatlng);
        aMap.addMarker(markerOptions);
        Toast.makeText(blueMapActivity.this,"map add mark",Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onMarkerClick(Marker marker) {

        marker.setInfoWindowEnable(true);
        marker.showInfoWindow();

       //Toast.makeText(blueMapActivity.this,"onMarkerClick",Toast.LENGTH_SHORT).show();
        return false;
    }


    @Override
    public View getInfoWindow(Marker marker) {

        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        //Toast.makeText(blueMapActivity.this,"getInfoContents",Toast.LENGTH_SHORT).show();
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        //Toast.makeText(blueMapActivity.this,"onInfoWindowClick",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapClick(LatLng latLng) {

        Toast.makeText(blueMapActivity.this,"onMapClick",Toast.LENGTH_SHORT).show();
        //Toast.makeText(blueMapActivity.this, (int) latLng.longitude,Toast.LENGTH_SHORT).show();
        mTextView.setText(""+latLng);

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Toast.makeText(blueMapActivity.this,"onMapLongClick",Toast.LENGTH_SHORT).show();
        if(markCounter < 5){
            Toast.makeText(blueMapActivity.this,"markCounter"+markCounter,Toast.LENGTH_SHORT).show();
            mapAddMark(latLng);
            markCounter++;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mapTypeBtn:
                if(mapTypeBtn.getText().equals("卫星地图")){
                    Toast.makeText(blueMapActivity.this,"卫星地图",Toast.LENGTH_SHORT).show();
                    mapTypeBtn.setText("平面地图");
                    aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
                }else if(mapTypeBtn.getText().equals("平面地图")){
                    Toast.makeText(blueMapActivity.this,"平面地图",Toast.LENGTH_SHORT).show();
                    mapTypeBtn.setText("卫星地图");
                    aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                }
                // addOverlayToMap();
                break;
            case R.id.mapClearBtn:
                aMap.clear();
                break;
        }
    }
}
