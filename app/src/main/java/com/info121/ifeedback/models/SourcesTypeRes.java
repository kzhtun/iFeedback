
package com.info121.ifeedback.models;

public class SourcesTypeRes {

    private String sourcetype;

    public SourcesTypeRes(String sourcetype) {
        this.sourcetype = sourcetype;
    }

    public String getSourcetype() {
        return sourcetype;
    }

    public void setSourcetype(String sourcetype) {
        this.sourcetype = sourcetype;
    }

    @Override
    public String toString() {
        return sourcetype;
    }
}
