package com.facundo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


/**
 * Created by facundo on 11/08/15.
 */
public class DiarioRequest {

    private HttpClient client;
    private CookieStore cookieStore;

    public DiarioRequest() throws Exception{
        this.cookieStore = new BasicCookieStore();
        this.client = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
    }

    public void DownloadImageNews(String url, String download_path) throws Exception{
        String path_name = download_path + url.substring( url.lastIndexOf('/')+1, url.length() );

        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = this.client.execute(httpGet);
        HttpEntity entity = response.getEntity();

        if (entity != null){
            FileOutputStream file = new FileOutputStream(path_name);
            entity.writeTo(file);
            file.close();
        }
    }
}
