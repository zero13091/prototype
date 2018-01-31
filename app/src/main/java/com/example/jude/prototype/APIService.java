package com.example.jude.prototype;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Jude on 11/9/2017.
 */

public interface APIService {

    @POST("sqlcommand")
    @FormUrlEncoded
    Call<Post> savePost(@Field("sqlcommand") String sqlcommand);

}
