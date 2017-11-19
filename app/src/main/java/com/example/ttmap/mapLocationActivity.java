package com.example.ttmap;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;

public class mapLocationActivity extends AppCompatActivity {
    MapView mMapView = null;
    public AMapLocationClient mlocationClient;
    public AMapLocationClientOption mLocationOption = null;
    TextView mTextView;
    AMap aMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location);
        mTextView = (TextView) findViewById(R.id.amap_tv1);
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        if (mMapView == null) {
            aMap = mMapView.getMap();
            setUpMap();
        }
        mlocationClient = new AMapLocationClient(this.getApplicationContext());
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(2000);
        mLocationOption.setNeedAddress(true);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.setLocationListener(aMapLocationListener);

        mlocationClient.startLocation();


    }
    private void setUpMap() {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        myLocationStyle.showMyLocation(true);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setOnMyLocationChangeListener(mLocationChangeListener);
    }

    LocationSource mLocationSource = new LocationSource() {
        @Override
        public void activate(OnLocationChangedListener onLocationChangedListener) {
            //Toast.makeText(mapLocationActivity.this, "mLocationSource",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void deactivate() {

        }
    };

    AMap.OnMyLocationChangeListener mLocationChangeListener = new AMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            Toast.makeText(mapLocationActivity.this, "mLocationChangeListener",Toast.LENGTH_SHORT).show();
        }
    };

    AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            Toast.makeText(mapLocationActivity.this, "onlcationChange",Toast.LENGTH_SHORT).show();
            StringBuilder position = new StringBuilder();
//            position.append("Location:").append(aMapLocation.getAddress());
            position.append("经纬度：").append(aMapLocation.getLongitude()).append("   ").append(aMapLocation.getLatitude());
            position.append("\npoi: ").append(aMapLocation.getPoiName());
            if(aMapLocation.getLocationType() == AMapLocation.LOCATION_TYPE_GPS){
                position.append("\n 定位方式: ").append("GPS");
            }else{
                position.append("\n 定位方式: ").append("net");
            }
            mTextView.setText(position);
            aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mlocationClient.stopLocation();//停止定位
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
}
