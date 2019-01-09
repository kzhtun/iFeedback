
package com.info121.ifeedback.models;


import com.orm.SugarRecord;

public class SourcesRes extends SugarRecord{

    private String sourcecode;
    private String sourcename;
    private String sourcetype;

    public SourcesRes() {
    }

    public SourcesRes(String sourcecode, String sourcename, String sourcetype) {
        this.sourcecode = sourcecode;
        this.sourcename = sourcename;
        this.sourcetype = sourcetype;
    }

    public String getSourcecode() {
        return sourcecode;
    }

    public void setSourcecode(String sourcecode) {
        this.sourcecode = sourcecode;
    }

    public String getSourcename() {
        return sourcename;
    }

    public void setSourcename(String sourcename) {
        this.sourcename = sourcename;
    }

    public String getSourcetype() {
        return sourcetype;
    }

    public void setSourcetype(String sourcetype) {
        this.sourcetype = sourcetype;
    }

    @Override
    public String toString() {
        return sourcename;
    }
}
