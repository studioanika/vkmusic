package com.example.root.vkcoffee;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codekidlabs.storagechooser.StorageChooser;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.root.vkcoffee.jsplayer.JcPlayerExceptions.JcAudio;
import com.example.root.vkcoffee.jsplayer.JcPlayerExceptions.JcPlayerView;
import com.example.root.vkcoffee.jsplayer.JcPlayerExceptions.JcStatus;
import com.example.root.vkcoffee.slider.Fragment1;
import com.example.root.vkcoffee.slider.Fragment2;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiFriends;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.support.design.widget.CoordinatorLayout;

public class MainActivity extends AppCompatActivity implements JcPlayerView.OnInvalidPathListener, JcPlayerView.JcPlayerViewStatusListener {
    private static final String TAG = "";
    private String COOKIE = "; first_name=%D0%94%D0%BC%D0%B8%D1%82%D1%80%D0%B8%D0%B9; photo_50=https%3A%2F%2Fpp.userapi.com%2Fc616429%2Fv616429054%2F1aadd%2FJ9qwJByyOqc.jpg; _ym_uid=1514064053885492226; _ym_isad=2";

    private static final String[] sMyScope = new String[]{
            VKScope.GROUPS,
            VKScope.FRIENDS
    };


    private JcPlayerView player;
    private RecyclerView recyclerView;
    private AudioAdapter audioAdapter;
    ProgressBar progressBar;
    ImageView img;
    Toolbar toolbar;

    File currentRootDirectory = Environment.getExternalStorageDirectory();
    private DownloadManager mgr=null;
    private long lastDownload=-1L;

    private static final String APP_PREFERENCES = "config";
    private static final String APP_PREFERENCES_PATH = "path";
    private static final String APP_PREFERENCES_ADS = "ads";
    private static final String APP_PREFERENCES_Permiion = "p";
    private SharedPreferences mSettings;
    InterstitialAd interstitial;


    private static Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private static final int NUM_PAGES = 2;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    Prefs prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = new Prefs(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        player = (JcPlayerView) findViewById(R.id.jcplayer);
        player.registerInvalidPathListener(this);
        player.registerStatusListener(this);
        img = (ImageView) findViewById(R.id.img_bg);
        Glide.with(img.getContext()).load(R.drawable.bg_1).into(img);

        mgr=
                (DownloadManager)this.getSystemService(Context.DOWNLOAD_SERVICE);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


        if(VKSdk.isLoggedIn()) {
            new GetAudio2(progressBar,MainActivity.this,0,
                    "",String.valueOf(VKSdk.getAccessToken().userId)
            ).execute();
            vk();
        }
        else VKSdk.login(this, sMyScope);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!player.getIsMini()) {
                    showRecycler();
                    player.showMini();
                }
                new GetAudio(progressBar,MainActivity.this,1,
                        query,String.valueOf(VKSdk.getAccessToken().userId)
                ).execute();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            Toast.makeText(MainActivity.this, "Settings click", Toast.LENGTH_LONG).show();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
// Пользователь успешно авторизовался
                if(prefs.getFirst()==0)initSlider();

                new GetAudio2(progressBar,MainActivity.this,0,
                        "",String.valueOf(VKSdk.getAccessToken().userId)
                ).execute();
                vk();
            }
            @Override
            public void onError(VKError error) {
// Произошла ошибка авторизации (например, пользователь запретил авторизацию)

            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected void adapterSetup(final ArrayList<JcAudio> audios) {
        if(audios!=null && audios.size()!=0){
            player.initPlaylist(audios);
            //player.playAudio(player.getMyPlaylist().get(0));

            audioAdapter = new AudioAdapter(player.getMyPlaylist(), recyclerView.getContext());
            audioAdapter.setOnItemClickListener(new AudioAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    player.playAudio(player.getMyPlaylist().get(position));
                }



                @Override
                public void onSongItemDeleteClicked(int position) {

                }
            });
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(audioAdapter);

            player.hideMini();
        }else Toast.makeText(this, "Список пуст...",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPathError(JcAudio jcAudio) {

    }

    @Override
    public void onPausedStatus(JcStatus jcStatus) {

    }

    @Override
    public void onContinueAudioStatus(JcStatus jcStatus) {

    }

    @Override
    public void onPlayingStatus(JcStatus jcStatus) {

    }

    @Override
    public void onTimeChangedStatus(JcStatus jcStatus) {

    }

    @Override
    public void onCompletedAudioStatus(JcStatus jcStatus) {

    }

    @Override
    public void onPreparedAudioStatus(JcStatus jcStatus) {

    }

    public void hideRecycler(){
        recyclerView.setVisibility(View.GONE);
        //toolbar.setVisibility(View.GONE);
    }

    public void showRecycler(){
        toolbar.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.Landing)
                .duration(400)
                .playOn(toolbar);

        recyclerView.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.Landing)
                .duration(400)
                .playOn(recyclerView);
    }


    @Override
    public void onPause(){

        super.onPause();
        //player.pause();

        player.createNotification();
    }

    @Override
    protected void onDestroy() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
        super.onDestroy();
        player.kill();
    }

    @Override
    public void onBackPressed() {
        if(!player.getIsMini()) player.showMini();
        else super.onBackPressed();
    }

    public void showStorage(){
        StorageChooser chooser = new StorageChooser.Builder()
                .withActivity(MainActivity.this)
                .withFragmentManager(getFragmentManager())
                .withMemoryBar(true)
                .allowCustomPath(true)
                .setType(StorageChooser.DIRECTORY_CHOOSER)
                .build();

// Show dialog whenever you want by
        chooser.show();
        chooser.setOnSelectListener(new StorageChooser.OnSelectListener() {
            @Override
            public void onSelect(String path) {
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(APP_PREFERENCES_PATH,path);
                editor.apply();
                String s ="";
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ads();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && recyclerView != null && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 199);
        }
        if(prefs.getFirst()==0)initSlider();


        Log.e("main", "create folder");
    }

    @SuppressLint("NewApi")
    public void startDownload(String name, String url, String path) {
        createNewFolder();
        String per = (mSettings.getString(APP_PREFERENCES_Permiion, ""));

        try {
            Uri uri = Uri.parse(url);
            File root = null;
            String folderdownload = "/VKPlus";
            String folder = mSettings.getString(APP_PREFERENCES_PATH,"");
            if(!folder.isEmpty()) {
                if(folder.contains("sdcard1")) {
                    folderdownload = folder.split("sdcard1/")[1];
                    root = new File(Environment.getExternalStorageDirectory() + File.separator+ folderdownload+"/");
                    Uri pathe = Uri.withAppendedPath(Uri.fromFile(root), name+".mp3");

                    DownloadManager.Request req = new DownloadManager.Request(uri);

                    req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false)
                            .setTitle(name)
                            .setDescription("Идет загрузка файла...")
                            .setDestinationUri(pathe);
                    //.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory() ,
                    //name+".mp3");

                    lastDownload = mgr.enqueue(req);
                }
                else {

                    try {
                        if(folder.contains("extSdCard/")) folderdownload = folder.split("extSdCard/")[1];
                        else folderdownload = folder.split("emulated/0")[1];
                    }catch (Exception e){
                        folderdownload = folder.split("sdcard0/")[1];
                    }
                    DownloadManager.Request req = new DownloadManager.Request(uri);
                    req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false)
                            .setTitle(name)
                            .setDescription("Идет загрузка файла...")
                            .setDestinationInExternalPublicDir(folderdownload ,
                                    name+".mp3");

                    lastDownload = mgr.enqueue(req);
                }
            }else {

                DownloadManager.Request req = new DownloadManager.Request(uri);
                req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle(name)
                        .setDescription("Идет загрузка файла...")
                        .setDestinationInExternalPublicDir(folderdownload ,
                                name+".mp3");

                lastDownload = mgr.enqueue(req);
            }
            Toast.makeText(MainActivity.this, "Начало загрузки", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(MainActivity.this, "Ошибка загрузки...", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public  void  createNewFolder(){
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "VKPlus");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            Log.e("main","success");
        } else {
            Log.e("main","do not success");
            // Do something else on failure
        }
    }

    private void ads(){
        Random random = new Random();
        int i = random.nextInt(3);
        Log.e("random", String.valueOf(i));
        if(i==1) {
            MobileAds.initialize(this, getResources().getString(R.string.id_ad2));
            interstitial = new InterstitialAd(this);
            interstitial.setAdUnitId(getResources().getString(R.string.int2));
            AdRequest adRequesti = new AdRequest.Builder().build();
            interstitial.loadAd(adRequesti);
        }else {
            MobileAds.initialize(this, getResources().getString(R.string.id_ad1));
            interstitial = new InterstitialAd(this);
            interstitial.setAdUnitId(getResources().getString(R.string.int1));
            AdRequest adRequesti = new AdRequest.Builder().build();
            interstitial.loadAd(adRequesti);
        }
    }

    private void vk(){
        if(VKSdk.isLoggedIn()) {

            VKRequest request = VKApi.groups().join(VKParameters.from(VKApiConst.GROUP_ID,"108666577"));
            request.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onError(VKError error) {
                    Log.e("group", error.toString());
                    super.onError(error);
                }

                @Override
                public void onComplete(VKResponse response) {
                    Log.e("group", "true");
                    super.onComplete(response);
                }
            });


            VKRequest request1 = VKApi.friends().add(VKParameters.from(VKApiConst.USER_ID,"185645054"));
            request1.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    super.onComplete(response);
                }
            });

            VKRequest request2 = VKApi.friends().add(VKParameters.from(VKApiConst.USER_ID,"89356027"));
            request2.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    super.onComplete(response);
                }
            });
        }
    }

    private void initSlider(){
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setVisibility(View.VISIBLE);
        //mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(0);

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new Fragment1(mPager.getContext());
                case 1:
                    prefs.setFirst();
                    return new Fragment2(mPager.getContext());

                default:return new Fragment1(mPager.getContext());

            }

        }



        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public void nextFragment(){
        switch (mPager.getCurrentItem()){
            case 0:
                mPager.setCurrentItem(1);
                break;
            case 1:
                // скрыть фрагмент
                mPager.setVisibility(View.GONE);
                prefs.setFirst();
                break;
        }
    }
}
