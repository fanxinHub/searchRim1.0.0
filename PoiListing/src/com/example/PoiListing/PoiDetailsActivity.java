package com.example.PoiListing;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.search.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import poilisting.demo.R;



/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-22
 * Time: 上午11:30
 * To change this template use File | Settings | File Templates.
 */
public class PoiDetailsActivity extends Activity {
    public static final String strKey = "DEc0fa4489ffc8c5972c77faf3e29130";
    private BMapManager mMapManager = null;
    private MapView mMapView = null;
    private MKSearch mMKSearch = null;
    private MapController mMapController;
//    private PoiOverlay<OverlayItem> itemItemizedOverlay;
    private Context mContext;
//    private View mapPopWindow;
    private Button walk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.poi_details);

        mContext = PoiDetailsActivity.this.getApplicationContext();

        mMapManager = new BMapManager(getApplicationContext());
        mMapManager.init(strKey, new MKGeneralListener() {

            @Override
            public void onGetNetworkState(int errorCode) {
                if (errorCode == MKEvent.ERROR_NETWORK_CONNECT) {
                    Toast.makeText(mContext, "您的网络出错啦！", Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onGetPermissionState(int errorCode) {
                if (errorCode == MKEvent.ERROR_PERMISSION_DENIED) {
                    // 授权Key错误：
                    Toast.makeText(mContext,
                            "请在 DemoApplication.java文件输入正确的授权Key！",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        setContentView(R.layout.poi_details);

        mMapView = (MapView) this.findViewById(R.id.bmapView);
        mMapView.setBuiltInZoomControls(false);
        mMapView.setTraffic(false);
//        mMapView.setSatellite(true);
         mMapController = mMapView.getController();
//         double lng = 34.25934463685013;
//         double lat = 108.94721031188965
          GeoPoint geoPoint = new GeoPoint((int) (34.25934463685013 * 1E6),
                (int) (108.94721031188965 * 1E6));

        mMapController.setCenter(geoPoint);
        mMapController.setZoom(14);
//        Drawable marker = getResources().getDrawable(R.drawable.u73_normal);
//        itemItemizedOverlay = new PoiOverlay<OverlayItem>(marker, mapView);
//        walk = (Button) findViewById(R.id.walkBut);
//        walk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mMapController.setCenter(geoPoint);
//            }
//        });


        MKPlanNode start = new MKPlanNode();
        start.pt = new GeoPoint ((int) (34.25934463685013 * 1E6),(int) (108.94721031188965 * 1E6));
//        Drawable mark= getResources().getDrawable(R.drawable.u71_normal);
//        itemItemizedOverlay = new PoiOverlay<OverlayItem>(mark, mMapView);
//        mMapView.getOverlays().add(itemItemizedOverlay);
        MKPlanNode end = new MKPlanNode();
        end.pt = new GeoPoint((int) (34.260319 * 1E6), (int) (108.949075 * 1E6));

        mMKSearch = new MKSearch();
        mMKSearch.init(mMapManager, new MKSearchListener() {

            @Override
            public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onGetDrivingRouteResult(MKDrivingRouteResult result,int arg1) {
                if (result == null)
                    return;
                RouteOverlay routeOverlay = new RouteOverlay(
                        PoiDetailsActivity.this, mMapView);
                routeOverlay.setData(result.getPlan(0).getRoute(0));
                mMapView.getOverlays().add(routeOverlay);
                mMapView.refresh();// 刷新地图

            }

            @Override
            public void onGetPoiDetailSearchResult(int arg0, int arg1) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onGetShareUrlResult(MKShareUrlResult mkShareUrlResult, int i, int i2) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
            @Override
            public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onGetTransitRouteResult(MKTransitRouteResult result,
                                                int arg1) {
                RouteOverlay routeOverlay = new RouteOverlay(
                        PoiDetailsActivity.this, mMapView);
                routeOverlay.setData(result.getPlan(0).getRoute(0));
                mMapView.getOverlays().add(routeOverlay);
                mMapView.refresh();// 刷新地图

            }

            @Override
            public void onGetWalkingRouteResult(MKWalkingRouteResult result,
                                                int arg1) {
                // TODO Auto-generated method stub
                RouteOverlay routeOverlay = new RouteOverlay(
                        PoiDetailsActivity.this, mMapView);
                routeOverlay.setData(result.getPlan(0).getRoute(0));
                mMapView.getOverlays().add(routeOverlay);
                mMapView.refresh();// 刷新地图
            }

        });

        // 设置驾车路线搜索策略，时间优先、费用最少或距离最短

		/*
		 * // 驾乘检索策略常量：时间优先
		 * mMKSearch.setDrivingPolicy(MKSearch.ECAR_TIME_FIRST);
		 * mMKSearch.drivingSearch(null, start, null, end);
		 *
		 * // 驾乘检索策略常量：较少费用 mMKSearch.setDrivingPolicy(MKSearch.ECAR_FEE_FIRST);
		 * mMKSearch.drivingSearch(null, start, null, end);
		 *
		 * // 驾乘检索策略常量：最短距离 mMKSearch.setDrivingPolicy(MKSearch.ECAR_DIS_FIRST);
		 * mMKSearch.drivingSearch(null, start, null, end);
		 */

        // 步行线路搜索
//            mMKSearch.walkingSearch(null, start, null, end);

        // 公交线路搜索
//           mMKSearch.transitSearch("钟楼", start, end);
        mMKSearch.setDrivingPolicy(MKSearch.ECAR_TIME_FIRST);
        mMKSearch.drivingSearch(null, start, null, end);
    }
//    class PoiOverlay<OverlayItem> extends ItemizedOverlay {
//
//        public PoiOverlay(Drawable drawable, MapView mapView) {
//            super(drawable, mapView);
//        }
//
//        @Override
//        protected boolean onTap(int i) {
//            Log.d("BaiduMapDemo", "onTap " + i);
//            com.baidu.mapapi.map.OverlayItem item = itemItemizedOverlay.getItem(i);
//            GeoPoint point = item.getPoint();
//            String title = item.getTitle();
//
//            TextView titleTextView = (TextView) mapPopWindow.findViewById(R.id.titleTextView);
//            titleTextView.setText(title);
//
////            MapView.LayoutParams layoutParam = new MapView.LayoutParams(
////                    //控件宽,继承自ViewGroup.LayoutParams
////                    MapView.LayoutParams.WRAP_CONTENT,
////                    //控件高,继承自ViewGroup.LayoutParams
////                    MapView.LayoutParams.WRAP_CONTENT,
////                    //使控件固定在某个地理位置
////                    point,
////                    10,
////                    -30,
////                    //控件对齐方式
////            MapView.LayoutParams.BOTTOM_CENTER);
//            mapPopWindow.setVisibility(View.VISIBLE);
////            mapPopWindow.setLayoutParams(layoutParam);
//            mMapController.animateTo(point);
//            return super.onTap(i);
//        }
//
//        @Override
//        public boolean onTap(GeoPoint geoPoint, MapView mapView) {
//            Log.d("BaiduMapDemo", "onTap geoPoint " + geoPoint);
//
//            mapPopWindow.setVisibility(View.GONE);
//
//            return super.onTap(geoPoint, mapView);    //To change body of overridden methods use File | Settings | File Templates.
//        }
//    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMapView.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        mMapView.onResume();

        if (mMapManager != null) {
            mMapManager.start();
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        if (mMapManager != null) {
            mMapManager.stop();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.destroy();
        if (mMapManager != null) {
            mMapManager.destroy();
            mMapManager = null;
        }
        super.onDestroy();
    }
}
