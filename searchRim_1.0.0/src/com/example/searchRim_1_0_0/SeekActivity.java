package com.example.searchRim_1_0_0;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.example.searchRim_1_0_0.SourcesUtill.UrlRequestData;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeekActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private  Button seekBack;
    private EditText editSearchText;
    private Button mapButton ;
    private String SearCondition;
    private ProgressDialog progressDialog;//进度条
    private final String addressToGeo ="https://api.weibo.com/2/location/pois/search/by_geo.json";
    private final String appKey ="1814457539";
    private final String accessToken="2.00hwncDCLSRnyB9b0e79fde3fs3JXC";
    private String lat = "34.265860873430";
    private  String lng ="108.954454572460";
    private int page=1;//页数
    private ListView  searchRimListView;
    private LinearLayout moreLoad;
    private static final int SUCCESS = 0;
    private static final int ERROR_SERVER = 1;
    private static final int ERROR_DATA_FORMAT = 2;
    private List<Map<String,Object>> MessageListMap = new ArrayList<Map<String, Object>>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.seek);

        //从HomePageActivity 参来的经纬度
        Bundle bundle=getIntent().getExtras();
         lat = bundle.getString("lat");
         lng = bundle.getString("lng");
        //后退按钮
        seekBack = (Button) findViewById(R.id.seekBack);
        seekBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Edit搜寻条件
        editSearchText = (EditText) findViewById(R.id.editSearchText);
        //搜索按钮
        mapButton = (Button) findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearCondition="";
                MessageListMap= new ArrayList<Map<String, Object>>();
                SearCondition =  editSearchText.getText().toString().trim();


                MessageListMap messageListMap = new MessageListMap() ;
                messageListMap.execute(SearCondition);
            }
        });
        //进度条开始
        progressDialog = new ProgressDialog(SeekActivity.this);
        progressDialog.setMessage("加载中...");
        //progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
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
               page =  page+1;
                SearCondition="";

                SearCondition =  editSearchText.getText().toString().trim();


                MessageListMap messageListMap = new MessageListMap() ;
                messageListMap.execute(SearCondition);

            }
        });
    }
    private class onItemClickListenerImpl implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             Toast.makeText(SeekActivity.this,""+MessageListMap.get(position).get("placeName"),1000).show();

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

         String SearCon =params[0];//搜索条件Text
         if(SearCon!=null){
                if(!SearCon.equals("")){
                    String  range ="3000";
                    int count =20;
                    int sr=1;
                    String q="";
                    try {
                        q = java.net.URLEncoder.encode(SearCon,"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    String  Url=addressToGeo+"?coordinate="+lng+","+lat+"&source="+appKey
                            +"&access_token="+accessToken+"&range="+range+"&count="
                            +count+"&page="+page+"&sr="+sr+"&q="+q;


                    //URLEncode.encode("string","utf8")
                    Url.replaceAll(" ", "%20");


                    try {
                      String responseStr=   UrlRequestData.requestServerData(Url);
                      JSONObject jsonObject = new JSONObject(responseStr);

                   //往MessageListMap 添加数据
                        JSONArray jsonArray = jsonObject.getJSONArray("poilist");
                        int len = jsonArray.length();
                        for (int i = 0; i < len; i++) {
                            JSONObject jsonObject1= (JSONObject) jsonArray.get(i);

                            String title = jsonObject1.optString("name");
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
                                //扩展 455米
                                if(!(Integer.valueOf(distance)/100<=9)){
                                    Float distanceChild = Float.valueOf(distance)/1000;
                                    distance =String.valueOf(distanceChild)+"km";
                                }else {
                                    distance = distance+"m";
                                }

                            }

                            Map<String,Object> messageMap = new HashMap<String, Object>();
                            messageMap.put("name",(MessageListMap.size()+1)+"--"+title) ;
                            messageMap.put("address",address);
                            messageMap.put("space",distance) ;
                            messageMap.put("lon",poiLng);
                            messageMap.put("lat",poiLat);
                            messageMap.put("tel",tel);
                            MessageListMap.add(messageMap);
                        }



                    } catch (IOException e) {
                        return ERROR_SERVER;
                    } catch (JSONException e) {
                        return ERROR_DATA_FORMAT;
                    }

                }

            }


            this.publishProgress("ok");
            return SUCCESS;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            MySimpleAdapter mySimpleAdapter = new MySimpleAdapter(SeekActivity.this,MessageListMap,R.layout.seek_item,
                    new String[]{"name","address","space"},new int[]{R.id.placeName,R.id.address,R.id.space});

            if(values[0].equals("ok")){

                 searchRimListView.setAdapter(mySimpleAdapter);
               if(MessageListMap.size()!=0){
                   //加载更多更新数据完毕后显示 加载更多布局
                   moreLoad = (LinearLayout) findViewById(R.id.moreLoad);
                   moreLoad.setVisibility(View.VISIBLE);
               }

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
}
