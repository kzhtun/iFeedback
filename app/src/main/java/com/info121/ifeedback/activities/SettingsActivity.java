package com.info121.ifeedback.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.info121.ifeedback.R;
import com.info121.ifeedback.utilities.PrefDB;
import com.info121.ifeedback.utilities.Utils;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.et_current_ip)
    EditText mCurrentIP;

    @BindView(R.id.et_new_ip)
    EditText mNewIP;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    PrefDB prefDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);

        prefDB = new PrefDB(SettingsActivity.this);

        mToolbar.setTitle("Settings");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCurrentIP.setText(prefDB.getString("CURRENT_IP"));

        if (!Utils.isNullOrEmpty(prefDB.getString("CURRENT_IP"))) {
            finish();
            startActivity(new Intent(SettingsActivity.this, SplashActivity.class));
            return;
        }
    }


    @OnClick(R.id.btn_update)
    public void BtnUpdateOnClick() {
        prefDB.putString("CURRENT_IP", mNewIP.getText().toString());

        AlertDialog alertDialog = new AlertDialog.Builder(SettingsActivity.this)
                //     .setMessage("New IP has been saved. Data synchronization process may take some time.")
                .setMessage("New IP has been saved. Application will load necessary data.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        startActivity(new Intent(SettingsActivity.this, SplashActivity.class));

                    }
                }).create();

        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();

//            if (!Utils.isNullOrEmpty(getIntent().getStringExtra(ID))) {
//                EventBus.getDefault().post("VEHICLE_DATA_UPDATE");
//            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
