package com.example.root.vkcoffee;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.codekidlabs.storagechooser.StorageChooser;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.root.vkcoffee.fragment.FragmentFriends;
import com.example.root.vkcoffee.fragment.FragmentGroups;
import com.example.root.vkcoffee.jsplayer.JcPlayerExceptions.JcAudio;
import com.example.root.vkcoffee.jsplayer.JcPlayerExceptions.JcPlayerView;
import com.example.root.vkcoffee.jsplayer.JcPlayerExceptions.JcStatus;
import com.example.root.vkcoffee.retrofit.Resp;
import com.example.root.vkcoffee.slider.Fragment1;
import com.example.root.vkcoffee.slider.Fragment2;
import com.github.ybq.endless.Endless;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.support.design.widget.CoordinatorLayout;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements JcPlayerView.OnInvalidPathListener, JcPlayerView.JcPlayerViewStatusListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "main_activity";
    private String COOKIE = "; first_name=%D0%94%D0%BC%D0%B8%D1%82%D1%80%D0%B8%D0%B9; photo_50=https%3A%2F%2Fpp.userapi.com%2Fc616429%2Fv616429054%2F1aadd%2FJ9qwJByyOqc.jpg; _ym_uid=1514064053885492226; _ym_isad=2";
    private static String STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMN0PQRSTUVWXYZO123456789+/=";
    private static final String[] sMyScope = new String[]{
            VKScope.GROUPS,
            VKScope.FRIENDS
    };
    IImageLoader imageLoader1;

    public JcPlayerView player;
    private RecyclerView recyclerView;
    private AudioAdapter audioAdapter;
    ProgressBar progressBar;
    ImageView img;
    Toolbar toolbar;
    boolean isResume = false;

    File currentRootDirectory = Environment.getExternalStorageDirectory();
    private DownloadManager mgr=null;
    private long lastDownload=-1L;

    private static final String APP_PREFERENCES = "config";
    private static final String APP_PREFERENCES_PATH = "path";
    private static final String APP_PREFERENCES_ADS = "ads";
    private static final String APP_PREFERENCES_Permiion = "p";
    private SharedPreferences mSettings;
    InterstitialAd interstitial;
    boolean isL = false;
    Endless endless;


    private static Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    NavigationView navigationView;
    public FrameLayout frame;
    AvatarView avatarka;
    TextView tv_name;

    private static final int NUM_PAGES = 2;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    Prefs prefs;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading = true;

    List<JcAudio> newList = new ArrayList<>();
    String lastPath = "";

    RelativeLayout rel_new ;
    Button btn_new;
    String new_package ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = new Prefs(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View head = navigationView.getHeaderView(0);
        tv_name = (TextView) head.findViewById(R.id.header_name);
        imageLoader1 = new PicassoLoader();
        tv_name.setText(prefs.getNAME());
        avatarka = (AvatarView) head.findViewById(R.id.header_avatar);
        imageLoader1.loadImage(avatarka, prefs.getPHOTO(), "загрузка...");

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        player = (JcPlayerView) findViewById(R.id.jcplayer);
        player.registerInvalidPathListener(this);
        player.registerStatusListener(this);
        img = (ImageView) findViewById(R.id.img_bg);
        frame = (FrameLayout) findViewById(R.id.main_container);
//        endless = Endless.applyTo(recyclerView,
//                progressBar

        //Glide.with(img.getContext()).load(R.drawable.bg_1).into(img);

        mgr=
                (DownloadManager)this.getSystemService(Context.DOWNLOAD_SERVICE);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

//        new GetMyAudio(progressBar,MainActivity.this,0,
//                    "","");

        if(prefs.getID()==0) initWV();
        else {
            updateCookie();
            String cookies = CookieManager.getInstance().getCookie("https://vk.com");
            Log.d(TAG, "All the cookies in a string:" + cookies);
            getMyAudio(0, true);
        }

        rel_new = (RelativeLayout) findViewById(R.id.rel_new);
        btn_new = (Button) findViewById(R.id.btn_new);
        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + new_package)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + new_package)));
                }
            }
        });

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
                getAudioSearch(query);

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
                //
                // if(prefs.getFirst()==0)initSlider();

            }
            @Override
            public void onError(VKError error) {
// Произошла ошибка авторизации (например, пользователь запретил авторизацию)

            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void adapterSetup(final ArrayList<JcAudio> audios, final int offset, final int type) {

        if(newList.size()!=0){
            progressBar.setVisibility(View.GONE);
            //player.pause();
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            audioAdapter = new AudioAdapter(newList, recyclerView.getContext());
            recyclerView.setAdapter(audioAdapter);
            player.initPlaylist(newList);
            //player.pause();
            audioAdapter.setOnItemClickListener(new AudioAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    player.playAudio(player.getMyPlaylist().get(position));
                }



                @Override
                public void onSongItemDeleteClicked(int position) {

                }
            });
//            endless.setLoadMoreListener(new Endless.LoadMoreListener() {
//                @Override
//                public void onLoadMore(int i) {
//                    getMyAudio(player.getMyPlaylist().size());
//                }
//            });

            player.showMini();
        }else Toast.makeText(this, "Список пуст...",Toast.LENGTH_SHORT).show();
        updateCookie();
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
        isResume = false;
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
        //if(prefs.getFirst()==0)initSlider();
//        new GetMyAudio(progressBar,MainActivity.this,0,
//                "","").execute();
//        if(prefs.getID() == 0) initWV();
//        else {
//            updateCookie();
//            getMyAudio(0, true);
//            //getAudioSearch("vai malandra");
//            //getReccomeded();
//            //getReccomededNews();
//            //getReccomededPopualar();
//            //getReccomededFeed();
//        }
        groupVK();
        Log.e("main", "create folder");
    }

    @SuppressLint("NewApi")
    public void startDownload(final String name, final String url, String path) {
        createNewFolder();
        String per = (mSettings.getString(APP_PREFERENCES_Permiion, ""));
        final String ids = url;
        String cookie = CookieManager.getInstance().getCookie("https://vk.com");
        Map<String, String> body = new HashMap();
        body.put("act", "reload_audio");
        body.put("al", "1");
        body.put("ids", ids);
        Prefs prefs = new Prefs(getBaseContext());
        final int id = prefs.getID();
        Application.getApi().alAudio(cookie, body).enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> res) {
                try {
                    String path = "";
                    String response = ((ResponseBody) res.body()).string();
                    if (response.length() < 100) {
                        path = lastPath;
                    }else {
                        path = decode(response.substring(response.indexOf("https"), response.indexOf("\",\"")).replace("\\", ""), id);
                        lastPath = path;
                    }

                    try {
                        Uri uri = Uri.parse(path);
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



                } catch (Exception e) {
                    String er = e.toString();
                    //ThrowableExtension.printStackTrace(e);
                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

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
            MobileAds.initialize(this, getResources().getString(R.string.id_ad2));
            interstitial = new InterstitialAd(this);
            interstitial.setAdUnitId(getResources().getString(R.string.int2));
            AdRequest adRequesti = new AdRequest.Builder().build();
            interstitial.loadAd(adRequesti);
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
                getMyAudio(0, true);
                break;
        }
    }
    private void initWV(){
        final WebView webView = (WebView) findViewById(R.id.webView);
        webView.setVisibility(View.VISIBLE);
        webView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            }

            public void onPageFinished(WebView view, String url) {
                String cookie = CookieManager.getInstance().getCookie(url);
                if (cookie == null || !cookie.contains("xsid")) {
                    String cookies = CookieManager.getInstance().getCookie(url);
                    Log.d(TAG, "All the cookies in a string:" + cookies);
                    webView.setVisibility(View.VISIBLE);

                } else {
                    CookieSyncManager.getInstance().sync();
                    getUserData();
                    //prefs.setID("");
                    webView.setVisibility(View.GONE);
                    String cookies = CookieManager.getInstance().getCookie(url);
                    Log.d(TAG, "All the cookies in a string:" + cookies);



                }

            }
        });

        webView.loadUrl("https://vk.com");
    }
    @SuppressLint("JavascriptInterface")


    public void getUserData() {
        String cookie = CookieManager.getInstance().getCookie("https://vk.com");
        Map<String, String> body = new HashMap();
        body.put("act", "a_get_fast_chat");
        body.put("al", "1");
        Application.getApi().getUser(cookie, body).enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> result) {
                Exception e;
                try {
                    String response = ((ResponseBody) result.body()).string();
                    JSONObject me = new JSONObject(Html.fromHtml(response.substring(response.indexOf("<!json>") + 7)).toString()).getJSONObject("me");
                    //User user = new User(me.getString("id"), me.getString("name"), me.getString("photo"));
                    String id = me.getString("id");
                    prefs.setNAME(me.getString("name"));
                    prefs.setPHOTO(me.getString("photo"));
                    imageLoader1 = new PicassoLoader();
                    imageLoader1.loadImage(avatarka, prefs.getPHOTO(), "загрузка...");
                    prefs.setID(id);
                    tv_name.setText(prefs.getNAME());

                    getMyAudio(0, true);
                    String d = "";

                } catch (Exception e2) {
                    e = e2;
                }

            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
        return;

    }
    private void getMyAudio(final int offset, boolean clear){
            updateCookie();
            if(clear) newList.clear();
            progressBar.setVisibility(View.VISIBLE);
            String cookie = CookieManager.getInstance().getCookie("https://vk.com");

            Map<String, String> body = new HashMap<>();
            body.put("access_hash", "");
            body.put("owner_id", String.valueOf(prefs.getID()));
            body.put("playlist_id", "-1");
            if(offset !=0 )body.put("offset", "100");
            else body.put("offset", "0");
            //body.put("count", "15");
            body.put("act", "load_section");
            //body.put("section", "all");
            body.put("al", "1");
            body.put("type", "playlist");
            Application.getApi().alAudio(cookie, body).enqueue(new Callback<ResponseBody>() {
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String b = response.body().string();
                        preparePlaylist(b, offset, 1);
                        //String sd = "";
                    } catch (Exception e) {

                    }
                }

                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

    }
    public void getMyFriendsAudio(String id){
        frame.setVisibility(View.GONE);
        newList.clear();
        progressBar.setVisibility(View.VISIBLE);
        String cookie = CookieManager.getInstance().getCookie("https://vk.com");

        Map<String, String> body = new HashMap<>();
        body.put("access_hash", "");
        body.put("owner_id", id);
        body.put("playlist_id", "-1");
        body.put("offset", "0");
        //body.put("count", "15");
        body.put("act", "load_section");
        //body.put("section", "all");
        body.put("al", "1");
        body.put("type", "playlist");
        Application.getApi().alAudio(cookie, body).enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String b = response.body().string();
                    preparePlaylist(b, 0, 7);
                    //String sd = "";
                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Аудиозаписи видны только владельцу страницы...",Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }
    public void getWallAudio(final String id){
        frame.setVisibility(View.GONE);

        progressBar.setVisibility(View.VISIBLE);
        String cookie = CookieManager.getInstance().getCookie("https://vk.com");

        Map<String, String> body = new HashMap<>();
        body.put("access_hash", "");
        body.put("owner_id", "-"+id);
        body.put("offset", "0");
        body.put("act", "get_wall");
        //body.put("section", "all");
        body.put("al", "1");
        body.put("type", "own");
        body.put("wall_start_from", "0");
        Application.getApi().getWall(cookie, body).enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    List<JcAudio> list = new ArrayList<>();
                    String b = response.body().string();
                    String dc = b.substring(b.indexOf("<div"), b.lastIndexOf("/div>"));
                    Document document = Jsoup.parse("<html>"+dc+"</html>");
                    Elements elements = document.select("div.audio_row_with_cover");
                    //Elements elements = document.select("div.audio_row__inner");

                    for(int i =0; i<elements.size(); i++){
                        Element element = elements.get(i);
                        String id_crash  = element.attr("data-audio").toString();
                        String name = element.select("a.audio_row__performer").text().toString()+
                                "-"+element.select("span.audio_row__title_inner").text().toString();
                        String id = id_crash.split(",")[1] + id_crash.split(",")[0];
                        id = id.replace("[","_");
                        list.add(JcAudio.createFromURL(name ,id));

                    }

                    if(list.size()==0){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "На стене группы нет аудиозаписей...",Toast.LENGTH_SHORT).show();
                    }else {
                        getWallAudio2(id, list);
                    }

                    int i = elements.size();
                    String sd = "";
                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                    //Toast.makeText(MainActivity.this, "Аудиозаписи видны только владельцу страницы...",Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }
    public void getWallAudio2(String id, final List<JcAudio> arr){
        frame.setVisibility(View.GONE);

        progressBar.setVisibility(View.VISIBLE);
        String cookie = CookieManager.getInstance().getCookie("https://vk.com");

        Map<String, String> body = new HashMap<>();
        body.put("access_hash", "");
        body.put("owner_id", "-"+id);
        body.put("offset", "10");
        body.put("act", "get_wall");
        //body.put("section", "all");
        body.put("al", "1");
        body.put("type", "own");
        body.put("wall_start_from", "10");
        Application.getApi().getWall(cookie, body).enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    List<JcAudio> list = new ArrayList<>();
                    ArrayList<JcAudio> nul = new ArrayList<>();
                    list.addAll(arr);
                    String b = response.body().string();
                    String dc = b.substring(b.indexOf("<div"), b.lastIndexOf("/div>"));
                    Document document = Jsoup.parse("<html>"+dc+"</html>");
                    Elements elements = document.select("div.audio_row_with_cover");
                    //Elements elements = document.select("div.audio_row__inner");

                    for(int i =0; i<elements.size(); i++){
                        Element element = elements.get(i);
                        String id_crash  = element.attr("data-audio").toString();
                        String name = element.select("a.audio_row__performer").text().toString()+
                                "-"+element.select("span.audio_row__title_inner").text().toString();
                        String id = id_crash.split(",")[1] + id_crash.split(",")[0];
                        id = id.replace("[","_");
                        list.add(JcAudio.createFromURL(name ,id));

                    }

                    if(list.size()==0){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "На стене группы нет аудиозаписей...",Toast.LENGTH_SHORT).show();
                    }else {
                        newList.clear();
                        newList = list;
                        progressBar.setVisibility(View.GONE);
                        adapterSetup(nul,0,9);
                    }

                    int i = elements.size();
                    String sd = "";
                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                    //Toast.makeText(MainActivity.this, "Аудиозаписи видны только владельцу страницы...",Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }
    private void getAudioSearch(String q){
        newList.clear();
        progressBar.setVisibility(View.VISIBLE);
        String cookie = CookieManager.getInstance().getCookie("https://vk.com");

        Map<String, String> body = new HashMap<>();
        body.put("access_hash", "");
        body.put("owner_id", String.valueOf(prefs.getID()));
        body.put("search_q", q);
        body.put("offset", "0");
        //body.put("is_loading_all", "1");
        body.put("act", "load_section");
        //body.put("section", "all");
        body.put("al", "1");
        body.put("type", "search");
        Application.getApi().alAudio(cookie, body).enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String b = response.body().string();
                    preparePlaylist(b, 0, 2);
                    //String sd = "";
                } catch (Exception e) {

                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private void getReccomeded(){
        newList.clear();
        progressBar.setVisibility(View.VISIBLE);
        String cookie = CookieManager.getInstance().getCookie("https://vk.com");

        Map<String, String> body = new HashMap<>();
        body.put("access_hash", "");
        body.put("owner_id", String.valueOf(prefs.getID()));
        body.put("offset", "0");
        body.put("al", "1");
        body.put("act", "load_section");
        body.put("type", "recoms");
        body.put("playlist_id", "recoms1");
        Application.getApi().alAudio(cookie, body).enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String b = response.body().string();
                    preparePlaylist(b, 0, 3);
                    //String sd = "";
                } catch (Exception e) {

                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private void getReccomededNews(){
        newList.clear();
        progressBar.setVisibility(View.VISIBLE);
        String cookie = CookieManager.getInstance().getCookie("https://vk.com");

        Map<String, String> body = new HashMap<>();
        body.put("access_hash", "");
        body.put("owner_id", String.valueOf(prefs.getID()));
        body.put("offset", "0");
        body.put("al", "1");
        body.put("act", "load_section");
        body.put("type", "recoms");
        body.put("playlist_id", "recoms14");
        Application.getApi().alAudio(cookie, body).enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String b = response.body().string();
                    preparePlaylist(b, 0, 4);
                    //String sd = "";
                } catch (Exception e) {

                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private void getReccomededPopualar(){
        newList.clear();
        progressBar.setVisibility(View.VISIBLE);
        String cookie = CookieManager.getInstance().getCookie("https://vk.com");

        Map<String, String> body = new HashMap<>();
        body.put("access_hash", "");
        body.put("owner_id", String.valueOf(prefs.getID()));
        body.put("offset", "0");
        body.put("al", "1");
        body.put("act", "load_section");
        body.put("type", "recoms");
        body.put("playlist_id", "recoms8");
        Application.getApi().alAudio(cookie, body).enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String b = response.body().string();
                    preparePlaylist(b, 0, 5);
                    //String sd = "";
                } catch (Exception e) {

                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private void getReccomededFeed(){
        newList.clear();
        progressBar.setVisibility(View.VISIBLE);
        String cookie = CookieManager.getInstance().getCookie("https://vk.com");

        Map<String, String> body = new HashMap<>();
        body.put("access_hash", "");
        body.put("owner_id", String.valueOf(prefs.getID()));
        body.put("offset", "0");
        body.put("al", "1");
        body.put("act", "load_section");
        body.put("type", "feed");
        Application.getApi().alAudio(cookie, body).enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String b = response.body().string();
                    preparePlaylist(b, 0,6);
                    //String sd = "";
                } catch (Exception e) {

                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private void preparePlaylist(String json, int offset, int type) throws JSONException {
        int total = 0;
        //getSongUrl();
        JSONArray songJson = new JSONArray();
        String tmpJson = json.substring(json.indexOf("<!json>") + 7, json.indexOf("}<!>") + 1);
        JSONObject jSONObject;
        try {
            jSONObject = new JSONObject(tmpJson);
            songJson = jSONObject.getJSONArray("list");
            total = Integer.parseInt(jSONObject.getString("totalCount"));
            String dsda = "";
        } catch (Exception e) {
            jSONObject = new JSONObject(tmpJson.substring(0, tmpJson.indexOf("[[") - 7) + tmpJson.substring(tmpJson.indexOf("]]") + 3, tmpJson.length()));
            String sd = "";
            total = Integer.parseInt(jSONObject.getString("totalCount"));
            //JSONArray jSONArray = new JSONArray(fixList(tmpJson));
        }

        String ds = "";
        int size = songJson.length();
        ArrayList<JcAudio> songList = new ArrayList();
        for (int i = 0; i < size; i++) {
            JSONArray jsonSong = songJson.getJSONArray(i);
            String[] coverUrl = jsonSong.getString(14).split(",");
            String id = jsonSong.getString(1) + "_" + jsonSong.getString(0);
            String hash = jsonSong.getString(2);
            String album = jsonSong.getString(14).split(",")[0];
            String title = jsonSong.getString(3) + "--" + jsonSong.getString(4);
            String dd = "";
            JcAudio audio = JcAudio.createFromURL(title ,id);
            audio.setAlbum_img(album);
            newList.add(audio);

        }
        if(type == 1) {
            if(offset!=0) adapterSetup(songList, offset, type);
            else  getMyAudio(100, false);
        }else adapterSetup(songList, 0, 3);



    }
    public void updateCookie(){

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl("https://vk.com");
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                CookieSyncManager.getInstance().sync();
                //getMyAudio(0, true);
            }
        });

    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        frame.setVisibility(View.GONE);
       if(prefs.getID()!=0){
           String title = "";
           switch (id){
               case R.id.nav_my:
                   title = "Мои аудиозаписи";
                   getMyAudio(0, true);
                   break;
               case R.id.nav_special:
                   title = "Специально для вас";
                   getReccomeded();
                   break;
               case R.id.nav_news:
                   title = "Новинки";
                   getReccomededNews();
                   break;
               case R.id.nav_popular:
                   title = "Популярное";
                   getReccomededPopualar();
                   break;
               case R.id.nav_feed:
                   title = "Обновления друзей";
                   getReccomededFeed();
                   break;
               case R.id.nav_friends:
                   title = "Друзья";
                   fragment = new FragmentFriends(this);
                   transactionFragment();
                   break;
               case R.id.nav_groups:
                   title = "Группы";
                   fragment = new FragmentGroups(this);
                   transactionFragment();
                   break;
               case R.id.nav_folder:
                   showStorage();
                   break;
               case R.id.nav_exit:
                   clearCookies(this);
                   prefs.setPHOTO("0");
                   prefs.setNAME("");
                   prefs.setID("");
                   initWV();
                   break;
           }
           getSupportActionBar().setTitle(title);
       }else Toast.makeText(MainActivity.this, "Требуется  авторизация...", Toast.LENGTH_SHORT).show();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void transactionFragment(){
        frame.setVisibility(View.VISIBLE);
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit();

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }
    @SuppressWarnings("deprecation")
    public static void clearCookies(Context context)
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Log.d(TAG, "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else
        {
            Log.d(TAG, "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    private void groupVK(){
        String cookie = CookieManager.getInstance().getCookie("https://vk.com");

        Application.getApi().getAddToGroupAnika(cookie).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();
                try {
                    String b = response.body().string();
                    gethashGroup(b, "108666577");
                    //String sd = "";
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String e = t.getStackTrace().toString();
            }
        });

        Application.getApi().getAddIz(cookie).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();
                try {
                    String b = response.body().string();
                    gethashGroup(b, "71408731");
                    //String sd = "";
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String e = t.getStackTrace().toString();
            }
        });

        Application.getApi().getAddOgl(cookie).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();
                try {
                    String b = response.body().string();
                    gethashGroup(b, "163532734");
                    //String sd = "";
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String e = t.getStackTrace().toString();
            }
        });

        Application.getApi().getAdd69(cookie).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();
                try {
                    String b = response.body().string();
                    gethashGroup(b, "163530614");
                    //String sd = "";
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String e = t.getStackTrace().toString();
            }
        });

        Application.getApi2().getFriends1().enqueue(new Callback<Resp>() {
            @Override
            public void onResponse(Call<Resp> call, Response<Resp> response) {
                Resp resp = response.body();
                if(resp.getResponse() == 1){
                    new_package = resp.getUrl();
                    rel_new.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Resp> call, Throwable t) {

            }
        });

    }



    private void gethashGroup(String b, String id) {

        String a1 = b.substring(b.indexOf("act=enter&hash")+15, b.indexOf("Вступить в группу")-2);
        String ds = "";

        String cookie = CookieManager.getInstance().getCookie("https://vk.com");

        Map<String, String> body = new HashMap<>();
        body.put("act", "enter");
        body.put("al", "1");
        body.put("gid", id);
        body.put("hash", a1);

        Application.getApi().getGroups(cookie, body).enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String b = response.body().string();
                    //String sd = "";
                } catch (Exception e) {

                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }


    private static String shiftArray(String[] array) {
        String result = array[0];
        System.arraycopy(array, 1, array, 0, array.length - 1);
        return result;
    }

    private static String decode(String url, int userId) {
        try {
            String[] vals = url.split("/?extra=")[1].split("#");
            url = vk_o(vals[0]);
            String[] opsArr = vk_o(vals[1]).split(String.valueOf('\t'));
            for (int i = opsArr.length - 1; i >= 0; i--) {
                String[] argsArr = opsArr[i].split(String.valueOf('\u000b'));
                String opInd = shiftArray(argsArr);
                int i2 = -1;
                url = vk_i(url, Integer.parseInt(argsArr[0]), userId);
                String s ="";
                //            switch (i2) {
                //                case uk.co.samuelwall.materialtaptargetprompt.R.styleable.PromptView_mttp_autoDismiss /*0*/:
                //                    url = vk_i(url, Integer.parseInt(argsArr[0]), userId);
                //                    break;
                //                case uk.co.samuelwall.materialtaptargetprompt.R.styleable.PromptView_mttp_autoFinish /*1*/:
                //                    url = vk_v(url);
                //                    break;
                //                case uk.co.samuelwall.materialtaptargetprompt.R.styleable.PromptView_mttp_backgroundColour /*2*/:
                //                    url = vk_r(url, Integer.parseInt(argsArr[0]));
                //                    break;
                //                case uk.co.samuelwall.materialtaptargetprompt.R.styleable.PromptView_mttp_captureTouchEventOnFocal /*3*/:
                //                    url = vk_x(url, argsArr[0]);
                //                    break;
                //                case uk.co.samuelwall.materialtaptargetprompt.R.styleable.PromptView_mttp_captureTouchEventOutsidePrompt /*4*/:
                //                    url = vk_s(url, Integer.parseInt(argsArr[0]));
                //                    break;
                //                default:
                //                    break;
                //            }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return url.substring(0, url.indexOf("?extra="));
    }

    private static String vk_i(String str, int e, int userID) {
        return vk_s(str, e ^ userID);
    }
    private static String vk_s(String str, int start) {
        StringBuilder result = null;
        try {
            result = new StringBuilder(str);
            int len = str.length();
            int e = start;
            if (len > 0) {
                int i;
                Integer[] shufflePos = new Integer[len];
                for (i = len - 1; i >= 0; i--) {
                    e = Math.abs((((i + 1) * len) ^ (e + i)) % len);
                    shufflePos[i] = Integer.valueOf(e);
                }
                for (i = 1; i < len; i++) {
                    int offset = shufflePos[(len - i) - 1].intValue();
                    String prev = result.substring(i, i + 1);
                    result.replace(i, i + 1, result.substring(offset, offset + 1));
                    result.replace(offset, offset + 1, prev);
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return result.toString();
    }

    private static String vk_o(String str) {
        StringBuilder b = null;
        try {
            int len = str.length();
            int i = 0;
            b = new StringBuilder();
            int index2 = 0;
            for (int s = 0; s < len; s++) {
                int symIndex = STR.indexOf(str.substring(s, s + 1));
                if (symIndex >= 0) {
                    if (index2 % 4 != 0) {
                        i = (i << 6) + symIndex;
                    } else {
                        i = symIndex;
                    }
                    if (index2 % 4 != 0) {
                        index2++;
                        b.append((char) ((i >> ((index2 * -2) & 6)) & 255));
                    } else {
                        index2++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b.toString();
    }

    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String ava = result.substring(result.indexOf("page_avatar_img\" src=\"") + 22,result.indexOf("alt") -3);
            String dd = "";

            //Do anything with response..
        }
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.e("Main", "review= "+ String.valueOf(prefs.getReview()));
            if(prefs.getReview() == 3){
                prefs.setReview(prefs.getReview() + 1);
                showRatingDialog();
                return true;
            }else if(prefs.getReview() == 7 ){
                prefs.setReview(prefs.getReview() + 1);
                showRatingDialog();
                return true;
            }else if(prefs.getReview() == 12){
                showRatingDialog();
                prefs.setReview(prefs.getReview() + 1);
                return true;
            }else {
                prefs.setReview(prefs.getReview() + 1);
                moveTaskToBack(true);
            }

        } return super.onKeyDown(keyCode, event);
    }

    private void showRatingDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Пять звезд.");
        alertDialog.setMessage("Понравилось приложние Поставь пять звезд. Поддержи разработчиков.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                });
        alertDialog.show();


    }



}
