package com.allvideodownloader.quickdownloader2020.api;

import com.allvideodownloader.quickdownloader2020.model.TiktokModel;
import com.allvideodownloader.quickdownloader2020.model.TwitterResponse;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIServices {
    @GET
    Observable<JsonObject> callResult(@Url String value, @Header("Cookie") String cookie, @Header("User-Agent") String userAgent);

    @FormUrlEncoded
    @POST
    Observable<TwitterResponse> callTwitter(@Url String url, @Field("id") String id);

    @GET
    Observable<TiktokModel> getTiktokData(@Url String url1, @Query("url") String url);

    @GET
    Call<JsonObject> getSnackVideoData(@Url String url);

}