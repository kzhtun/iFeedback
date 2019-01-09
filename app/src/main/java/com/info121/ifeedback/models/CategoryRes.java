package com.info121.ifeedback.models;

public class CategoryRes {
    private String feedbackcategory;

    public CategoryRes(String feedbackcategory) {
        this.feedbackcategory = feedbackcategory;
    }

    public String getFeedbackcategory() {
        return feedbackcategory;
    }

    public void setFeedbackcategory(String feedbackcategory) {
        this.feedbackcategory = feedbackcategory;
    }

    @Override
    public String toString() {
        return feedbackcategory;
    }
}
