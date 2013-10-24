package com.example.searchRim_1_0_0;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.searchRim_1_0_0.SourcesUtill.DemoApplication;

import java.util.*;

public class HomepageActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private ListView  searchRimListView;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListenner();
    private LocationClient locationClient;
    public  static String lng; //定位的经度      //其他activity可以调用 lng lat
    public  static String lat;//定位纬度
    private TextView  locationMessage;//显示定位后的地址
    private Button   onlocationBut;//定位按钮
    private List<Map<String,Object>> MessageListMap = new ArrayList<Map<String, Object>>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.homepage);

        //关于
       Button   icActionSetting = (Button) findViewById(R.id.icActionSetting);
       icActionSetting.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent  intent = new Intent(HomepageActivity.this,AboutActivity.class);
               startActivity(intent);
           }
       });

        //搜索
        Button  absIcSearch = (Button) findViewById(R.id.absIcSearch);
        absIcSearch.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lat!=null&&lng!=null){
                    Intent intent = new Intent(HomepageActivity.this,SeekActivity.class);
                    Bundle bundle =new Bundle();
                    bundle.putString("lat",String.valueOf(lat));
                    bundle.putString("lng",String.valueOf(lng));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else {
                    getMyLocation();
                    locationOnclick(locationMessage);
                }

            }
        });
        //定位
        locationMessage = (TextView) findViewById(R.id.locationMessage);
        onlocationBut = (Button) findViewById(R.id.onlocationBut);
        //一进来就定位
//        MyLocationAsyncTask  myLocationAsyncTask= new MyLocationAsyncTask();
//        myLocationAsyncTask.execute("");
        getMyLocation();
        locationOnclick(locationMessage);
          //当按定位按钮时重新定位
        onlocationBut.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  MyLocationAsyncTask  myLocationAsyncTask= new MyLocationAsyncTask();
                myLocationAsyncTask.execute("");*/
                getMyLocation();
                locationOnclick(locationMessage);
            }
        });

        final   TextView locationMessage = (TextView) findViewById(R.id.locationMessage);
        Button  onlocationBut = (Button) findViewById(R.id.onlocationBut);
        onlocationBut.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                   locationMessage.setText("");
            }
        });

        searchRimListView = (ListView) findViewById(R.id.searchRimListView);
        searchRimListView.setOnItemClickListener(new onItemClickListenerImpl());
        MessageListMap messageListMap = new MessageListMap() ;
        messageListMap.execute("暂无数据");


    }

   public void show(String address){
       TextView locationMessage = (TextView) findViewById(R.id.locationMessage);
       locationMessage.setText(""+address);
   }
    //定位
    public class MyLocationListenner implements BDLocationListener {
       @Override
       public void onReceiveLocation(BDLocation location) {
           if (location == null)
               return ;
            String address = location.getAddrStr();
           show(address);
       }
       public void onReceivePoi(BDLocation poiLocation) {

       }
   }
    //点击Item 每一项的 点击事件
    private class onItemClickListenerImpl implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           //此处的的 lat  Long 经纬度 是  定位后的 。目前 写死一个经纬度
//            String lat = "34.265860873430";
//            String Long ="108.954454572460";

            //点每项都先定位
            if(lat!=null||lng!=null){
                Intent intent = new Intent(HomepageActivity.this,PoiListOrMapActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("messageToPoiListOrMapActivity",(String)MessageListMap.get(position).get("message"));//参的是点的Item 的标题
                bundle.putString("lat",String.valueOf(lat));
                bundle.putString("long",String.valueOf(lng));
                intent.putExtras(bundle);
                startActivity(intent);
            }else {
                getMyLocation();
                locationOnclick(locationMessage);
            }



//             Toast.makeText(HomepageActivity.this,""+MessageListMap.get(position).get("message"),1000).show();

        }
    }
    //list数据
    private class MessageListMap extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {
            String messageArray[]  = new String[]{"餐饮服务","购物服务","生活服务","体育休闲服务","医疗保健服务","住宿服务","科教文化服务","交通设施服务","公共设施"};

            for(String message:messageArray){
                Map<String,Object> messageMap = new HashMap<String, Object>();

                messageMap.put("message",message) ;
                MessageListMap.add(messageMap);

            }
            this.publishProgress("ok");
            return "数据更新完毕";  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        protected void onProgressUpdate(String... values) {
            MySimpleAdapter mySimpleAdapter = new MySimpleAdapter(HomepageActivity.this,MessageListMap,R.layout.char_item,
                    new String[]{"message"},new int[]{R.id.listMessage});

            if(values[0].equals("ok")){

                 searchRimListView.setAdapter(mySimpleAdapter);


            }

        }
    }
    //将list数据添加到listView
    public class MySimpleAdapter extends SimpleAdapter {
        private MySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }


        @Override
        public View getView( final int position, View convertView, ViewGroup parent) {
            View  v = super.getView(position,convertView,parent);
            Button but = (Button)v.findViewById(R.id.ListBut);
            but.setVisibility(View.VISIBLE);
            but.setTag(position);
            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  String message =   MessageListMap.get(position).get("message").toString();
                  String ServerName="";
                   //"餐饮服务","购物服务","生活服务","体育休闲服务",
                   // "医疗保健服务","住宿服务","科教文化服务","交通设施服务","公共设施"
                    if(message.equals("餐饮服务")){
                        ServerName= "餐饮服务";
                    }
                    if(message.equals("购物服务")){
                        ServerName= "购物服务";
                    }
                    if(message.equals("生活服务")){
                        ServerName= "生活服务";
                    }
                    if(message.equals("体育休闲服务")){
                        ServerName= "体育休闲服务";
                    }
                    if(message.equals("医疗保健服务")){
                        ServerName= "医疗保健服务";
                    }
                    if(message.equals("住宿服务")){
                        ServerName= "住宿服务";
                    }
                    if(message.equals("科教文化服务")){
                        ServerName= "科教文化服务";
                    }
                    if(message.equals("交通设施服务")){
                        ServerName= "交通设施服务";
                    }
                    if(message.equals("公共设施")){
                        ServerName= "公共设施";
                    }
                     if(message.equals(ServerName)&&lat!=null&&lng!=null){
                         Intent it = new Intent(HomepageActivity.this,SecondLevelActivity.class);
                         Bundle bundle=new Bundle();
                         bundle.putString("name", ServerName);
                         bundle.putString("lat",lat);
                         bundle.putString("lng",lng);
                         it.putExtras(bundle);       // it.putExtra(“test”, "shuju”);
                         startActivity(it);            // startActivityForResult(it,REQUEST_CODE);
                     }else{
                         getMyLocation();
                         locationOnclick(locationMessage);
                     }
//                    Toast.makeText(HomepageActivity.this, "点按钮了"+message, 1000).show();
                }
            });
            return v;
            //To change body of overridden methods use File | Settings | File Templates.


        }
    }
   //定位的异步处理
   class MyLocationAsyncTask extends AsyncTask<String,String,String>{
       @Override
       protected String doInBackground(String... params) {
           getMyLocation();
           return "ok";  //To change body of implemented methods use File | Settings | File Templates.
       }

       @Override
       protected void onPreExecute() {
           locationOnclick(locationMessage);
       }

       @Override
       protected void onPostExecute(String s) {
           super.onPostExecute(s);    //To change body of overridden methods use File | Settings | File Templates.
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
                lng = String.valueOf(bdLocation.getLongitude());
                lat = String.valueOf(bdLocation.getLatitude());
                DemoApplication.locData.latitude = bdLocation.getLatitude();
                DemoApplication.locData.longitude = bdLocation.getLongitude();
                if (lng.equals("0")||lat.equals("0"))
                {
                    locationMessage.setText("定位失败请重新定位");
                }else
                {
                    locationMessage.setText(bdLocation.getAddrStr());
                }
            }


            public void onReceivePoi(BDLocation bdLocation) {
                Log.d("BaiduMapDemo", "onReceivePoi ");
            }
        });
    }
    public void locationOnclick(TextView view)
    {
        locationClient.start();
        locationMessage.setText("定位中请稍等");
        Toast.makeText(HomepageActivity.this, "正在定位……", Toast.LENGTH_SHORT).show();
        locationClient.requestLocation();
    }
}
