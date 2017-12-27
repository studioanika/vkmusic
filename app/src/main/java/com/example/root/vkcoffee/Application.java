package com.example.root.vkcoffee;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;
import com.vk.sdk.util.VKUtil;

/**
 * Created by root on 24.12.17.
 */

public class Application extends android.app.Application {
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
        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());

        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
    }
}