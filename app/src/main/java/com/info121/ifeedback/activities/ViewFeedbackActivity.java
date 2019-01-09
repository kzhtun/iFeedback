package com.info121.ifeedback.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.info121.ifeedback.AbstractActivity;
import com.info121.ifeedback.R;
import com.info121.ifeedback.api.APIClient;
import com.info121.ifeedback.models.FeedbackDetail;
import com.info121.ifeedback.utilities.Utils;

import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewFeedbackActivity extends AbstractActivity {
    public static final String ID = "ID";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.feedback)
    TextView mFeedback;

    @BindView(R.id.submit_to)
    TextView mSubmitTo;

    @BindView(R.id.location)
    TextView mLocation;

    @BindView(R.id.date)
    TextView mDate;

    @BindView(R.id.time)
    TextView mTime;

    @BindView(R.id.block)
    TextView mBlock;


    @BindView(R.id.storey)
    TextView mStorey;

    @BindView(R.id.status)
    TextView mStatus;

    @BindView(R.id.submit_date_time)
    TextView mSubmitDateTime;

    @BindView(R.id.remarks)
    TextView mRemarks;

//    @BindView(R.id.slider)
//    SliderLayout mDemoSlider;


    Context mContext;
    HashMap<String, String> url_maps = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feedback);

        ButterKnife.bind(this);

        initializeControls();

        APIClient.GetFeedbackDetail(getIntent().getStringExtra(ID));

    }

    private void initializeControls() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mContext = ViewFeedbackActivity.this;

        mToolbar.setTitle("Feedback Detail") ;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Subscribe
    public void onEvent(FeedbackDetail fb) {
        populateFeedbackDetail(fb);
    }

    private void populateFeedbackDetail(FeedbackDetail fb) {
        mToolbar.setTitle("Feedback Detail #" + fb.getId()) ;

        mTitle.setText(fb.getTitle());
        mFeedback.setText(fb.getFeedback());
        mSubmitTo.setText(fb.getSourcecode());
        mLocation.setText(fb.getLocation());

        Date d = Utils.convertDateStringToDate(fb.getDateseen(), "dd/MM/yyyy hh:mm:ss");

        mDate.setText(Utils.convertDateToString(d, "dd-MMM-yyyy"));
        mTime.setText(fb.getTimeseen());
        mBlock.setText(fb.getBlockno());
        mStorey.setText(fb.getStorey());
        mStatus.setText(fb.getStatus());
        mLocation.setText(fb.getLocation());
        mSubmitDateTime.setText(fb.getSentat());
        mRemarks.setText(fb.getSourceremarks());

//        if (!fb.getPic1().isEmpty())
//            url_maps.put("Photo 1", fb.getPic1());
//
//        if (!fb.getPic2().isEmpty())
//            url_maps.put("Photo 2", fb.getPic2());
//
//        if (!fb.getPic3().isEmpty())
//            url_maps.put("Photo 3", fb.getPic3());
//
//
//        for (String name : url_maps.keySet()) {
//            TextSliderView textSliderView = new TextSliderView(this);
//            // initialize a SliderLayout
//            textSliderView
//                    .description(name)
//                    .image(url_maps.get(name))
//                    .setScaleType(BaseSliderView.ScaleType.Fit);
//
//
//            //add your extra information
//            textSliderView.bundle(new Bundle());
//            textSliderView.getBundle()
//                    .putString("extra", name);
//
//            mDemoSlider.addSlider(textSliderView);
//        }
//        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
//        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
//        mDemoSlider.setDuration(4000);

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
