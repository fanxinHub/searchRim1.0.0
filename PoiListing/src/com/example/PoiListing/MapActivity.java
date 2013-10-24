package com.example.PoiListing;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import poilisting.demo.R;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: cdm
 * Date: 13-10-11
 * Time: AM10:08
 * To change this template use File | Settings | File Templates.
 */
public class MapActivity extends Activity {
    private BMapManager bMapManager;
    public static final String strKey = "DEc0fa4489ffc8c5972c77faf3e29130";
    private MapView mapView;
    private MapController mapController;
    private LayoutInflater layoutInflater;
    private View mapPopWindow;
    private double lat;
    private double lng;
    private PoiOverlay<OverlayItem> itemItemizedOverlay;
    private MyLocationOverlay myLocationOverlay;
    private Button locateMeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.


        //初始化BaiduMapManager
        bMapManager = new BMapManager(this);
        boolean initResult = bMapManager.init(strKey, new MKGeneralListener() {
            @Override
            public void onGetNetworkState(int iError) {
                if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                    Toast.makeText(MapActivity.this, "您的网络出错啦！", Toast.LENGTH_LONG).show();
                } else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                    Toast.makeText(MapActivity.this, "输入正确的检索条件！", Toast.LENGTH_LONG).show();
                }
            }

            @Override


            public void onGetPermissionState(int iError) {
                if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
                    Toast.makeText(MapActivity.this, "请在 DemoApplication.java文件输入正确的授权Key！", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (!initResult) {
            Toast.makeText(MapActivity.this, "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }

        setContentView(R.layout.poi_listing);

        layoutInflater = LayoutInflater.from(this);

        mapView = (MapView) findViewById(R.id.bmapView);

        mapView.setBuiltInZoomControls(false);
        //卫星图层
//        mapView.setSatellite(true);
        //交通图层
//        mapView.setTraffic(true);


        mapController = mapView.getController();
        //控制缩放等级
        mapController.setZoom(14);

        //移动到经纬度点
       final GeoPoint geoPoint = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
        //设置中心点
        mapController.setCenter(geoPoint);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mapController.animateTo(geoPoint);
            }
        }, 1000);

        Drawable marker = getResources().getDrawable(R.drawable.u73_normal);
       itemItemizedOverlay = new PoiOverlay<OverlayItem>(marker, mapView);


        for (int i = 0; i < 5; i++) {
            GeoPoint point = new GeoPoint(geoPoint.getLatitudeE6() + i * 10000, geoPoint.getLongitudeE6() + i * 10000);
            OverlayItem overlayItem = new OverlayItem(point, "我是标题 " + i, "我是内容 " + i);
            if(i==0){
            overlayItem.setMarker(getResources().getDrawable(R.drawable.u71_normal));
            }
            itemItemizedOverlay.addItem(overlayItem);
        }
        mapView.getOverlays().add(itemItemizedOverlay);

        //添加弹出窗口
        mapPopWindow = layoutInflater.inflate(R.layout.map_pop_window, null);
        mapPopWindow.setVisibility(View.GONE);
        mapView.addView(mapPopWindow);

        locateMeButton = (Button) findViewById(R.id.locButton);
        locateMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyLocation();
            }
        });

        myLocationOverlay = new MyLocationOverlay(getResources().getDrawable(R.drawable.u71_normal),mapView);
        mapView.getOverlays().add(myLocationOverlay);

    }

    class PoiOverlay<OverlayItem> extends ItemizedOverlay {

        public PoiOverlay(Drawable drawable, MapView mapView) {
            super(drawable, mapView);
        }

        @Override
        protected boolean onTap(int i) {
            Log.d("BaiduMapDemo", "onTap " + i);
            com.baidu.mapapi.map.OverlayItem item = itemItemizedOverlay.getItem(i);
            GeoPoint point = item.getPoint();
            String title = item.getTitle();

            TextView titleTextView = (TextView) mapPopWindow.findViewById(R.id.titleTextView);
            titleTextView.setText(title);

            MapView.LayoutParams layoutParam = new MapView.LayoutParams(
                    //控件宽,继承自ViewGroup.LayoutParams
                    MapView.LayoutParams.WRAP_CONTENT,
                    //控件高,继承自ViewGroup.LayoutParams
                    MapView.LayoutParams.WRAP_CONTENT,
                    //使控件固定在某个地理位置
                    point,
                    10,
                    -30,
                    //控件对齐方式
                    MapView.LayoutParams.BOTTOM_CENTER);

            mapPopWindow.setVisibility(View.VISIBLE);

            mapPopWindow.setLayoutParams(layoutParam);

            mapController.animateTo(point);

            return super.onTap(i);
        }

        @Override
        public boolean onTap(GeoPoint geoPoint, MapView mapView) {
            Log.d("BaiduMapDemo", "onTap geoPoint " + geoPoint);

            mapPopWindow.setVisibility(View.GONE);

            return super.onTap(geoPoint, mapView);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

    private void getMyLocation() {
        Log.d("BaiduMapDemo", "getMyLocation");

        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setAddrType("all");
        option.setOpenGps(true);

        LocationClient locationClient = new LocationClient(this);
        locationClient.setLocOption(option);

        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                Log.d("BaiduMapDemo", "onReceiveLocation address " + bdLocation.getAddrStr());
                Log.d("BaiduMapDemo", "onReceiveLocation Latitude " + bdLocation.getLatitude());
                Log.d("BaiduMapDemo", "onReceiveLocation Longitude " + bdLocation.getLongitude());

                double lng = bdLocation.getLongitude();
                double lat = bdLocation.getLatitude();

//                double lng = 34.25934463685013;
//                double lat = 108.94721031188965;

                int offset = (int) (new Random().nextFloat() * 10000);
                GeoPoint point = new GeoPoint((int) (lng * 1E6) + offset, (int) (lat * 1E6) - offset);

               myLocationOverlay.setMyLocation(point);

                mapView.refresh();
            }

            @Override
            public void onReceivePoi(BDLocation bdLocation) {
                Log.d("BaiduMapDemo", "onReceivePoi ");
            }
        });

        locationClient.start();
        locationClient.requestLocation();
    }
}
