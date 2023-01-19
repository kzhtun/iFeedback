package com.info121.ifeedback.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.info121.ifeedback.R;
import com.info121.ifeedback.activities.ViewFeedbackActivity;
import com.info121.ifeedback.models.Feedback;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {
    List<Feedback> mFeedbacks;
    Context mContext;


    public FeedbackAdapter(List<Feedback> mFeedbacks, Context mContext) {
        this.mFeedbacks = mFeedbacks;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        // Inflate the custom layout
        View feedbackView = inflater.inflate(R.layout.card_feedback, parent, false);

        // Return a new holder
        return new ViewHolder(feedbackView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
     //   holder.srno.setText(String.valueOf(i + 1));
        holder.srno.setText(mFeedbacks.get(i).getId());

//        holder.title.setText(mFeedbacks.get(i).getTitle());
//        holder.status.setText("Status : " + mFeedbacks.get(i).getStatus());
//        holder.date.setText("Date Sent : " + mFeedbacks.get(i).getSentat());
//        holder.to.setText("To : " + mFeedbacks.get(i).getSourcecode());

        holder.title.setText(mFeedbacks.get(i).getTitle());
        holder.status.setText( mFeedbacks.get(i).getStatus());
        holder.date.setText(mFeedbacks.get(i).getSentat());
        holder.to.setText(mFeedbacks.get(i).getSourcecode());

        final Intent intent = new Intent(mContext, ViewFeedbackActivity.class);

        intent.putExtra(ViewFeedbackActivity.ID, mFeedbacks.get(i).getId());

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFeedbacks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, status, date, to, srno;
        CardView root;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_title);
            status = itemView.findViewById(R.id.tv_status);
            date = itemView.findViewById(R.id.tv_date);
            to = itemView.findViewById(R.id.tv_to);
            srno = itemView.findViewById(R.id.tv_serial);
            root =  itemView.findViewById(R.id.root_layout);
        }
    }
}
