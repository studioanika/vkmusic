package com.example.root.vkcoffee;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.root.vkcoffee.retrofit.Api;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;
import com.vk.sdk.util.VKUtil;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by root on 24.12.17.
 */

public class Application extends android.app.Application {

    private static Api umoriliApi;
    private Retrofit retrofit;

    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
//            if (newToken == null) {
//                Toast.makeText(Application.this, "AccessToken invalidated", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(Application.this, LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }else startActivity(new Intent(Application.this, MainActivity.class));
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://vk.com") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        umoriliApi = retrofit.create(Api.class);

        //String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());

        //vkAccessTokenTracker.startTracking();
        //VKSdk.initialize(this);
    }

    public static Api getApi() {
        return umoriliApi;
    }

}