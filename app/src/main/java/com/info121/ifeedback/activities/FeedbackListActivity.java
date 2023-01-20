package com.info121.ifeedback.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.info121.ifeedback.AbstractActivity;
import com.info121.ifeedback.App;
import com.info121.ifeedback.R;
import com.info121.ifeedback.adapters.FeedbackAdapter;
import com.info121.ifeedback.api.APIClient;
import com.info121.ifeedback.models.Feedback;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedbackListActivity extends AbstractActivity {

    public static final String VIEW_TYPE = "VIEW_TYPE";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.rv_feedback)
    RecyclerView mRecyclerView;

    List<Feedback> sentFeedbackList;

    FeedbackAdapter sentAdapter;

    Boolean isSentFeedback = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_list);

        ButterKnife.bind(this);



        if (getIntent().getStringExtra(VIEW_TYPE).equals("FEEDBACK")) {
            APIClient.GetFeedbacks(App.User_Name);
            isSentFeedback = true;
        } else {
            APIClient.GetDrafts(App.User_Name);
            isSentFeedback = false;
        }


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadList() {
        if (isSentFeedback)
            mToolbar.setTitle("Sent Feedback List");
        else
            mToolbar.setTitle("Draft Feedback List");

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(FeedbackListActivity.this, LinearLayoutManager.VERTICAL, false));
        sentAdapter = new FeedbackAdapter(sentFeedbackList, FeedbackListActivity.this);
        mRecyclerView.setAdapter(sentAdapter);
    }

    @Subscribe
    public void onEvent(List<?> res) {
        sentFeedbackList = new ArrayList<>();

        if (res.get(0) instanceof Feedback) {

            Feedback  fb =(Feedback) res.get(0);
            if (Integer.parseInt(fb.getId()) > 0) {
                sentFeedbackList = (List<Feedback>) res;
            }

            loadList();
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
