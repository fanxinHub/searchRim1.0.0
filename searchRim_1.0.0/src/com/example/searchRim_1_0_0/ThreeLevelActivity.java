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

public class ThreeLevelActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private ListView  searchRimListView;
    private String name;
    private double lat=0;
    private double lng =0;
    private Button foodServiceBack;

    private List<Map<String,Object>> MessageListMap = new ArrayList<Map<String, Object>>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.three_level);

        Bundle bundle=getIntent().getExtras();
        name=bundle.getString("name");
        lat = Double.valueOf(bundle.getString("lat"));
        lng = Double.valueOf(bundle.getString("lng"));


        //判断是哪个服务

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
             Toast.makeText(ThreeLevelActivity.this,""+MessageListMap.get(position).get("message"),1000).show();

            Intent intent = new Intent(ThreeLevelActivity.this,PoiListOrMapActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("messageToPoiListOrMapActivity",(String)MessageListMap.get(position).get("message"));//参的是点的Item 的标题
            bundle.putString("lat",String.valueOf(lat));
            bundle.putString("long",String.valueOf(lng));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
    private class MessageListMap extends AsyncTask<String,String,String> {
        String messageArray[]=new String[]{};
        @Override
        protected String doInBackground(String... params) {

                    TextView  title = (TextView)findViewById(R.id.serviceText);
                    title.setText(name);  //根据点到的来设置title


            // 餐饮服务
            if(name.equals("中餐厅")){

                messageArray = new String[]{"综合酒楼","四川菜（川菜）"," 广东菜（粤菜）","江苏菜 ","浙江菜","上海菜"
                        ,"湖南菜（湘菜）","安徽菜（徽菜）","福建菜","北京菜","湖北菜（鄂菜）","东北菜","云贵菜"
                        ,"西北菜","老字号","火锅店","特色/地方风味餐厅","海鲜酒楼","中式素菜馆","清真菜馆 ","台湾菜","潮州菜"};

            }
            if(name.equals("外国餐厅")){
                messageArray = new String[]{"西餐厅（综合风味）","日本料理"," 法式菜品餐厅","意式菜品餐厅","泰国/越南菜品餐厅 ","地中海风格菜品"
                        ,"美食风味","印度风味","牛扒店（扒房）","俄国菜","葡国菜","德国菜","巴西菜"
                        ,"墨西哥菜","其它亚洲菜"};
            }
            if(name.equals("快餐厅")){
                messageArray  = new String[]{"肯德基","麦当劳","必胜客","永和豆浆","茶餐厅","大家乐"
                        ,"大快活","美心","吉野家","仙跡岩"};
            }
            if(name.equals("咖啡厅")){
                messageArray  = new String[]{"星巴克咖啡","上岛咖啡","Pacifi Ccoffee Company","巴黎咖啡店"};
            }
  // 购物服务
            if(name.equals("商场")){  // 购物中心  、 普通商场  、 免税品店
                messageArray  = new String[]{"购物中心","普通商场","免税品店"};
            }
            if(name.equals("便民商店/便利店")){
                messageArray  = new String[]{"7-ELEVEN便利店","OK便利店"};
            }
            if(name.equals("家电电子卖场")){
                messageArray = new String[]{"综合家电市场","国美","大中","苏宁","手机销售"
                        ,"数码电子","丰泽","镭射"};
            }
            if(name.equals("超级市场")){
                messageArray  = new String[]{"家乐福","沃尔玛","华润","北京华联","上海华联"
                        ,"麦德龙","万客隆","华堂","易初莲花","好又多","屈臣氏","乐购","惠康超市"
                        ,"百佳超市","万宁超市"};
            }
            if(name.equals("花鸟鱼虫市场")){
                messageArray  = new String[]{"花卉市场","鱼虫市场"};

            }
            if(name.equals("家居建材市场")){
                messageArray = new String[]{"家居建材综合市场","家具城","建材五金市场"
                        ,"厨卫市场","布艺市场","灯具瓷器市场"};
            }
            if(name.equals("综合市场")){
                messageArray= new String[]{"小商品市场","旧货市场","农副产品市场"
                        ,"果品市场","蔬菜市场","水产海鲜市场"};
            }
            if(name.equals("文化用品店")){
                 //wu
            }
            if(name.equals("体育用品店")){
                messageArray  = new String[]{"李宁专卖店","耐克专卖店","阿迪达斯专卖店"
                        ,"锐步专卖店","彪马专卖店","高尔夫用品店","户外用品"};
            }
            if(name.equals("特色商业街")){
                messageArray  = new String[]{"步行街"};
            }
            if(name.equals("服装鞋帽皮具店")){
                messageArray = new String[]{"品牌服装店","品牌鞋店","品牌皮具店"};
            }
            if(name.equals("专卖店")){
                messageArray  = new String[]{"书店","音像店","儿童用品店"
                        ,"自行车专卖店","礼品饰品店","烟酒专卖店","宠物用品店",
                        "摄影器材店","宝马生活方式"};
            }
            if(name.equals("特殊买卖场所")){
                messageArray  = new String[]{"拍卖行","典当行"};
            }
            if(name.equals("个人用品/化妆品店")){
                messageArray = new String[]{"莎莎"};
            }
   //生活服务
           /* "售票处","邮局",,"电讯营业厅","事务所""丧葬设施"*/
            if(name.equals("售票处")){
                messageArray = new String[]{"飞机票代售点","火车票代售点","长途汽车票代售点",
                        "船票代售点","公园景点售票处"};
            }
            if(name.equals("邮局")){
                messageArray  = new String[]{"邮政速递"};
            }
            if(name.equals("电讯营业厅")){
                messageArray = new String[]{"中国电信营业厅","中国网通营业厅","中国移动营业厅",
                        "中国联通营业厅","中国铁通营业厅","中国卫通营业厅","和记电讯","数码通电讯",
                        "电讯盈科","中国移动万众/Peoples"};
            }
            if(name.equals("事务所")){
                messageArray = new String[]{"律师事务所","会计师事务所","评估事务所","审计事务所","认证事务所" ,
                        "专利事务所"};
            }
            if(name.equals("丧葬设施")){
                messageArray = new String[]{"陵园","公墓","殡仪馆"};
            }
//体育休闲服务
            //"运动场所","高尔夫相关","娱乐场所","度假疗养场所","休闲场所","影剧院"
            if(name.equals("影剧院")){
                messageArray = new String[]{"电影院","音乐厅","剧场"};
            }
            if(name.equals("休闲场所")){
                messageArray = new String[]{"游乐场","垂钓园","采摘园","露营地","水上活动中心"};
            }
            if(name.equals("度假疗养场所")){
                messageArray = new String[]{"度假村","疗养院"};
            }
            if(name.equals("娱乐场所")){
                messageArray = new String[]{"夜总会","K T V","迪厅","酒吧","游戏厅","棋牌室","博彩中心","网吧"};
            }
            if(name.equals("高尔夫相关")){
                messageArray = new String[]{"高尔夫球场","高尔夫练习场"};
            }
            if(name.equals("运动场所")){
                messageArray = new String[]{"综合体育馆","保龄球馆","网球场","篮球场馆","足球场","滑雪场","溜冰场","户外健身场所","海滨浴场","游泳馆","健身中心","乒乓球馆","台球厅","壁球场","马术俱乐部","赛马场","橄榄球场","羽毛球场","跆拳道场馆"};
            }
 //医疗保健服务

            String [] me =new String[]{"综合医院","专科医院","医药保健相关","动物医疗场所"};
            String [][]  media =new String[][]{{"三级甲等医院","卫生院"},{"整形美容","口腔医院","眼科医院","耳鼻喉医院","胸科医院","骨科医院","肿瘤医院","脑科医院","妇科医院","精神病医院","传染病医院"},{"药房","医疗保健用品"},{"宠物诊所","兽医站"}};

            for(int i =0;i<me.length;i++){
                   if(name.equals(me[i])){
                       messageArray = media[i];
                   }
           }


   //住宿服务

            String []  putUp=new String[]{"宾馆酒店","旅馆招待所"};
            String [][]  putChild =new String[][]{{"六星级宾馆","五星级宾馆","四星级宾馆","三星级宾馆","经济型连锁酒店"},{"青年旅社"}};


            for(int i =0;i<putUp.length;i++){
                if(name.equals(putUp[i])){
                    messageArray = putChild[i];
                }
            }

 //科教文化服务

            String []  education=new String[]{"博物馆","传媒机构","学校"};
            String [][]  educationChild =new String[][]{{"奥迪博物馆","奥迪博物馆"} ,{"电视台","电台","报社","杂志社","出版社"},{"高等院校","中学","小学","幼儿园","成人教育","职业技术学校"}};

            for(int i =0;i<education.length;i++){
                if(name.equals(education[i])){
                    messageArray = educationChild[i];
                }
            }
// 交通设施服务


            String []  traffic=new String[]{"港口码头","公交车站","停车场"};
            String [][]  trafficChild =new String[][]{{"客运港","车渡口","人渡口"} ,{"旅游专线车站","普通公交站"},{"室内停车场","室外停车场","停车换乘点"}};

            for(int i =0;i<traffic.length;i++){
                if(name.equals(traffic[i])){
                    messageArray = trafficChild[i];
                }
            }

            //通过for循环来添加List<Map> 数据
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
            MySimpleAdapter mySimpleAdapter = new MySimpleAdapter(ThreeLevelActivity.this,MessageListMap,R.layout.char_item,
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
        public View getView( final int position, View convertView, ViewGroup parent) {
            View  v = super.getView(position,convertView,parent)  ;
            Button but = (Button)v.findViewById(R.id.ListBut);


            but.setTag(position);
            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    Toast.makeText(ThreeLevelActivity.this, "点按钮了"+MessageListMap.get(position).get("message"), 1000).show();
                }
            });
            return v;
            //To change body of overridden methods use File | Settings | File Templates.


        }
    }
}
