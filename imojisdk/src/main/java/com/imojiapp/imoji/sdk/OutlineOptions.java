package com.imojiapp.imoji.sdk;

/**
 * Created by sajjadtabib on 4/9/15.
 */
public class OutlineOptions {
    public int color;
    public int outlineRadius = Integer.MAX_VALUE;
    public int shadowRadius = Integer.MAX_VALUE;
    public int shadowOffsetX = Integer.MAX_VALUE;
    public int shadowOffsetY = Integer.MAX_VALUE; //Integer.MAX_VALUE indicates it's uninitialized
    public int shadowColor = 0x80000000;

    @Override
    public String toString() {
        return super.toString();
    }
}
