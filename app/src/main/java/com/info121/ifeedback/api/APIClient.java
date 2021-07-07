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
import com.info121.ifeedback.utilities.Utils;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;


public class APIClient {
    private static String DEVICE_TYPE = "Android";

    public static void GetSourcesList() {
        Call<List<SourcesRes>> call = RestClient.IFB().getApiService().getSourcesList();
        call.enqueue(new APICallback<List<SourcesRes>>() {
        });
    }

    public static void GetSourcesTypeList() {
        Call<List<SourcesTypeRes>> call = RestClient.IFB().getApiService().getSourcesTypeList();
        call.enqueue(new APICallback<List<SourcesTypeRes>>() {
        });
    }


    public static void GetBlockList() {
        Call<List<Block>> call = RestClient.ICP().getApiService().getBlockList();
        call.enqueue(new APICallback<List<Block>>() {
        });
    }

    public static void GetCategoryList() {
        Call<List<CategoryRes>> call = RestClient.IFB().getApiService().getCategoryList();
        call.enqueue(new APICallback<List<CategoryRes>>() {
        });
    }

    public static void GetStorey(String block) {
        Call<List<Storey>> call = RestClient.ICP().getApiService().getStorey(block);
        call.enqueue(new APICallback<List<Storey>>() {
        });
    }


    public static void GetFeedbacks(String user) {
        Call<List<Feedback>> call = RestClient.IFB().getApiService().getFeedbacks(user);
        call.enqueue(new APICallback<List<Feedback>>() {
        });
    }

    public static void GetDrafts(String user) {
        Call<List<Feedback>> call = RestClient.IFB().getApiService().getDrafts(user);
        call.enqueue(new APICallback<List<Feedback>>() {
        });
    }


    public static void GetFeedbackDetail(String id) {
        Call<FeedbackDetail> call = RestClient.IFB().getApiService().getFeedbackDetail(id);
        call.enqueue(new APICallback<FeedbackDetail>() {
        });
    }

    public static void GetUserProfileByDeviceID(String deviceId) {
        Call<UserProfileRes> call = RestClient.IFB().getApiService().getUserProfileByDeviceID(deviceId);
        call.enqueue(new APICallback<UserProfileRes>() {
        });
    }

    public static void RegisterUser(RegisterReq userReq) {
        Call<RegisterRes> call = RestClient.IFB().getApiService().registerUser(userReq);
        call.enqueue(new APICallback<RegisterRes>() {
        });
    }


    public static void GetUserProfile(String code) {
        Call<UserProfileRes> call = RestClient.IFB().getApiService().getUserProfile(code);
        call.enqueue(new APICallback<UserProfileRes>() {
        });
    }

    public static void updateUserProfile(RequestBody username,
                                         RequestBody email,
                                         RequestBody mobileno,
                                         RequestBody deviceid,
                                         MultipartBody.Part profilepic,
                                         RequestBody address,
                                         RequestBody profileCode) {

        Call<UpdateProfileRes> call = RestClient.UPLOAD().getApiService().updateUserProfile(username, email, mobileno, deviceid, profilepic, address, profileCode);
        call.enqueue(new APICallback<UpdateProfileRes>() {
        });
    }


    public static void sendFeedback(RequestBody username,
                                    MultipartBody.Part pic1,
                                    MultipartBody.Part pic2,
                                    MultipartBody.Part pic3,
                                    RequestBody title,
                                    RequestBody feedback,
                                    RequestBody blockno,
                                    RequestBody storey,
                                    RequestBody sourcecode,
                                    RequestBody sourceremarks,
                                    RequestBody dateseen,
                                    RequestBody timeseen,
                                    RequestBody location,
                                    RequestBody feedbackcategory,
                                    RequestBody sourcetype) {


        Call<FeedbackRes> call = RestClient.UPLOAD().getApiService().sendFeedback(username,
                pic1, pic2, pic3,
                title,
                feedback,
                blockno,
                storey,
                sourcecode,
                sourceremarks,
                dateseen,
                timeseen,
                location,
                feedbackcategory,
                sourcetype);
        call.enqueue(new APICallback<FeedbackRes>() {
        });
    }


    public static void saveDraft(RequestBody username,
                                 MultipartBody.Part pic1,
                                 MultipartBody.Part pic2,
                                 MultipartBody.Part pic3,
                                 RequestBody title,
                                 RequestBody feedback,
                                 RequestBody blockno,
                                 RequestBody storey,
                                 RequestBody sourcecode,
                                 RequestBody sourceremarks,
                                 RequestBody dateseen,
                                 RequestBody timeseen,
                                 RequestBody location) {


        Call<FeedbackRes> call = RestClient.UPLOAD().getApiService().saveDraft(username,
                pic1, pic2, pic3,
                title,
                feedback,
                blockno,
                storey,
                sourcecode,
                sourceremarks,
                dateseen,
                timeseen,
                location);
        call.enqueue(new APICallback<FeedbackRes>() {
        });
    }



    public static void UpdateTokenID(String deviceId, String tokenId) {
        Call<String> call = RestClient.IFB().getApiService().updateTokenId(deviceId, tokenId);
        call.enqueue(new APICallback<String>() {
        });
    }

    public static void ValidateRegistration(String username, String email) {
        Call<String> call = RestClient.IFB().getApiService().validateRegistration(username, email);
        call.enqueue(new APICallback<String>() {
        });
    }

}
