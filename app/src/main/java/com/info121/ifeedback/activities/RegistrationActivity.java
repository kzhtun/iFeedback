package com.info121.ifeedback.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

//import com.crashlytics.android.Crashlytics;
import com.info121.ifeedback.AbstractActivity;
import com.info121.ifeedback.App;
import com.info121.ifeedback.R;
import com.info121.ifeedback.api.APIClient;
import com.info121.ifeedback.models.RegisterReq;
import com.info121.ifeedback.models.RegisterRes;
import com.info121.ifeedback.models.SourcesRes;
import com.info121.ifeedback.utilities.PrefDB;
import com.info121.ifeedback.utilities.Utils;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
//import io.fabric.sdk.android.Fabric;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegistrationActivity extends AbstractActivity {
    private ProgressDialog pd;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.full_name)
    EditText mFullName;

    @BindView(R.id.email)
    EditText mEmail;

    @BindView(R.id.mobile)
    EditText mMobile;

    @BindView(R.id.source)
    Spinner mSource;

    @BindView(R.id.address)
    EditText mAddress;


    String filename;
    Uri imageUri;

    ContentValues values;

    ArrayAdapter<SourcesRes> sourceAdapter;

    Context mContext;

    RegisterReq userReq;
    PrefDB prefDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ButterKnife.bind(this);

        initializeControls();
        populateSources();

        userReq = new RegisterReq(Utils.getDeviceID(mContext));


        prefDB = new PrefDB(getApplicationContext());

        //Fabric.with(this, new Crashlytics());

//        InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
//        imeManager.showInputMethodPicker();

    }

    private void initializeControls() {
        mToolbar = findViewById(R.id.toolbar);

        mContext = RegistrationActivity.this;

        mToolbar.setTitle("Registration");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void populateSources() {
        sourceAdapter = new ArrayAdapter<SourcesRes>(mContext, R.layout.support_simple_spinner_dropdown_item, App.SourcesList);
        mSource.setAdapter(sourceAdapter);
    }

    private RequestBody getTextRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    private MultipartBody.Part getFileRequestBody(String fileName, String tagName) {
        if (Utils.isNullOrEmpty(fileName)) return null;

        File file = new File(Environment.getExternalStorageDirectory() + File.separator + App.App_Folder + File.separator + fileName);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

        return MultipartBody.Part.createFormData(tagName, file.getName(), requestFile);

    }

    @OnClick(R.id.btn_save)
    public void btnSaveOnclick() {
        if (validateProfile()) {
            userReq.setUsername(mFullName.getText().toString());
            userReq.setEmail(mEmail.getText().toString());
            userReq.setMobileno(mMobile.getText().toString());
            userReq.setSourcecode(mSource.getSelectedItem().toString());
            userReq.setUsertype("resident");
            userReq.setAddress(mAddress.getText().toString());
            userReq.setTokenid(App.FCM_TOKEN);

            // APIClient.RegisterUser(userReq);
            APIClient.ValidateRegistration(userReq.getUsername(), userReq.getEmail());
        }
    }

    private boolean validateProfile() {
        if (Utils.isNullOrEmpty(mFullName.getText().toString())) {
            mFullName.setError("Name should not be blank.");
            mFullName.setFocusable(true);
            mFullName.requestFocus();
            return false;
        }

        if (Utils.isNullOrEmpty(mEmail.getText().toString())) {
            mEmail.setError("Email should not be blank.");
            mEmail.setFocusable(true);
            mEmail.requestFocus();
            return false;

        } else {
            if (!Utils.isValidEmaillId(mEmail.getText().toString())) {
                mEmail.setError("Please type valid email.");
                mEmail.setFocusable(true);
                mEmail.requestFocus();
                return false;
            }
        }

        if (Utils.isNullOrEmpty(mMobile.getText().toString())) {
            mMobile.setError("Mobile number should not be blank.");
            mMobile.setFocusable(true);
            mMobile.requestFocus();
            return false;
        }

        return true;
    }


    @Subscribe
    public void onEvent(String message) {
        if (message.equalsIgnoreCase("valid")) {
            APIClient.RegisterUser(userReq);
        }

        if (message.equalsIgnoreCase("error: invalid user")) {
            mFullName.setError("User name already exist.");
            mFullName.setFocusable(true);
            mFullName.requestFocus();
        }

        if (message.equalsIgnoreCase("error: invalid email")) {
            mEmail.setError("Email already exist.");
            mEmail.setFocusable(true);
            mEmail.requestFocus();
        }
    }

    @Subscribe
    public void onEvent(RegisterRes res) {
        if (res.getStatus().equalsIgnoreCase("SUCCESS")) {

            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }

            App.User_Name = res.getUsername();
            App.Profile_Code = res.getProfilecode();

            prefDB.putString("USERNAME", App.User_Name);
            prefDB.putString("PROFILECODE", App.Profile_Code);

            final AlertDialog alertDialog = new AlertDialog.Builder(RegistrationActivity.this)
                    .setMessage("Record successfully uploaded to server.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                        }
                    }).create();
            alertDialog.setCancelable(false);
            alertDialog.show();

        }
    }

    @Subscribe
    public void onEvent(Throwable t) {
        final AlertDialog alertDialog = new AlertDialog.Builder(RegistrationActivity.this)
                .setMessage(t.getMessage())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.setCancelable(false);
        alertDialog.show();


        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
