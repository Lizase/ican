package com.example.weiting.taipei;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Weiting on 2017/9/24.
 */

public class DBconnected {
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
