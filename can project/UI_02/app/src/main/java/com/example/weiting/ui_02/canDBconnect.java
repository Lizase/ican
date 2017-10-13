package com.example.weiting.ui_02;

/**
 * Created by Weiting on 2017/8/2.
 */
import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class canDBconnect {

    public static String getData(ArrayList<NameValuePair> params,String url) {
        String result = "";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            //view_account.setText(httpResponse.getStatusLine().toString());
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream inputStream = httpEntity.getContent();

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            StringBuilder builder = new StringBuilder();
            String line = null;
            //line = bufReader.readLine();
            while((line = bufReader.readLine()) != null) {

                builder.append(line + "\n");
            }
            inputStream.close();
            result = builder.toString();
            //result = line;
        } catch(Exception e) {
            //Log.e("log_tag", e.toString());
        }

        return result;
    }
    public static String updateData(String url) {
        String result = "";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpget);
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream inputStream = httpEntity.getContent();
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while((line = bufReader.readLine()) != null) {
                builder.append(line);
            }
            inputStream.close();
            result = builder.toString();
        } catch(Exception e) {
            //Log.e("log_tag", e.toString());
        }

        return result;

    }
}
