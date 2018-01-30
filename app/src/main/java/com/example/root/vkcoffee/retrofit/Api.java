package com.example.root.vkcoffee.retrofit;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by root on 23.1.18.
 */

public interface Api {

    @POST("al_audio.php")
    @FormUrlEncoded
    Call<ResponseBody> alAudio(@Header("Cookie") String str, @FieldMap Map<String, String> map);

    @POST("al_im.php")
    @FormUrlEncoded
    Call<ResponseBody> getUser(@Header("Cookie") String str, @FieldMap Map<String, String> map);

    @POST("al_friends.php")
    @FormUrlEncoded
    Call<ResponseBody> getFriends(@Header("Cookie") String str, @FieldMap Map<String, String> map);

    @POST("al_groups.php")
    @FormUrlEncoded
    Call<ResponseBody> getGroups(@Header("Cookie") String str, @FieldMap Map<String, String> map);

    @POST("al_wall.php")
    @FormUrlEncoded
    Call<ResponseBody> getWall(@Header("Cookie") String str, @FieldMap Map<String, String> map);


}
