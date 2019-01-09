package com.info121.ifeedback.models;

public class RegisterRes {

    private String Status;
    private String profilecode;
    private String username;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getProfilecode() {
        return profilecode;
    }

    public void setProfilecode(String profilecode) {
        this.profilecode = profilecode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
