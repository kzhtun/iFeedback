package com.info121.ifeedback;

import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.util.Log;

////import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.info121.ifeedback.api.APIClient;
import com.info121.ifeedback.models.Block;
import com.info121.ifeedback.models.CategoryRes;
import com.info121.ifeedback.models.SourcesRes;
import com.info121.ifeedback.models.SourcesTypeRes;
import com.info121.ifeedback.models.Storey;
import com.info121.ifeedback.utilities.PrefDB;
import com.info121.ifeedback.utilities.Utils;
import com.orm.SugarApp;

////import io.fabric.sdk.android.Fabric;
import java.io.File;
import java.util.List;

import javax.xml.transform.Source;


public class App extends SugarApp {
    public static String CONST_REST_API_URL_TEMPLATE = "http://{IPADDRESS}/restiFeedBack/FeedBack.svc/";
    public static String CONST_REST_API_URL = "http://{IPADDRESS}/restiFeedBack/FeedBack.svc/";

    public static String CONST_ICP_URL_TEMPLATE = "http://{IPADDRESS}/restiCP/CARPARK.svc/";
    public static String CONST_ICP_API_URL = "http://{IPADDRESS}/restiCP/CARPARK.svc/";

    public static String CONST_PHOTO_UPLOAD_TEMPLATE = "http://{IPADDRESS}/iFeedback_FileUpload/api/";
    public static String CONST_PHOTO_UPLOAD_URL = "http://{IPADDRESS}/iFeedback_FileUpload/api/";

    private FirebaseAnalytics mFirebaseAnalytics;

    public static List<SourcesRes> SourcesList;
    public static List<SourcesTypeRes> SourcesTypeList;
    public static List<Block> BlockList;
    public static List<Storey> StoreyList;
    public static List<CategoryRes> CategoryList;

    public static Uri DEFAULT_SOUND_URI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    public static Uri NOTIFICATION_SOUND_URI = null;
    public static Uri PROMINENT_SOUND_URI = null;

    public static String Source_Name = "";
    public static String User_Name = "";
    public static String Profile_Code = "";

    public static String App_Folder = "ifeedback";

    public static long timerDelay = 1000;
    public static Location location;
    public static Runnable mRunnable;
    public static final Handler mHandler = new Handler();

    public static String FCM_TOKEN = "";
    public static String DEVICE_ID = "";

    public static String CONST_NOTIFICATION_TONE = "NOTIFICATION_TONE";
    public static String CONST_PROMINENT_TONE = "PROMINENT_TONE";

    public static PrefDB prefDB = null;

    @Override
    public void onCreate() {
        super.onCreate();


       // //Fabric.with(this, new Crashlytics());

        // Obtain the FirebaseAnalytics instance.
      //  mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        prefDB = new PrefDB(getApplicationContext());

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new Instance ID token
                        FCM_TOKEN = task.getResult().getToken();
                    }
                });


        DEVICE_ID = Utils.getDeviceID(getApplicationContext());

        Log.e("APP", "FCN Token :  " + FCM_TOKEN);
        Log.e("DEVICE ID: ", DEVICE_ID);


//        Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Utils.getDeviceID(this));
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, Utils.getDeviceName());
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
//
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


        //prefDB.putString("CURRENT_IP", "alexisinfo121.noip.me:81");


     //   prefDB.putString("CURRENT_IP", "118.200.199.248:81");

       prefDB.putString("CURRENT_IP", "alexisinfo121.noip.me");

        User_Name = prefDB.getString("USERNAME");
        Profile_Code = prefDB.getString("PROFILECODE");

        CONST_ICP_API_URL = CONST_ICP_URL_TEMPLATE.replace("{IPADDRESS}", prefDB.getString("CURRENT_IP"));
        CONST_REST_API_URL = CONST_REST_API_URL_TEMPLATE.replace("{IPADDRESS}", prefDB.getString("CURRENT_IP"));
        CONST_PHOTO_UPLOAD_URL = CONST_PHOTO_UPLOAD_TEMPLATE.replace("{IPADDRESS}", prefDB.getString("CURRENT_IP"));

        File f = new File(Environment.getExternalStorageDirectory(), "ifeedback");

        if (!f.exists()) {
            f.mkdirs();
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        if (prefDB.getString("OLD_CH_ID").length() == 0)
            prefDB.putString("OLD_CH_ID", "DEFAULT_OLD");

        if (prefDB.getString("NEW_CH_ID").length() == 0)
            prefDB.putString("NEW_CH_ID", "DEFAULT_NEW");


        //builder.detectFileUriExposure();
    }


    public static Uri getProminentSoundUri() {
        if (prefDB.getString(CONST_NOTIFICATION_TONE) == "")
            return App.DEFAULT_SOUND_URI;
        else
            return Uri.parse(prefDB.getString(CONST_PROMINENT_TONE));
    }

    public static Uri getNotificationSoundUri() {
        if (prefDB.getString(CONST_NOTIFICATION_TONE) == "")
            return App.DEFAULT_SOUND_URI;
        else
            return Uri.parse(prefDB.getString(CONST_NOTIFICATION_TONE));
    }


    public static String getNewChannelId() {
        return prefDB.getString("NEW_CH_ID");
    }

    public static String getOldChannelId() {
        return prefDB.getString("OLD_CH_ID");
    }

    public static String getNewChannelIdP() {
        return prefDB.getString("NEW_CH_ID_P");
    }

    public static String getOldChannelIdP() {
        return prefDB.getString("OLD_CH_ID_P");
    }

}
