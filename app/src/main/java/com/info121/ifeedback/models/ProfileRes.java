
package com.info121.ifeedback.models;


import com.google.gson.annotations.SerializedName;


public class ProfileRes {

    @SerializedName("ID")
    private Long mID;
    @SerializedName("msg")
    private String mMsg;
    @SerializedName("Status")
    private String mStatus;

    public Long getID() {
        return mID;
    }

    public void setID(Long iD) {
        mID = iD;
    }

    public String getMsg() {
        return mMsg;
    }

    public void setMsg(String msg) {
        mMsg = msg;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
