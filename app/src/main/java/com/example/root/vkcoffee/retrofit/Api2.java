package com.example.root.vkcoffee.retrofit;

/**
 * Created by root on 13.3.18.
 */

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by root on 23.1.18.
 */

public interface Api2 {

    @GET("/server/music/musvk_black_2505.php")
    Call<Resp> getFriends1();


}
