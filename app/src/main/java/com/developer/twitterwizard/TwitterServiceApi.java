package com.developer.twitterwizard;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface TwitterServiceApi {
    @GET

    Call<Results> fetchTweetsResult(@Url String url);
    @GET("/uploads/combined.zip")
    Call<ResponseBody> downloadFileWithFixedUrl();
    @GET
    Call<TopPositive> fetchPositiveResult(@Url String url);


}
