package com.info121.ifeedback.models;

public class Block {

    private String blockno;

    public Block() {

    }

    public Block(String block) {
        this.blockno = block;
    }

    public String getBlock() {
        return blockno;
    }

    public void setBlock(String block) {
        this.blockno = block;
    }

    @Override
    public String toString() {
        return blockno;
    }

}
