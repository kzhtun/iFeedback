package com.info121.ifeedback.models;

public class Storey {

    private String block;
    private String storey;

    public Storey() {
    }

    public Storey(String block, String storey) {
        this.block = block;
        this.storey = storey;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getStorey() {
        return storey;
    }

    public void setStorey(String storey) {
        this.storey = storey;
    }

    @Override
    public String toString() {
        return storey;
    }

}
