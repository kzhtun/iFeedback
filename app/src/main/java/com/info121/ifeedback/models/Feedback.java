
package com.info121.ifeedback.models;


public class Feedback {
    private String id;
    private String sentat;
    private String sourcecode;
    private String status;
    private String title;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
