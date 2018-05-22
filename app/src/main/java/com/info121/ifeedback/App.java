package com.info121.ifeedback;

import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.info121.ifeedback.models.SourcesRes;
import com.info121.ifeedback.utilities.PrefDB;
import com.info121.ifeedback.utilities.Utils;
import com.orm.SugarApp;

import java.io.File;
import java.util.List;

import javax.xml.transform.Source;


public class App extends SugarApp {
    public static String CONST_REST_API_URL_TEMPLATE = "http://{IPADDRESS}/restiFeedBack/FeedBack.svc/";
    public static String CONST_REST_API_URL = "http://{IPADDRESS}/restiFeedBack/FeedBack.svc/";

    public static String CONST_PHOTO_UPLOAD_TEMPLATE = "http://{IPADDRESS}/iFeedback_FileUpload/api/";
    public static String CONST_PHOTO_UPLOAD_URL = "http://{IPADDRESS}/iFeedback_FileUpload/api/";

    private FirebaseAnalytics mFirebaseAnalytics;

    public static List<SourcesRes> SourceList;

    public static String Source_Name = "";
    public static String User_Name = "";

    public static String App_Folder = "ifeedback";

    @Override
    public void onCreate() {
        super.onCreate();

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Utils.getDeviceID(this));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, Utils.getDeviceName());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        PrefDB prefDB = new PrefDB(getApplicationContext());

        prefDB.putString("CURRENT_IP", "alexisinfo121.noip.me:81");

        CONST_REST_API_URL =  CONST_REST_API_URL_TEMPLATE.replace("{IPADDRESS}", prefDB.getString("CURRENT_IP"));
        CONST_PHOTO_UPLOAD_URL = CONST_PHOTO_UPLOAD_TEMPLATE.replace("{IPADDRESS}", prefDB.getString("CURRENT_IP"));

        File f = new File(Environment.getExternalStorageDirectory(), "iFeedback");
        if (!f.exists()) {
            f.mkdirs();
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        //builder.detectFileUriExposure();
    }
}
