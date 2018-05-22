package com.info121.ifeedback.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.info121.ifeedback.AbstractActivity;
import com.info121.ifeedback.App;
import com.info121.ifeedback.R;
import com.info121.ifeedback.api.APIClient;
import com.info121.ifeedback.models.ProfileReq;
import com.info121.ifeedback.models.ProfileRes;
import com.info121.ifeedback.models.SourcesRes;
import com.info121.ifeedback.utilities.Utils;

import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @BindView(R.id.profile_image)
    ImageView mProfileImage;

    @BindView(R.id.set_profile)
    ImageView mSetProfile;

    private static final int REQ_PROFILE_PHOTO = 2011;

    String filename;
    Uri imageUri;

    ContentValues values;

    ArrayAdapter<SourcesRes> sourceAdapter;

    Context mContext;

    ProfileReq profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ButterKnife.bind(this);

        initializeControls();
        populateSources();

        profile = new ProfileReq(UUID.randomUUID().toString(), Utils.getDeviceID(mContext));

    }

    private void initializeControls() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mContext = RegistrationActivity.this;

        mToolbar.setTitle("Registration");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void populateSources() {
        sourceAdapter = new ArrayAdapter<SourcesRes>(mContext, R.layout.support_simple_spinner_dropdown_item, App.SourceList);
        mSource.setAdapter(sourceAdapter);
    }

    @OnClick(R.id.set_profile)
    void openCamera() {

        final int requestCode = REQ_PROFILE_PHOTO;

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, profile.getUuid() + requestCode);
                //    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");

                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, requestCode);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String fileName = profile.getUuid() + "_" + requestCode + "_" + ".jpg";

        if (resultCode != Activity.RESULT_OK) return;

        Bitmap photo = getPhoto();

        if (requestCode == REQ_PROFILE_PHOTO) {
            //  mVehiclePhoto.setImageBitmap(photo);
            mProfileImage.setImageBitmap(photo);
            profile.setProfilePhoto(fileName);
            savePhoto(photo, fileName);

            // vehicle.setPhotoVehicle(fileName);
        }
    }


    public Bitmap getPhoto() {
        try {
            Bitmap thumbnail = MediaStore.Images.Media.getBitmap(
                    getContentResolver(), imageUri);


            ExifInterface ei = new ExifInterface(getRealPathFromURI(imageUri));
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap rotatedBitmap = null;
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(thumbnail, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(thumbnail, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(thumbnail, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = thumbnail;
            }

            return rotatedBitmap;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void savePhoto(Bitmap photoData, String fileName) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photoData.compress(Bitmap.CompressFormat.JPEG, 10, bytes);

        File file = new File(Environment.getExternalStorageDirectory() + File.separator + App.App_Folder + File.separator + fileName);

        try {
            file.createNewFile();
            FileOutputStream fo = new FileOutputStream(file);

            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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

    private void sendProfile(ProfileReq p) {
        pd = ProgressDialog.show(RegistrationActivity.this, "", "Sending...", true, false);

        APIClient.uploadProfile(
                getTextRequestBody(p.getUsername()),
                getFileRequestBody(p.getProfilePhoto(), "profilepic"),
                getTextRequestBody(p.getAddress()),
                getTextRequestBody(p.getMobileno()),
                getTextRequestBody(p.getEmail()),
                getTextRequestBody(p.getSourcecode()),
                getTextRequestBody(p.getDeviceid())
        );
    }


    @OnClick(R.id.btn_save)
    public void btnSaveOnclick(){
        if (validateProfile()) {
                profile.setUsername(mFullName.getText().toString());
                profile.setEmail(mEmail.getText().toString());
                profile.setMobileno(mMobile.getText().toString());
                profile.setSourcecode(mSource.getSelectedItem().toString());
                profile.setAddress(mAddress.getText().toString());

                sendProfile(profile);
        }
    }

    private boolean validateProfile(){
        if (Utils.isNullOrEmpty(mFullName.getText().toString())) {
            mFullName.setError("Name should not be blank.");
            mFullName.setFocusable(true);
            return false;
        }

        if (Utils.isNullOrEmpty(mEmail.getText().toString())) {
            mFullName.setError("Email should not be blank.");
            mFullName.setFocusable(true);
            return false;
        }

        if (Utils.isNullOrEmpty(mMobile.getText().toString())) {
            mFullName.setError("Mobile number should not be blank.");
            mFullName.setFocusable(true);
            return false;
        }

        return true;
    }


    @Subscribe
    public void onEvent(ProfileRes res) {
        if (res.getStatus().equalsIgnoreCase("SUCCESS")) {

            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }

            final AlertDialog alertDialog = new AlertDialog.Builder(RegistrationActivity.this)
                    .setMessage("Record successfully uploaded to server.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
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
