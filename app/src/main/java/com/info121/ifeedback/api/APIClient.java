package com.info121.ifeedback.api;


import com.info121.ifeedback.models.ProfileReq;
import com.info121.ifeedback.models.ProfileRes;
import com.info121.ifeedback.models.SourcesRes;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Part;


public class APIClient {
    private static String DEVICE_TYPE = "Android";

    public static void GetSourcesList() {
        Call<List<SourcesRes>> call = RestClient.IFB().getApiService().getSourcesList();
        call.enqueue(new APICallback<List<SourcesRes>>() {
        });
    }

    public static void uploadProfile( RequestBody username,
                                     MultipartBody.Part  profilepic,
                                     RequestBody address,
                                     RequestBody mobileno,
                                     RequestBody email,
                                     RequestBody sourcecode,
                                     RequestBody deviceid) {

        Call<ProfileRes> call = RestClient.UPLOAD().getApiService().uploadProfile(username, profilepic, address, mobileno, email, sourcecode, deviceid);
        call.enqueue(new APICallback<ProfileRes>() {
        });
    }

}
