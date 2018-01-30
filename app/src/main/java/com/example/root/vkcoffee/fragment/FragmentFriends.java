package com.example.root.vkcoffee.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.ProgressBar;

import com.example.root.vkcoffee.Application;
import com.example.root.vkcoffee.AudioAdapter;
import com.example.root.vkcoffee.MainActivity;
import com.example.root.vkcoffee.Prefs;
import com.example.root.vkcoffee.R;
import com.example.root.vkcoffee.adapter.FriendsAdapter;
import com.example.root.vkcoffee.jsplayer.JcPlayerExceptions.JcAudio;
import com.example.root.vkcoffee.retrofit.Friend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 24.1.18.
 */

@SuppressLint("ValidFragment")
public class FragmentFriends extends Fragment {

    View v;
    Prefs prefs;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    FriendsAdapter audioAdapter;
    MainActivity activity;

    public FragmentFriends(MainActivity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_list, container, false);
        prefs = new Prefs(v.getContext());
        progressBar = (ProgressBar) v.findViewById(R.id.friends_progress);
        recyclerView = (RecyclerView) v.findViewById(R.id.list);

        getUserFriends();
        return v;
    }

    private void getUserFriends(){
        String cookie = CookieManager.getInstance().getCookie("https://vk.com");
        Map<String, String> body = new HashMap();
        body.put("act", "load_friends_silent");
        body.put("id", String.valueOf(prefs.getID()));
        body.put("al", "1");
        Application.getApi().getFriends(cookie, body).enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> result) {
                Exception e;
                JSONArray songJson = new JSONArray();

                try {
                    String response = ((ResponseBody) result.body()).string();
                    String doc = "";

                    JSONObject me = null;
                    try {
                        doc = response.substring(response.indexOf("all")-2, response.indexOf("requests") - 2);
                        me = new JSONObject(Html.fromHtml(doc+"}").toString());
                    } catch (Exception e1) {
                        doc = response.substring(response.indexOf("all")-2, response.indexOf("all_requests") - 2);
                        me = new JSONObject(Html.fromHtml(doc+"}").toString());
                    }
                    songJson = me.getJSONArray("all");

                    int size = songJson.length();
                    ArrayList<Friend> songList = new ArrayList();
                    for (int i = 0; i < size; i++) {
                        JSONArray jsonSong = songJson.getJSONArray(i);
                        Friend f = new Friend();
                        f.setId(jsonSong.getString(0));
                        f.setImg(jsonSong.getString(1));
                        f.setName(jsonSong.getString(5));
                        String dd = "";

                        songList.add(f);

                    }

                    String d = "";
                    progressBar.setVisibility(View.GONE);
                    setUpRecycler(songList);

                } catch (Exception e2) {
                    progressBar.setVisibility(View.GONE);
                    e = e2;
                }

            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
        return;


    }

    private void setUpRecycler(final ArrayList<Friend> arrayList){

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        audioAdapter = new FriendsAdapter(arrayList, this.getContext());
        recyclerView.setAdapter(audioAdapter);

        audioAdapter.setOnItemClickListener(new FriendsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                activity.getMyFriendsAudio(arrayList.get(position).getId());
            }

            @Override
            public void onSongItemDeleteClicked(int position) {

            }
        });

    }
}
