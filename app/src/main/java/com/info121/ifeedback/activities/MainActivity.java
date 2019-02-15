package com.info121.ifeedback.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.info121.ifeedback.App;
import com.info121.ifeedback.R;
import com.info121.ifeedback.api.APIClient;
import com.info121.ifeedback.models.UserProfileRes;
import com.info121.ifeedback.utilities.Utils;

import net.hockeyapp.android.metrics.model.User;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;

public class MainActivity extends AppCompatActivity  {
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    @BindView(R.id.drawer)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    @BindView(R.id.date_time)
    TextView mDateTime;

    @BindView(R.id.welcome)
    TextView mWelcomeMsg;

    MenuItem mHome,mRegister, mCreat, mViewFB, mViewDraft, mProfile, mLogout;
    TextView mSourceName, mNavWelcome, mNavDateTime;

    Context mContext;
     LocationGooglePlayServicesProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);


        mHome = mNavigationView.getMenu().findItem(R.id.home);
        mRegister = mNavigationView.getMenu().findItem(R.id.register);
        mCreat = mNavigationView.getMenu().findItem(R.id.create_fb);
        mViewFB = mNavigationView.getMenu().findItem(R.id.view_fb);
        mViewDraft = mNavigationView.getMenu().findItem(R.id.view_draft);
        mProfile = mNavigationView.getMenu().findItem(R.id.profile);
        mLogout = mNavigationView.getMenu().findItem(R.id.logout);

        mNavWelcome = mNavigationView.getHeaderView(0).findViewById(R.id.welcome);
        mSourceName =  mNavigationView.getHeaderView(0).findViewById(R.id.source_name);
        mNavDateTime = mNavigationView.getHeaderView(0).findViewById(R.id.date_time);


        mSourceName.setText(App.Source_Name);
        mWelcomeMsg.setText("Welcome " + App.User_Name );

        // mToolbar.setTitle("HOME");
        setSupportActionBar(mToolbar);

        initializeEvents();

//        Drawable mainDrawable = Utils.getDrawable("HomePhoto.jpg");
//
//        if(mainDrawable != null){
//            mHomeBackground.setBackground(mainDrawable);
//        }

        final Handler timer = new Handler(getMainLooper());
        timer.postDelayed(new Runnable() {
            @Override
            public void run() {

                String dateString = Utils.convertDateToString(Calendar.getInstance().getTime(), "dd-MMM-yyyy hh:mm:ss a");

                mDateTime.setText(dateString.toString());
                mNavDateTime.setText(dateString.toString());
                timer.postDelayed(this, 1000);
            }
        }, 1000);


        initializeEvents();

        //startLocation();

        if (!App.User_Name.isEmpty())
            mRegister.setVisible(false);
       // else
          //  APIClient.UpdateTokenID(App.DEVICE_ID, App.FCM_TOKEN);

        // update token id
      //  String FCM_TOKEN = FirebaseInstanceId.getInstance().getToken();
      //  Log.e("Main ", "FCN Token" +  FCM_TOKEN);
       // APIClient.UpdateTokenID(Utils.getDeviceID(mContext), FCM_TOKEN);


    }

    @OnClick(R.id.btn_send_feedback)
    public void btnSendFeedbackOnClick(){
        Intent intent = new Intent(MainActivity.this, AddFeedbackActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_feed_back_list)
    public void btnFeedbackListOnClick(){
        Intent intent = new Intent(MainActivity.this, FeedbackListActivity.class);
        intent.putExtra(FeedbackListActivity.VIEW_TYPE, "FEEDBACK");
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void initializeEvents() {
        final ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();


        // Home
        mHome.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        // Register
        mRegister.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(intent);
                return false;
            }
        });


        // Create Feedback
        mCreat.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, AddFeedbackActivity.class);
                startActivity(intent);
                return false;
            }
        });


        // Feedback List
        mViewFB.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, FeedbackListActivity.class);
                intent.putExtra(FeedbackListActivity.VIEW_TYPE, "FEEDBACK");
                startActivity(intent);
                return false;
            }
        });


        // Feedback List
        mViewDraft.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, FeedbackListActivity.class);
                intent.putExtra(FeedbackListActivity.VIEW_TYPE, "DRAFT");
                startActivity(intent);
                return false;
            }
        });

        // Profile
        mProfile.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(intent);
                return false;
            }
        });

        mLogout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                return false;
            }
        });

    }




}
