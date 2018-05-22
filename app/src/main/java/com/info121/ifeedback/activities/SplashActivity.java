package com.info121.ifeedback.activities;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.info121.ifeedback.AbstractActivity;
import com.info121.ifeedback.App;
import com.info121.ifeedback.R;
import com.info121.ifeedback.api.APIClient;
import com.info121.ifeedback.models.SourcesRes;
import com.info121.ifeedback.utilities.PrefDB;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.ButterKnife;

public class SplashActivity extends AbstractActivity {
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.CALL_PHONE};

    PrefDB prefDB;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);
        prefDB = new PrefDB(getApplicationContext());

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        if (hasPermissions(this, PERMISSIONS)) {
            APIClient.GetSourcesList();
        }
//        if (prefDB.getBoolean("SYNCHONIZED")) {
//            startActivity(new Intent(SplashActivity.this, MainActivity.class));
//        }else{
//            // Load Source List
//            APIClient.GetSourcesList();
//        }
    }


    @Subscribe
    public void onEvent(List<SourcesRes> res) {
        if (res.get(0) instanceof SourcesRes) {

            // UserRes.deleteAll(UserRes);

//            for (int i = 0; i < res.size(); i++) {
//                SourcesRes s = res.get(i);
//                s.save();
//            }

            //prefDB.putBoolean("SYNCHONIZED", true);

            App.SourceList = res;

            finish();
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
    }


    private  void sendUserInfo(){

    }
}
