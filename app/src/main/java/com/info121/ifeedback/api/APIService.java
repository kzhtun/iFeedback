package com.info121.ifeedback.api;


import com.info121.ifeedback.models.Block;
import com.info121.ifeedback.models.CategoryRes;
import com.info121.ifeedback.models.Feedback;
import com.info121.ifeedback.models.FeedbackDetail;
import com.info121.ifeedback.models.FeedbackRes;
import com.info121.ifeedback.models.UpdateProfileRes;
import com.info121.ifeedback.models.RegisterReq;
import com.info121.ifeedback.models.RegisterRes;
import com.info121.ifeedback.models.SourcesRes;
import com.info121.ifeedback.models.SourcesTypeRes;
import com.info121.ifeedback.models.Storey;
import com.info121.ifeedback.models.UserProfileRes;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface APIService {

    @GET("getFeedbackSourcesList/Condominium")
    Call<List<SourcesRes>> getSourcesList();

    @GET("getFeedbackSourceTypesList")
    Call<List<SourcesTypeRes>> getSourcesTypeList();

    @GET("getBlockNos")
    Call<List<Block>> getBlockList();

    @GET("getFeedbackCategoryList")
    Call<List<CategoryRes>> getCategoryList();

    @GET("getstoreys/{block}")
    Call<List<Storey>> getStorey(@Path("block") String block);

    @GET("getFeedbacksListByUser/{user}")
    Call<List<Feedback>> getFeedbacks(@Path("user") String user);

    @GET("getFeedbackDraftListByUser/{user}")
    Call<List<Feedback>> getDrafts(@Path("user") String user);

    @GET("getFeedbackDetails/{id}")
    Call<FeedbackDetail> getFeedbackDetail(@Path("id") String id);

    @GET("getUserProfileByDeviceID/{deviceId}")
    Call<UserProfileRes> getUserProfileByDeviceID(@Path("deviceId") String deviceId);

    @POST("registerUserProfile")
    Call<RegisterRes> registerUser(@Body RegisterReq userReq);

    @GET("getUserProfile/{code}")
    Call<UserProfileRes> getUserProfile(@Path("code") String code);

    @GET("updatetokenid/{deviceId},{tokenId}")
    Call<String> updateTokenId(@Path("deviceId") String deviceId, @Path("tokenId") String tokenId);

    @GET("validateregistration/{username},{email}")
    Call<String> validateRegistration(@Path("username") String deviceId, @Path("email") String tokenId);



    @Multipart
    @POST("userprofile")
    Call<UpdateProfileRes> updateUserProfile(@Part("username") RequestBody username,
                                             @Part("email") RequestBody email,
                                             @Part("mobileno") RequestBody mobileno,
                                             @Part("deviceid") RequestBody deviceid,
                                             @Part MultipartBody.Part profilepic,
                                             @Part("address") RequestBody address,
                                             @Part("profilecode") RequestBody profilecode);


    @Multipart
    @POST("upload")
    Call<FeedbackRes> sendFeedback(@Part("username") RequestBody username,
                                   @Part MultipartBody.Part pic1,
                                   @Part MultipartBody.Part pic2,
                                   @Part MultipartBody.Part pic3,
                                   @Part("title") RequestBody title,
                                   @Part("feedback") RequestBody feedback,
                                   @Part("blockno") RequestBody blockno,
                                   @Part("storey") RequestBody storey,
                                   @Part("sourcecode") RequestBody sourcecode,
                                   @Part("sourceremarks") RequestBody sourceremarks,
                                   @Part("dateseen") RequestBody dateseen,
                                   @Part("timeseen") RequestBody timeseen,
                                   @Part("location") RequestBody location,
                                   @Part("feedbackcategory") RequestBody feedbackcategory,
                                   @Part("sourcetype") RequestBody sourcetype);


    @Multipart
    @POST("uploaddraft")
    Call<FeedbackRes> saveDraft(@Part("username") RequestBody username,
                                @Part MultipartBody.Part pic1,
                                @Part MultipartBody.Part pic2,
                                @Part MultipartBody.Part pic3,
                                @Part("title") RequestBody title,
                                @Part("feedback") RequestBody feedback,
                                @Part("blockno") RequestBody blockno,
                                @Part("storey") RequestBody storey,
                                @Part("sourcecode") RequestBody sourcecode,
                                @Part("sourceremarks") RequestBody sourceremarks,
                                @Part("dateseen") RequestBody dateseen,
                                @Part("timeseen") RequestBody timeseen,
                                @Part("location") RequestBody location);


}
