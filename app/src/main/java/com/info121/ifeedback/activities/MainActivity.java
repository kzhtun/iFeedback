package com.info121.ifeedback.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.info121.ifeedback.App;
import com.info121.ifeedback.R;
import com.info121.ifeedback.utilities.Utils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
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

    MenuItem mHome,mRegister, mCreat;
    TextView mSourceName, mNavWelcome, mNavDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);


        mHome = mNavigationView.getMenu().findItem(R.id.home);
        mRegister = mNavigationView.getMenu().findItem(R.id.register);
        mCreat = mNavigationView.getMenu().findItem(R.id.create_fb);
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
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
                return false;
            }
        });


        // Create Feedback
        mCreat.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
                return false;
            }
        });


    }
}
