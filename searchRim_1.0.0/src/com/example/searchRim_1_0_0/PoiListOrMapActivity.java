package com.example.searchRim_1_0_0;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.example.searchRim_1_0_0.SourcesUtill.DemoApplication;
import com.example.searchRim_1_0_0.SourcesUtill.UrlRequestData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PoiListOrMapActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private  Button returnButton;    //后退按钮
    private LocationClient locationClient;//定位客户端
    public   String lng=""; //定位的经度
    public  String lat="";//定位纬度
    private TextView poiListTitle;  //titleBar 标题
    private LinearLayout  ScopeListKmM;//选择范围
    private int   isListOrMap=0;//一个点按钮是否是list 还是地图
    private TextView  textData;//范围的数据
    private String scopeVar="";//要参到Url的范围
    private Button mapButton ;     //暂无用
    private String SearCondition;
    public  String   messageToPoiListOrMapActivity;//
    private ProgressDialog progressDialog;//进度条
    private final String addressToGeo ="https://api.weibo.com/2/location/pois/search/by_geo.json";
    private final String appKey ="1814457539";
    private final String accessToken="2.00hwncDCLSRnyB9b0e79fde3fs3JXC";
    private int page=1;//页数  返回结果的页码，默认为1，最大为40。
    private ListView  searchRimListView;
    private LinearLayout moreLoad;
    private BMapManager bMapManager;
    private final String strKey="6d3873c2bbba6718c341512344db11fc";
    private MapView  mapView;//地图控件
    private MapController  mapController;//地图控制者
    private PoiOverlay<OverlayItem> itemItemizedOverlay;  //POI信息点覆盖物的集合
    private LayoutInflater   layoutInflater;
    private View mapPopWindow;
    private String title;  //从Json 得到的地名 title
    private static final int SUCCESS = 0;
    private static final int ERROR_SERVER = 1;
    private static final int ERROR_DATA_FORMAT = 2;
    private List<Map<String,Object>> MessageListMap = new ArrayList<Map<String, Object>>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //初始化BaiduMapManager
        bMapManager = new BMapManager(this);
        boolean initResult = bMapManager.init(strKey, new MKGeneralListener() {
            @Override
            public void onGetNetworkState(int iError) {
                if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                    Toast.makeText(PoiListOrMapActivity.this, "您的网络出错啦！", Toast.LENGTH_LONG).show();
                } else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                    Toast.makeText(PoiListOrMapActivity.this, "输入正确的检索条件！", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onGetPermissionState(int iError) {
                if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
                    Toast.makeText(PoiListOrMapActivity.this, "请在 DemoApplication.java文件输入正确的授权Key！", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (!initResult) {
            Toast.makeText(PoiListOrMapActivity.this, "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }
        layoutInflater = LayoutInflater.from(this);
        setContentView(R.layout.poi_listing);
























        //从HomePageActivity 或  SecondLevelActivity  或  ThreeLevelActivity  参来的 经纬度和 title 标题
        Bundle bundle=getIntent().getExtras();
       messageToPoiListOrMapActivity=bundle.getString("messageToPoiListOrMapActivity");  //title标题
        lat = bundle.getString("lat");//经度
        lng =bundle.getString("long");//纬度
        //定位


        //后退按钮
       returnButton = (Button) findViewById(R.id.returnButton);
       returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //标题文本
        poiListTitle = (TextView) findViewById(R.id.poiListTitle);
        poiListTitle.setText(messageToPoiListOrMapActivity);
        //refreshButton 刷新按钮
          Button  refreshButton = (Button) findViewById(R.id.refreshButton);
          refreshButton.setOnClickListener(new Button.OnClickListener() {
              @Override
              public void onClick(View v) {
                  MessageListMap messageListMap = new MessageListMap() ;
                  messageListMap.execute(SearCondition);
              }
          });
        //列表和地图切换按钮
      final Button mapOrListButton  = (Button) findViewById(R.id.mapOrListButton);
        mapOrListButton.setOnClickListener(new View.OnClickListener() {

            LinearLayout listFramelayout = (LinearLayout) findViewById(R.id.listFramelayout);
            LinearLayout listMapPoiLayout = (LinearLayout) findViewById(R.id.listMapPoiLayout);
            List<LinearLayout> layoutlist= new ArrayList<LinearLayout>();;

            @Override
            public void onClick(View v) {
                layoutlist= new ArrayList<LinearLayout>();
                layoutlist.add(listFramelayout);
                layoutlist.add(listMapPoiLayout);
                for(LinearLayout lay:layoutlist){
                           lay.setVisibility(View.GONE); //每点一次让两兄弟不可见
                }
                mapOrListButton.setBackgroundResource(R.drawable.u65_normal);
                if(isListOrMap==1){
                    mapOrListButton.setBackgroundResource(R.drawable.u65_normal); //列表图标
                    listFramelayout.setVisibility(View.VISIBLE);
                    isListOrMap=0;
                }else{
                    listMapPoiLayout.setVisibility(View.VISIBLE);
                    mapOrListButton.setBackgroundResource(R.drawable.u18_normal);  //地图图标
                    isListOrMap=1;
                }
            }
        });

        // Map 信息点集合的 设置中心点
        Button   locButton = (Button) findViewById(R.id.locButton);
        locButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeoPoint geoPoint = new GeoPoint((int) (Double.valueOf(lat) * 1E6), (int) (Double.valueOf(lng) * 1E6));
                mapController.setCenter(geoPoint);
            }
        });
        //选择范围按钮
        ScopeListKmM = (LinearLayout) findViewById(R.id.ScopeListKmM);
        ScopeListKmM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogOnClick(v);
            }
        });
        // 跳到次界面直接加载listView的数据添加到界面
        SearCondition=messageToPoiListOrMapActivity;
        MessageListMap= new ArrayList<Map<String, Object>>();

        //进度条开始
        progressDialog = new ProgressDialog(PoiListOrMapActivity.this);
        progressDialog.setMessage("加载中...");
        //progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        //一进来就定位

        //初始化list 列表数据
        MessageListMap messageListMap = new MessageListMap() ;
        messageListMap.execute(SearCondition);
        //LIst 信息列表 点击每一项item 触发事件
        searchRimListView = (ListView) findViewById(R.id.searchRimListView);
        searchRimListView.setOnItemClickListener(new onItemClickListenerImpl());

       //加载更多
        LinearLayout moreLoad = (LinearLayout) findViewById(R.id.moreLoad);
        moreLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               page =  page+2;
                SearCondition =messageToPoiListOrMapActivity;


                MessageListMap messageListMap = new MessageListMap() ;
                messageListMap.execute(SearCondition);

            }
        });
    }
    //弹出范围选择AlertDialog
    public void AlertDialogOnClick(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("请输入范围：").setIcon(getResources().getDrawable(R.drawable.ic_launcher));

        final String[] date = new String[]{"1000米内", "2000米内", "3000米内", "4000米内", "5000米内","6000米内","10000米内"};

        builder.setItems(date, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                textData = (TextView) findViewById(R.id.textData);
                scopeVar = date[which].replace("米内","");
                textData.setText("范围:"+date[which]);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private class onItemClickListenerImpl implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             Toast.makeText(PoiListOrMapActivity.this,""+MessageListMap.get(position).get("name"),1000).show();

        }
    }
    private class MessageListMap extends AsyncTask<String,String,Integer> {

        @Override
        protected void onPreExecute() {

                progressDialog.show();


        }

        @Override

        protected void onPostExecute(Integer s) {

                progressDialog.dismiss();
                if (s == SUCCESS) {

                } else if (s == ERROR_SERVER) {
                    showServerErrorMessage();
                } else if (s == ERROR_DATA_FORMAT) {
                    showDataErrorMessage();
                }


        }

        @Override
        protected Integer doInBackground(String... params) {
                    scopeVar ="3000";
                    int count =20;
                    int sr=1;
                    String q="";  //查询的关键词，必须进行URLencode。
                   String searchType="";  //searchtype 查询类型:POI分类类型，需要进行urlencode编码，如：住宅小区。
                    try {
                        q = java.net.URLEncoder.encode(messageToPoiListOrMapActivity,"UTF-8");
                        searchType =java.net.URLEncoder.encode(messageToPoiListOrMapActivity,"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    String  Url=addressToGeo+"?coordinate="+lng+","+lat+"&source="+appKey
                            +"&access_token="+accessToken+"&range="+scopeVar+"&count="
                            +count+"&page="+page+"&sr="+sr+"&q="+q;

                    Url.replaceAll(" ", "%20");


                    try {
                      String responseStr=   UrlRequestData.requestServerData(Url);
                      JSONObject jsonObject = new JSONObject(responseStr);

                        /*JSONObject  latlong = jsonObject.optJSONObject("geo");    //自己的经纬度

                             Map<String,Object> latMapt= new HashMap<String, Object>();
                             latMapt.put("lon",latlong.optString("lon"));
                             latMapt.put("lat",latlong.optString("lat"));*/
                   //往MessageListMap 添加数据
                        JSONArray jsonArray = jsonObject.getJSONArray("poilist");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1= (JSONObject) jsonArray.get(i);

                            title = jsonObject1.optString("name");
                            String address =jsonObject1.optString("address");
                            String distance= jsonObject1.optString("distance");
                            String poiLng = jsonObject1.optString("y");
                            String poiLat = jsonObject1.getString("x");
                            String tel = jsonObject1.getString("tel");

                            if(address.equals("null")||address.trim().equals("")){
                                address="暂无地址信息。。。。";
                            }
                            if(distance.equals("null")){
                                distance="暂无距离信息";
                            }else {
                                // 455米
                                if(!(Integer.valueOf(distance)/100<=9)){
                                    Float distanceChild = Float.valueOf(distance)/1000;
                                    distance =String.valueOf(distanceChild)+"km";
                                }else {
                                    distance = distance+"m";
                                }

                            }
                             //判断分类是否和前一页面一样再把数据放到MessageListMap  （ArrayList）
                            //messageToPoiListOrMapActivity.equals(categoryName)
                            if(true){
                                Map<String,Object> messageMap = new HashMap<String, Object>();
                                messageMap.put("name",(MessageListMap.size()+1)+"--"+title) ;
                                messageMap.put("address",address);
                                messageMap.put("space",distance) ;
                                messageMap.put("lon",poiLng);
                                messageMap.put("lat",poiLat);
                                messageMap.put("tel",tel);
                                MessageListMap.add(messageMap);
                            }

                        }



                    } catch (IOException e) {
                        return ERROR_SERVER;
                    } catch (JSONException e) {
                        return ERROR_DATA_FORMAT;
                    }




            this.publishProgress("ok");
            return SUCCESS;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            MySimpleAdapter mySimpleAdapter = new MySimpleAdapter(PoiListOrMapActivity.this,MessageListMap,R.layout.seek_item,
                    new String[]{"name","address","space"},new int[]{R.id.placeName,R.id.address,R.id.space});

            if(values[0].equals("ok")){

              searchRimListView.setAdapter(mySimpleAdapter);
               if(MessageListMap.size()!=0){
                   //加载更多更新数据完毕后显示 加载更多布局
                   moreLoad = (LinearLayout) findViewById(R.id.moreLoad);
                   moreLoad.setVisibility(View.VISIBLE);
               }
                //当list的数据加载完成后。再把地图的POi信息点放到地图上；
 /*>>>>>>>>>>>>>>>>>>>>>>>>>*/
                mapView = (MapView) findViewById(R.id.bmapView);

                mapView.setBuiltInZoomControls(false);
                //卫星图层
                mapView.setSatellite(false);
                //交通图层
                mapView.setTraffic(true);
                mapController = mapView.getController();
                //控制缩放等级
                mapController.setZoom(14);
                //移动到经纬度点
                final GeoPoint geoPoint = new GeoPoint((int) (Double.valueOf(lat) * 1E6), (int) (Double.valueOf(lng) * 1E6));
                //设置中心点
                mapController.setCenter(geoPoint);



                //一秒后定位到我的位置，并显示POI
               /* Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mapController.animateTo(geoPoint);

                    }
                }, 1000);*/

                Drawable marker = getResources().getDrawable(R.drawable.u73_normal);
                itemItemizedOverlay = new PoiOverlay<OverlayItem>(marker,mapView);


    //自己的位置坐标
               /* GeoPoint point = new GeoPoint(geoPoint.getLatitudeE6(), geoPoint.getLongitudeE6());
                OverlayItem overlayItem = new OverlayItem(point,"当前位置",null);
                overlayItem.setMarker(getResources().getDrawable(R.drawable.u71_normal));
                itemItemizedOverlay.addItem(overlayItem);*/
                MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mapView);
                LocationData locData = new LocationData();
                myLocationOverlay.setMarker(getResources().getDrawable(R.drawable.u71_normal));
              //手动将位置源置为天安门，在实际应用中，请使用百度定位SDK获取位置信息，要在SDK中显示一个位置，需要使用百度经纬度坐标（bd09ll）
                locData.latitude =Double.valueOf(lat);
                locData.longitude = Double.valueOf(lng);
                locData.direction = 2.0f;
                myLocationOverlay.setData(locData);
                mapView.getOverlays().add(myLocationOverlay);
                mapView.refresh();
                mapView.getController().animateTo(geoPoint);


    //信息点的位置坐标
                /* for (int i = 0; i < MessageListMap.size(); i++) {
                    String  latPoi =(String) MessageListMap.get(i).get("lat");
                    String  lngPoi =(String) MessageListMap.get(i).get("lon");
                    String  adressName =  (String)MessageListMap.get(i).get("name");
                     String  adress =  (String)MessageListMap.get(i).get("address");

                     GeoPoint point = new GeoPoint((int)(Double.parseDouble(latPoi) * 1E6), (int)(Double.parseDouble(lngPoi) * 1E6));
//                   GeoPoint geoPoint = new GeoPoint((int) (34.25934463685013 * 1E6), (int) (108.94721031188965 * 1E6));

                     OverlayItem overlayItem = new OverlayItem(point,adressName,adress);

                    itemItemizedOverlay.addItem(overlayItem);
                }*/

                for (int i = 0; i < MessageListMap.size(); i++) {
                    GeoPoint point = new GeoPoint(geoPoint.getLatitudeE6() + i * 10000, geoPoint.getLongitudeE6() + i * 10000);
                    OverlayItem overlayItem = new OverlayItem(point, "我是标题 " + i, "我是内容 " + i);
                    itemItemizedOverlay.addItem(overlayItem);
                }


                mapView.getOverlays().add(itemItemizedOverlay);

                //添加弹出窗口
                mapPopWindow = layoutInflater.inflate(R.layout.map_pop_window, null);
                mapPopWindow.setVisibility(View.GONE);
                mapView.addView(mapPopWindow);










            }

        }
    }

    private void showServerErrorMessage() {
        Toast.makeText(this, "请求数据失败",1000).show();
    }

    private void showDataErrorMessage() {
        Toast.makeText(this, "数据格式错误",1000).show();
    }
    public class MySimpleAdapter extends SimpleAdapter {
        private MySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }


        @Override
        public View getView( final int position, View convertView, ViewGroup parent) {


            return super.getView(position,convertView,parent);
            //To change body of overridden methods use File | Settings | File Templates.


        }
    }
    private void getMyLocation() {
        Log.d("BaiduMapDemo", "getMyLocation");
        locationClient= new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setAddrType("all");
        option.setOpenGps(true);
        option.setPriority(LocationClientOption.NetWorkFirst);
        option.setScanSpan(5000);




        locationClient.setLocOption(option);

        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null)
                    return ;
                Log.d("BaiduMapDemo", "onReceiveLocation address " + bdLocation.getAddrStr());

                Log.d("BaiduMapDemo", "onReceiveLocation Latitude " + bdLocation.getLatitude());
                Log.d("BaiduMapDemo", "onReceiveLocation Longitude " + bdLocation.getLongitude());
                lng =String.valueOf(bdLocation.getLongitude());
                lat =String.valueOf(bdLocation.getLatitude()) ;
                DemoApplication.locData.latitude = bdLocation.getLatitude();
                DemoApplication.locData.longitude = bdLocation.getLongitude();

            }


            public void onReceivePoi(BDLocation bdLocation) {
                Log.d("BaiduMapDemo", "onReceivePoi ");
            }
        });
    }
    public void locationOnclick()
    {
        locationClient.start();

        Toast.makeText(PoiListOrMapActivity.this, "正在定位……", Toast.LENGTH_SHORT).show();
        locationClient.requestLocation();
    }


    //地图的

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
            String content = item.getSnippet();

           final TextView titleTextView = (TextView) mapPopWindow.findViewById(R.id.titleTextView);
            titleTextView.setText(title);

            //点每个信息点跳转到详情 步行 驾车 界面
        LinearLayout  paopaoInfo = (LinearLayout) mapPopWindow.findViewById(R.id.paopaoInfo);
            paopaoInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toPOIDetailsActivity();
                    //测试泡泡可以点击
//                    mapPopWindow.setVisibility(View.GONE);
//                    Intent  intent  = new Intent();
//                    Toast.makeText(PoiListOrMapActivity.this,titleTextView.getText(),Toast.LENGTH_SHORT);

                }
            });

            MapView.LayoutParams layoutParam = new MapView.LayoutParams(
                    //控件宽,继承自ViewGroup.LayoutParams
                    MapView.LayoutParams.WRAP_CONTENT,
                    //控件高,继承自ViewGroup.LayoutParams
                    MapView.LayoutParams.WRAP_CONTENT,
                    //使控件固定在某个地理位置
                    point,
                    0,
                    -40,
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

            return super.onTap(geoPoint, mapView);
        }

    }

    public void toPOIDetailsActivity(){
        Intent intent  = new Intent (PoiListOrMapActivity.this,PoiDetailsActivity.class);
        startActivity(intent);
    }
}
