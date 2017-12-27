package com.example.root.vkcoffee;

/**
 * Created by root on 26.12.17.
 */

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.root.vkcoffee.jsplayer.JcPlayerExceptions.JcAudio;
import com.vk.sdk.VKAccessToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 24.12.17.
 */

public class GetAudio2 extends AsyncTask<Void, Void, ArrayList<JcAudio>> {
    private String COOKIE = "id=c9e95871924ba4b03317282bf9f70a10; mode=line; volume=75; vk_id=";
    private String COOKIE2 = "; first_name=%D0%94%D0%BC%D0%B8%D1%82%D1%80%D0%B8%D0%B9; photo_50=https%3A%2F%2Fpp.userapi.com%2Fc616429%2Fv616429054%2F1aadd%2FJ9qwJByyOqc.jpg; PHPSESSID=874df6e09cf74fd9ab4ef267a97008bc; lmomh=1";



    ProgressBar progressBar;
    MainActivity context;
    int type;
    String query;
    String id;


    public GetAudio2(ProgressBar progressBar, MainActivity context, int type, String query, String id) {
        this.progressBar = progressBar;
        this.context = context;
        this.type = type;
        this.query = query;
        this.id = id;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<JcAudio> aVoid) {
        progressBar.setVisibility(View.GONE);
        context.adapterSetup(aVoid);
    }

    @Override
    protected ArrayList<JcAudio> doInBackground(Void... voids) {
        ArrayList<JcAudio> jcAudios = new ArrayList<>();
        switch (type){
            case 0:
                query = "";
                break;
            case 1:
                query = "?q="+query;
                break;
        }

        String sd = getServerResponseByHttpGet("http://music.xn--41a.ws/скачать-музыку-с-вк/"+query,
                COOKIE+ id+COOKIE2);

        Document doc = Jsoup.parse(sd);

        Elements li_all = doc.select("li.track");

        for(int i =0; i< li_all.size(); i++){

//            Element a = li_all.get(i).select("a").first();
//            String a_href = a.attr("href").toString();
//            String a_title = a.text().toString();
//            String[] titles = a_title.split("-");
//            String dsd = "";
            String a_title = li_all.get(i).select("b").first().text().toString() +
                    " - "+ li_all.get(i).select("em").get(1).text().toString();
            String a_href = "http://music.xn--41a.ws"+ li_all.get(i).attr("data-mp3").toString();
            jcAudios.add(JcAudio.createFromURL(a_title,a_href));


        }

        String d = "";


        return jcAudios;
    }

    public static String getServerResponseByHttpGet(String url, String token) {

        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(url);
            get.setHeader("Cookie", token );
            Log.d(TAG, "Try to open => " + url);

            HttpResponse httpResponse = client.execute(get);
            int connectionStatusCode = httpResponse.getStatusLine().getStatusCode();
            Log.d(TAG, "Connection code: " + connectionStatusCode + " for request: " + url);

            HttpEntity entity = httpResponse.getEntity();
            String serverResponse = EntityUtils.toString(entity);
            Log.d(TAG, "Server response for request " + url + " => " + serverResponse);


            return serverResponse;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
