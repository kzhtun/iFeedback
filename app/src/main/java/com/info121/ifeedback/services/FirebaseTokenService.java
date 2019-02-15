package com.info121.ifeedback.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.info121.ifeedback.App;
import com.info121.ifeedback.api.APIClient;

/**
 * Created by KZHTUN on 7/28/2017.
 */

public class FirebaseTokenService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseTokenService";

    String FCM_TOKEN = "";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        App.FCM_TOKEN = FirebaseInstanceId.getInstance().getToken();
       // Log.e(TAG, "FCN Token" + FCM_TOKEN);

        if (App.FCM_TOKEN.length() > 0)
            APIClient.UpdateTokenID(App.DEVICE_ID, App.FCM_TOKEN);

    }
}
