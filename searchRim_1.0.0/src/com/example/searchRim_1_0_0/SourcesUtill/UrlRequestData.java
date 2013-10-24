package com.example.searchRim_1_0_0.SourcesUtill;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: ${luozhimin}
 * Date: 13-10-19
 * Time: 上午11:45
 * To change this template use File | Settings | File Templates.
 */
public class UrlRequestData {
    public  static   String requestServerData(String url) throws IOException {
        //请求服务器
        HttpGet request = new HttpGet(url);
        DefaultHttpClient httpClient = new DefaultHttpClient();

        HttpResponse response = httpClient.execute(request);
        String responseStr = EntityUtils.toString(response.getEntity(), "UTF-8");

        return responseStr;
    }
}
