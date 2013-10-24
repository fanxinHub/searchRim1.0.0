package com.example.searchRim_1_0_0;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.example.searchRim_1_0_0.SourcesUtill.GetData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecondLevelActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private ListView  searchRimListView;
    private String name;
    private double lat=0;
    private double lng =0;
    private Button foodServiceBack;
    String messageArray[]=new String[]{};
    private List<Map<String,Object>> MessageListMap = new ArrayList<Map<String, Object>>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.second_level);

        Bundle bundle=getIntent().getExtras();
        name=bundle.getString("name");  //判断是哪个服务
        lat = Double.valueOf(bundle.getString("lat"));
        lng = Double.valueOf(bundle.getString("lng"));



        searchRimListView = (ListView) findViewById(R.id.searchRimListView);
        searchRimListView.setOnItemClickListener(new onItemClickListenerImpl());
        MessageListMap messageListMap = new MessageListMap() ;
        messageListMap.execute("暂无数据");

        //后退按钮
        foodServiceBack = (Button) findViewById(R.id.foodServiceBack);
        foodServiceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private class onItemClickListenerImpl implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//             Toast.makeText(SecondLevelActivity.this,""+MessageListMap.get(position).get("message"),1000).show();


            Intent intent = new Intent(SecondLevelActivity.this,PoiListOrMapActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("messageToPoiListOrMapActivity",(String)MessageListMap.get(position).get("message"));//参的是点的Item 的标题
            bundle.putString("lat",String.valueOf(lat));
            bundle.putString("long",String.valueOf(lng));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
    private class MessageListMap extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {

                    TextView  title = (TextView)findViewById(R.id.serviceText);
                    title.setText(name);  //根据点到的来设置title
           //调用GetData的获取二级界面数据
            messageArray = GetData.getSecondStringArray(name);


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
            MySimpleAdapter mySimpleAdapter = new MySimpleAdapter(SecondLevelActivity.this,MessageListMap,R.layout.char_item,
                    new String[]{"message"},new int[]{R.id.listMessage});

            if(values[0].equals("ok")){

                 searchRimListView.setAdapter(mySimpleAdapter);


            }

        }
    }


    public class MySimpleAdapter extends SimpleAdapter {
        private MySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }


        @Override
        public View getView( final int position,View convertView, ViewGroup parent) {
            View  v = super.getView(position,convertView,parent)  ;
            Button but = (Button)v.findViewById(R.id.ListBut);
            //调用是否有三级目录
            if(name.equals("餐饮服务")){
                if(position==0||position==1 ||position==2 ||position==4){
                    but.setVisibility(View.VISIBLE);
                }
            }
            if(name.equals("购物服务")){
                if(position!=7){
                    but.setVisibility(View.VISIBLE);
                }
            }
            if(name.equals("生活服务")){
               but.setVisibility(View.VISIBLE);
            }
            if(name.equals("体育休闲服务")){

                    but.setVisibility(View.VISIBLE);

            }
            if(name.equals("医疗保健服务")){
                if(position==0||position==1||position==5||position==6){
                    but.setVisibility(View.VISIBLE);
                }
            }
            if(name.equals("住宿服务")){
                but.setVisibility(View.VISIBLE);
            }
            if(name.equals("科教文化服务")){
                if(position==0||position==10||position==11){
                    but.setVisibility(View.VISIBLE);
                }
            }
            if(name.equals("交通设施服务")){
                if(position==2||position==6||position==8){
                    but.setVisibility(View.VISIBLE);
                }
            }
            if(name.equals("住宿服务")){
//                but.setVisibility(View.VISIBLE);
            }
            but.setTag(position);
            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String threeName = MessageListMap.get(position).get("message").toString();

                          Intent it = new Intent(SecondLevelActivity.this,ThreeLevelActivity.class);
                          Bundle bundle=new Bundle();
                          bundle.putString("name",threeName);
                          bundle.putString("lat",String.valueOf(lat));
                          bundle.putString("lng",String.valueOf(lng));
                          it.putExtras(bundle);       // it.putExtra(“test”, "shuju”);
                          startActivity(it);            // startActivityForResult(it,REQUEST_CODE);

//                    Toast.makeText(SecondLevelActivity.this, "点按钮了"+MessageListMap.get(position).get("message"),1000).show();
                }
            });
            return v;
            //To change body of overridden methods use File | Settings | File Templates.


        }
    }
}
