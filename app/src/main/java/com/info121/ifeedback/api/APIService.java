package com.info121.ifeedback.api;


import com.info121.ifeedback.models.ProfileReq;
import com.info121.ifeedback.models.ProfileRes;
import com.info121.ifeedback.models.SourcesRes;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface APIService {

    @GET("getFeedbackSourcesList")
    Call<List<SourcesRes>> getSourcesList();

    @Multipart
    @POST("user")
    Call<ProfileRes> uploadProfile(@Part("UUID") RequestBody username,
                                   @Part  MultipartBody.Part  profilepic,
                                   @Part("UUID") RequestBody address,
                                   @Part("UUID") RequestBody mobileno,
                                   @Part("UUID") RequestBody email,
                                   @Part("UUID") RequestBody sourcecode,
                                   @Part("UUID") RequestBody deviceid );

}
