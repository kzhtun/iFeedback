package com.info121.ifeedback.models;

import java.util.UUID;

public class FeedbackReq {

    String uuid;
    String username;
    String pic1;
    String pic2;
    String pic3;
    String title;
    String feedback;
    String blockno;
    String storey;
    String sourcecode;
    String sourceremarks;
    String dateseen;
    String timeseen;
    String location;
    String category;
    String sourcetype;

    public FeedbackReq() {
        this.uuid = UUID.randomUUID().toString();
        this.username = "";
        this.pic1 = "";
        this.pic2 = "";
        this.pic3 = "";
        this.title = "";
        this.feedback = "";
        this.blockno = "";
        this.storey = "";
        this.sourcecode = "";
        this.dateseen = "";
        this.timeseen = "";
        this.location = "";
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getBlockno() {
        return blockno;
    }

    public void setBlockno(String blockno) {
        this.blockno = blockno;
    }

    public String getStorey() {
        return storey;
    }

    public void setStorey(String storey) {
        this.storey = storey;
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

    public String getDateseen() {
        return dateseen;
    }

    public void setDateseen(String dateseen) {
        this.dateseen = dateseen;
    }

    public String getTimeseen() {
        return timeseen;
    }

    public void setTimeseen(String timeseen) {
        this.timeseen = timeseen;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSourcetype() {
        return sourcetype;
    }

    public void setSourcetype(String sourcetype) {
        this.sourcetype = sourcetype;
    }
}
