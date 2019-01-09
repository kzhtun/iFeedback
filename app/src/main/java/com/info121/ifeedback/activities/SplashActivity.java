package com.info121.ifeedback.activities;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.info121.ifeedback.AbstractActivity;
import com.info121.ifeedback.App;
import com.info121.ifeedback.R;
import com.info121.ifeedback.api.APIClient;
import com.info121.ifeedback.models.Block;
import com.info121.ifeedback.models.CategoryRes;
import com.info121.ifeedback.models.SourcesRes;
import com.info121.ifeedback.models.SourcesTypeRes;
import com.info121.ifeedback.utilities.PrefDB;
import com.info121.ifeedback.utilities.Utils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;

public class SplashActivity extends AbstractActivity {
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.CALL_PHONE};

    PrefDB prefDB;

    Boolean sourcesLoaded = false;
    Boolean sourcesTypeLoaded = false;

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

        if (Utils.isNullOrEmpty(prefDB.getString("CURRENT_IP"))) {
            finish();
            startActivity(new Intent(SplashActivity.this, SettingsActivity.class));
            return;
        } else {

            App.CONST_ICP_API_URL = App.CONST_ICP_URL_TEMPLATE.replace("{IPADDRESS}", prefDB.getString("CURRENT_IP"));
            App.CONST_REST_API_URL = App.CONST_REST_API_URL_TEMPLATE.replace("{IPADDRESS}", prefDB.getString("CURRENT_IP"));
            App.CONST_PHOTO_UPLOAD_URL = App.CONST_PHOTO_UPLOAD_TEMPLATE.replace("{IPADDRESS}", prefDB.getString("CURRENT_IP"));
        }


        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }


        if (hasPermissions(this, PERMISSIONS)) {
            APIClient.GetSourcesTypeList();
        }


//        if (prefDB.getBoolean("SYNCHONIZED")) {
//            startActivity(new Intent(SplashActivity.this, MainActivity.class));
//        }else{
//            // Load Source List
//            APIClient.GetSourcesList();
//        }
    }


    @Subscribe
    public void onEvent(List<?> res) {
        if (res.get(0) instanceof SourcesTypeRes) {
            App.SourcesTypeList = new ArrayList<>();
            App.SourcesTypeList.add(new SourcesTypeRes(""));
            App.SourcesTypeList.addAll((List<SourcesTypeRes>) res);

            APIClient.GetSourcesList();

        }

        if (res.get(0) instanceof SourcesRes) {

            //prefDB.putBoolean("SYNCHONIZED", true);
            App.SourcesList = new ArrayList<>();
            App.SourcesList.add(new SourcesRes("", "", ""));
            App.SourcesList.addAll((List<SourcesRes>) res);


            APIClient.GetBlockList();

        }


        if (res.get(0) instanceof SourcesRes) {

            //prefDB.putBoolean("SYNCHONIZED", true);
            App.SourcesList = new ArrayList<>();
            App.SourcesList.add(new SourcesRes("", "", ""));
            App.SourcesList.addAll((List<SourcesRes>) res);


            APIClient.GetCategoryList();

        }


        if (res.get(0) instanceof CategoryRes) {

            //prefDB.putBoolean("SYNCHONIZED", true);
            App.CategoryList = new ArrayList<>();
            App.CategoryList.add(new CategoryRes(""));
            App.CategoryList.addAll((List<CategoryRes>) res);


          //  APIClient.GetBlockList();

            finish();

            if (App.User_Name.isEmpty()) {
                startActivity(new Intent(SplashActivity.this, RegistrationActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }

        }


        if (res.get(0) instanceof Block) {
            App.BlockList = new ArrayList<>();
            App.BlockList.add(new Block(""));
            App.BlockList.addAll((List<Block>) res);


            finish();

            if (App.User_Name.isEmpty()) {
                startActivity(new Intent(SplashActivity.this, RegistrationActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));

            }
        }


    }

    @Subscribe
    public void onEvent(Throwable t) {
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(SplashActivity.this)
                .setMessage(t.getMessage())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void startApp() {

    }

}
