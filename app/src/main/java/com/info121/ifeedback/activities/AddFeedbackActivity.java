package com.info121.ifeedback.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.info121.ifeedback.AbstractActivity;
import com.info121.ifeedback.App;
import com.info121.ifeedback.R;
import com.info121.ifeedback.api.APIClient;
import com.info121.ifeedback.models.Block;
import com.info121.ifeedback.models.CategoryRes;
import com.info121.ifeedback.models.FeedbackReq;
import com.info121.ifeedback.models.FeedbackRes;
import com.info121.ifeedback.models.SourcesRes;
import com.info121.ifeedback.models.SourcesTypeRes;
import com.info121.ifeedback.models.Storey;
import com.info121.ifeedback.utilities.Utils;

import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnItemSelected;
import io.fabric.sdk.android.Fabric;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class AddFeedbackActivity extends AbstractActivity implements OnLocationUpdatedListener {
    private ProgressDialog pd;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.remarks)
    TextView mRemarks;

    @BindView(R.id.source)
    Spinner mSource;

    @BindView(R.id.category)
    Spinner mCategory;

    @BindView(R.id.photo_1)
    ImageView mPhoto1;

    @BindView(R.id.photo_2)
    ImageView mPhoto2;

    @BindView(R.id.photo_3)
    ImageView mPhoto3;

    @BindView(R.id.date)
    TextView mDate;

    @BindView(R.id.time)
    TextView mTime;

    @BindView(R.id.block)
    TextView mBlock;

    @BindView(R.id.storey)
    TextView mStorey;

    @BindView(R.id.location)
    TextView mLocation;

    Context mContext;
    ContentValues values;

    ArrayAdapter<SourcesRes> sourcesAdapter;
    ArrayAdapter<SourcesTypeRes> sourcesTypeAdapter;
    ArrayAdapter<CategoryRes> categoryAdapter;
    ArrayAdapter<Block> blockAdapter;
    ArrayAdapter<Storey> storeyAdapter;

    private static final int REQ_PHOTO_1 = 2011;
    private static final int REQ_PHOTO_2 = 2012;
    private static final int REQ_PHOTO_3 = 2013;

    private static final int REQ_GALLERY_1 = 2021;
    private static final int REQ_GALLERY_2 = 2022;
    private static final int REQ_GALLERY_3 = 2023;

    Boolean isDraftSave = false;

    String filename;
    Bitmap photo;
    File file;
    Uri imageUri;

    FeedbackReq feedBack;

    Calendar myCalendar;


    @Override
    public void onLocationUpdated(Location location) {
        App.location = location;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feedback);

        ButterKnife.bind(this);

        feedBack = new FeedbackReq();

        initializeControls();
        initializeEvents();

        populateSources();
        // populateSourcesType();
        populateCategory();
        //     populateBlock();

//        storeyAdapter = new ArrayAdapter<>(mContext, R.layout.support_simple_spinner_dropdown_item, new ArrayList<Storey>());
//        mStorey.setAdapter(storeyAdapter);


        SmartLocation.with(AddFeedbackActivity.this).location()
                .oneFix()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        App.location = location;
                        mLocation.setText(Utils.getCompleteAddressString(AddFeedbackActivity.this));
                    }
                });

        //  mLocation.setText(Utils.getCompleteAddressString( mContext));

        Fabric.with(this, new Crashlytics());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // startLocationService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //stopLocationService();
    }

    private void initializeControls() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mContext = AddFeedbackActivity.this;

        mToolbar.setTitle("Send Feedback");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myCalendar = Calendar.getInstance();

    }


    private void showSelectDialog(final int requestCode) {

        final Dialog dialog = new Dialog(this, R.style.AppTheme);
        dialog.setContentView(R.layout.dialog_setting_selection);
        dialog.setTitle("App Selection");

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Window window = dialog.getWindow();
        window.setAttributes(lp);
        window.setGravity(Gravity.CENTER);

        //adding dialog animation sliding up and down
        // dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

        // to cancel when outside touch
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Fade;

        // window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

        Button mCamera = dialog.findViewById(R.id.btn_camera);
        Button mGallery = dialog.findViewById(R.id.btn_gallery);


        mGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                openGallery(2020 + requestCode);
            }
        });

        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                openCamera(2010 + requestCode);
            }
        });

        dialog.show();

    }

    @OnFocusChange(R.id.date)
    public void dateOnFocusChange(boolean focused) {
        if (focused) showDateDialog();
    }

    @OnClick(R.id.date)
    public void dateOnClick() {
        showDateDialog();
    }


    private void showDateDialog() {
        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                mDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        new DatePickerDialog(AddFeedbackActivity.this, dateListener,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }


    @OnFocusChange(R.id.time)
    public void timeOnFocusChange(boolean focused) {
        if (focused) showTimeDialog();
    }


    @OnClick(R.id.time)
    public void timeOnClick() {
        showTimeDialog();
    }

    private void showTimeDialog() {
        TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int i, int i1) {
                myCalendar.set(Calendar.HOUR_OF_DAY, i);
                myCalendar.set(Calendar.MINUTE, i1);

                String myFormat = "HH:mm:ss"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                mTime.setText(sdf.format(myCalendar.getTime()));
                //) i + ":" + i1);
            }
        };

        new TimePickerDialog(AddFeedbackActivity.this, timeListener,
                myCalendar.get(Calendar.HOUR_OF_DAY),
                myCalendar.get(Calendar.MINUTE),
                true
        ).show();


    }


    private void initializeEvents() {


    }

    private void populateSourcesType() {
        sourcesTypeAdapter = new ArrayAdapter<>(mContext, R.layout.support_simple_spinner_dropdown_item, App.SourcesTypeList);
        mCategory.setAdapter(sourcesTypeAdapter);
    }

    private void populateCategory() {
        categoryAdapter = new ArrayAdapter<>(mContext, R.layout.support_simple_spinner_dropdown_item, App.CategoryList);
        mCategory.setAdapter(categoryAdapter);
    }

    private void populateSources() {
        sourcesAdapter = new ArrayAdapter<>(mContext, R.layout.support_simple_spinner_dropdown_item, App.SourcesList);
        mSource.setAdapter(sourcesAdapter);
    }


//    private void populateBlock() {
//        blockAdapter = new ArrayAdapter<>(mContext, R.layout.support_simple_spinner_dropdown_item, App.BlockList);
//        mBlock.setAdapter(blockAdapter);
//    }
//
//    @OnItemSelected(R.id.block)
//    public void blockOnItemSelected() {
//        String blockNo = mBlock.getSelectedItem().toString();
//        if (blockNo.length() > 0)
//            APIClient.GetStorey(blockNo);
//    }


    @Subscribe
    public void onEvent(List<Storey> res) {
        if (res.get(0) instanceof Storey) {
            App.StoreyList = new ArrayList<>();
            App.StoreyList.add(new Storey("", ""));
            App.StoreyList.addAll((List<Storey>) res);

            //populateStorey();
        }
    }

//    private void populateStorey() {
//        storeyAdapter = new ArrayAdapter<>(mContext, R.layout.support_simple_spinner_dropdown_item, App.StoreyList);
//        mStorey.setAdapter(storeyAdapter);
//    }


    @OnClick(R.id.photo_1)
    public void photo1onclick() {
        //  openGallery(REQ_GALLERY_1);
        // openCamera(REQ_PHOTO_1);

        showSelectDialog(1);
    }

    @OnClick(R.id.photo_2)
    public void photo2onclick() {
        showSelectDialog(2);
    }

    @OnClick(R.id.photo_3)
    public void photo3onclick() {
        showSelectDialog(3);
    }


    void openCamera(final int requestCode) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, feedBack.getUuid() + requestCode);
                //    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");

                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, requestCode);
            }
        });
    }


    void openGallery(int requestCode) {
        final int reqCode = requestCode;

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, reqCode);
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        filename = "";

        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == REQ_PHOTO_1 || requestCode == REQ_PHOTO_2 || requestCode == REQ_PHOTO_3) {
            filename = feedBack.getUuid() + "_" + requestCode + "_" + ".jpg";
            photo = getPhoto();
            file = new File(getRealPathFromURI(imageUri));
            file.delete();
            savePhoto(photo, filename);
        }

        if (requestCode == REQ_GALLERY_1 || requestCode == REQ_GALLERY_2 || requestCode == REQ_GALLERY_3) {
            if (data != null) {
                Uri contentURI = data.getData();
                file = new File(getRealPathFromURI(contentURI));

                filename = file.getAbsolutePath();

                photo = BitmapFactory.decodeFile(filename);

            }
        }

        switch (requestCode) {
            case REQ_PHOTO_1:
                mPhoto1.setImageBitmap(photo);
                mPhoto1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                feedBack.setPic1(filename);

                // driverPhotoOnClick();
                break;

            case REQ_PHOTO_2:
                mPhoto2.setImageBitmap(photo);
                mPhoto2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                feedBack.setPic2(filename);

                break;

            case REQ_PHOTO_3:
                mPhoto3.setImageBitmap(photo);
                mPhoto3.setScaleType(ImageView.ScaleType.CENTER_CROP);
                feedBack.setPic3(filename);

                break;


            case REQ_GALLERY_1:
                mPhoto1.setImageBitmap(photo);
                mPhoto1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                feedBack.setPic1(filename);

                break;

            case REQ_GALLERY_2:
                mPhoto2.setImageBitmap(photo);
                mPhoto2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                feedBack.setPic2(filename);

                break;


            case REQ_GALLERY_3:
                mPhoto3.setImageBitmap(photo);
                mPhoto3.setScaleType(ImageView.ScaleType.CENTER_CROP);
                feedBack.setPic3(filename);

                break;
        }

        mLocation.setText(Utils.getCompleteAddressString(AddFeedbackActivity.this));

    }


    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + App.App_Folder);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
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


    private void saveDraft() {
        pd = ProgressDialog.show(AddFeedbackActivity.this, "", "Sending...", true, false);

        APIClient.saveDraft(
                Utils.getTextRequestBody(feedBack.getUsername()),
                Utils.getFileRequestBody(feedBack.getPic1(), "pic1"),
                Utils.getFileRequestBody(feedBack.getPic2(), "pic2"),
                Utils.getFileRequestBody(feedBack.getPic3(), "pic3"),
                Utils.getTextRequestBody(feedBack.getTitle()),
                Utils.getTextRequestBody(feedBack.getFeedback()),
                Utils.getTextRequestBody(feedBack.getBlockno()),
                Utils.getTextRequestBody(feedBack.getStorey()),
                Utils.getTextRequestBody(feedBack.getSourcecode()),
                Utils.getTextRequestBody(feedBack.getSourceremarks()),
                Utils.getTextRequestBody(feedBack.getDateseen()),
                Utils.getTextRequestBody(feedBack.getTimeseen()),
                Utils.getTextRequestBody(feedBack.getLocation())
        );
    }

    private void sendFeedback() {
        pd = ProgressDialog.show(AddFeedbackActivity.this, "", "Sending...", true, false);

        APIClient.sendFeedback(
                Utils.getTextRequestBody(feedBack.getUsername()),
                Utils.getFileRequestBody(feedBack.getPic1(), "pic1"),
                Utils.getFileRequestBody(feedBack.getPic2(), "pic2"),
                Utils.getFileRequestBody(feedBack.getPic3(), "pic3"),
                Utils.getTextRequestBody(feedBack.getTitle()),
                Utils.getTextRequestBody(feedBack.getFeedback()),
                Utils.getTextRequestBody(feedBack.getBlockno()),
                Utils.getTextRequestBody(feedBack.getStorey()),
                Utils.getTextRequestBody(feedBack.getSourcecode()),
                Utils.getTextRequestBody(feedBack.getSourceremarks()),
                Utils.getTextRequestBody(feedBack.getDateseen()),
                Utils.getTextRequestBody(feedBack.getTimeseen()),
                Utils.getTextRequestBody(feedBack.getLocation()),
                Utils.getTextRequestBody(feedBack.getCategory()),
                Utils.getTextRequestBody(feedBack.getSourcetype())
        );
    }

    private void setFeedbackData() {

        feedBack.setUsername(App.User_Name);
        feedBack.setTitle(mTitle.getText().toString());

        feedBack.setSourceremarks(mRemarks.getText().toString());
        feedBack.setCategory(mCategory.getSelectedItem().toString());
        feedBack.setSourcecode(sourcesAdapter.getItem(mSource.getSelectedItemPosition()).getSourcecode());


        feedBack.setSourcetype("Condominium");


        Date dateSeen = Utils.convertDateStringToDate(mDate.getText().toString(), "dd/MM/yyyy");
        feedBack.setDateseen(Utils.convertDateToString(dateSeen, "yyyyMMdd"));

        feedBack.setTimeseen(mTime.getText().toString());
        feedBack.setLocation(mLocation.getText().toString());


//        feedBack.setBlockno(mBlock.getSelectedItem().toString());
//        feedBack.setStorey(mStorey.getSelectedItem().toString());

        feedBack.setBlockno(mBlock.getText().toString());
        feedBack.setStorey(mStorey.getText().toString());

    }

    private boolean valid() {


        if (Utils.isNullOrEmpty(mTitle.getText().toString())) {
            mTitle.setError("Title should not be blank.");
            mTitle.setFocusable(true);
            return false;
        }

        if (Utils.isNullOrEmpty(mRemarks.getText().toString())) {
            mRemarks.setError("Remarks should not be blank.");
            mRemarks.setFocusable(true);
            return false;
        }


        if (Utils.isNullOrEmpty(mBlock.getText().toString())) {
            mBlock.setError("Block number should not be blank.");
            mBlock.setFocusable(true);
            return false;
        }


        if (!TextUtils.isDigitsOnly(mBlock.getText())) {
            mBlock.setError("Block number should only be numeric.");
            mBlock.setFocusable(true);
            return false;
        }

        if (mBlock.getText().length() > 4) {
            mBlock.setError("Invalid block number");
            mBlock.setFocusable(true);
            return false;
        }


        if (Utils.isNullOrEmpty(mStorey.getText().toString())) {
            mStorey.setError("Storey number should not be blank.");
            mStorey.setFocusable(true);
            return false;
        }

        if (!TextUtils.isDigitsOnly(mStorey.getText())) {
            if (!mStorey.getText().toString().equalsIgnoreCase("B1")) {
                mStorey.setError("Storey number should only be numeric or B1");
                mStorey.setFocusable(true);
                return false;
            }
        }

        if (mStorey.getText() != "B1" && mBlock.getText().length() > 4) {
            mStorey.setError("Invalid storey number");
            mStorey.setFocusable(true);
            return false;
        }


        return true;
    }


    @OnClick(R.id.btn_save)
    public void saveDraftOnClick() {
        isDraftSave = true;
        if (valid()) {
            setFeedbackData();

            if (isOnline())
                saveDraft();
        }
    }

    @OnClick(R.id.btn_send)
    public void sendOnClick() {
        isDraftSave = false;
        if (valid()) {
            setFeedbackData();

            if (isOnline())
                sendFeedback();
        }
    }


    private boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

    private void clearAllFields() {

        feedBack = new FeedbackReq();


        mPhoto1.setImageResource(R.mipmap.ic_camera_alt_black_48dp);
        mPhoto2.setImageResource(R.mipmap.ic_camera_alt_black_48dp);
        mPhoto3.setImageResource(R.mipmap.ic_camera_alt_black_48dp);

        mTitle.setText("");
        mRemarks.setText("");
        mCategory.setSelection(0);
        mRemarks.setText("");
        mSource.setSelection(0);
        mLocation.setText("");
        mDate.setText("");
        mTime.setText("");
        mBlock.setText("");
        mStorey.setText("");

//        mBlock.setSelection(0);
//        mStorey.setSelection(0);

    }

    @Subscribe
    public void onEvent(FeedbackRes res) {
        if (res.getStatus().equalsIgnoreCase("SUCCESS")) {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }

            String message;

            if (isDraftSave)

                message = " Your feed back has been saved to draft.\n Your reference Feedback #" + res.getID() + ". \n Would you like to submit another?";
            else
                message = " Your feedback has been sent.\n Your reference Feedback #" + res.getID() + ". \n Would you like to submit another?";


            final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(AddFeedbackActivity.this)
                    .setMessage(message)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                            clearAllFields();
                            mTitle.setFocusable(true);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .create();

            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }

    @Subscribe
    public void onEvent(Throwable t) {
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(AddFeedbackActivity.this)
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

    @OnClick(R.id.location_delete)
    public void LocationDeleteClick() {
        mLocation.setText("");
    }

    @OnClick(R.id.title_delete)
    public void TitleDeleteClick() {
        mTitle.setText("");
    }

    @OnClick(R.id.remarks_delete)
    public void RemarksDeleteClick() {
        mRemarks.setText("");
    }

    @OnClick(R.id.photo1_delete)
    public void Photo1DeleteClick() {
        feedBack.setPic1("");
        mPhoto1.setImageResource(R.mipmap.ic_camera_alt_black_48dp);
    }

    @OnClick(R.id.photo2_delete)
    public void Photo2DeleteClick() {
        feedBack.setPic2("");
        mPhoto2.setImageResource(R.mipmap.ic_camera_alt_black_48dp);
    }

    @OnClick(R.id.photo2_delete)
    public void Photo3DeleteClick() {
        feedBack.setPic3("");
        mPhoto3.setImageResource(R.mipmap.ic_camera_alt_black_48dp);
    }

}
