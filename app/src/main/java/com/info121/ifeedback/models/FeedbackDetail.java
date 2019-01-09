package com.info121.ifeedback.models;

import com.google.gson.annotations.Expose;


public class FeedbackDetail {
    @Expose
    private Long active;
    @Expose
    private String blockno;
    @Expose
    private String dateseen;
    @Expose
    private String feedback;
    @Expose
    private String feedbackcategory;
    @Expose
    private Long id;
    @Expose
    private String location;
    @Expose
    private String pic1;
    @Expose
    private String pic2;
    @Expose
    private String pic3;
    @Expose
    private String sentat;
    @Expose
    private String sourcecode;
    @Expose
    private String sourceremarks;
    @Expose
    private String status;
    @Expose
    private String storey;
    @Expose
    private String timeseen;
    @Expose
    private String title;
    @Expose
    private Long userdeleted;

    public Long getActive() {
        return active;
    }

    public void setActive(Long active) {
        this.active = active;
    }

    public String getBlockno() {
        return blockno;
    }

    public void setBlockno(String blockno) {
        this.blockno = blockno;
    }

    public String getDateseen() {
        return dateseen;
    }

    public void setDateseen(String dateseen) {
        this.dateseen = dateseen;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFeedbackcategory() {
        return feedbackcategory;
    }

    public void setFeedbackcategory(String feedbackcategory) {
        this.feedbackcategory = feedbackcategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public String getPic3() {
        return pic3;
    }

    public void setPic3(String pic3) {
        this.pic3 = pic3;
    }

    public String getSentat() {
        return sentat;
    }

    public void setSentat(String sentat) {
        this.sentat = sentat;
    }

    public String getSourcecode() {
        return sourcecode;
    }

    public void setSourcecode(String sourcecode) {
        this.sourcecode = sourcecode;
    }

    public String getSourceremarks() {
        return sourceremarks;
    }

    public void setSourceremarks(String sourceremarks) {
        this.sourceremarks = sourceremarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStorey() {
        return storey;
    }

    public void setStorey(String storey) {
        this.storey = storey;
    }

    public String getTimeseen() {
        return timeseen;
    }

    public void setTimeseen(String timeseen) {
        this.timeseen = timeseen;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getUserdeleted() {
        return userdeleted;
    }

    public void setUserdeleted(Long userdeleted) {
        this.userdeleted = userdeleted;
    }
}
