package com.example.searchRim_1_0_0.SourcesUtill;

import android.view.View;
import android.widget.Button;

/**
 * Created with IntelliJ IDEA.
 * User: ${luozhimin}
 * Date: 13-10-16
 * Time: 下午11:08
 * To change this template use File | Settings | File Templates.
 */
public class GetData {
    /**
     *  获得二级页面数据
     * @param name
     * @return  返回二级StringArray
     */
    public static String[] getSecondStringArray(String name) {
        String messageArray[]=new String[]{};
        if(name.equals("餐饮服务")){

            messageArray = new String[]{"中餐厅","外国餐厅","快餐厅","休闲餐饮场所","咖啡厅","茶艺馆",
                    "冷饮店","糕饼店","甜品店"};
        }
        if(name.equals("购物服务")){

            messageArray  = new String[]{"商场","便民商店/便利店","家电电子卖场","超级市场","花鸟鱼虫市场","家居建材市场",
                    "综合市场","文化用品店","体育用品店","特色商业街","服装鞋帽皮具店","专卖店","特殊买卖场所","个人用品/化妆品店"};

        }
        if(name.equals("生活服务")){
         messageArray  = new String[]{"旅行社","信息咨询中心","售票处",
                    "邮局","物流速递","电讯营业厅","事务所","人才市场",
                    "自来水营业厅","店里营业厅","美容美发店","维修站点","摄影冲印店" ,
                    "洗浴推拿场所","洗衣店","中介机构","搬家公司","彩票彩卷销售点","丧葬设施"};

        }
        if(name.equals("体育休闲服务")){
            messageArray  = new String[]{"运动场所","高尔夫相关","娱乐场所","度假疗养场所","休闲场所","影剧院"};
        }

        if(name.equals("医疗保健服务")){
            messageArray  = new String[]{"综合医院","专科医院","诊所","急救中心","疾病预防机构","医药保健相关","动物医疗场所"};
        }

        if(name.equals("住宿服务")){
            messageArray  = new String[]{"宾馆酒店","旅馆招待所"};
        }
        if(name.equals("科教文化服务")){
            messageArray  = new String[]{"博物馆","展览馆","会展中心","美术馆","图书馆","科技馆","天文馆","文化宫","档案馆","文艺团体","传媒机构","学校","科研机构","培训机构","驾校"};
        }
        if(name.equals("交通设施服务")){
            messageArray  = new String[]{"飞机场","火车站","港口码头","长途汽车站","地铁站","轻轨站","公交车站","班车站","停车场","过境口岸"};
        }
        if(name.equals("公共设施")){
            messageArray  = new String[]{"报刊亭","公用电话","公共厕所","紧急避难场所"};
        }
        return messageArray;    //To change body of overridden methods use File | Settings | File Templates.
    }

}
