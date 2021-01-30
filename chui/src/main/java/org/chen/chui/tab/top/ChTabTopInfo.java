package org.chen.chui.tab.top;

import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

public class ChTabTopInfo<Color> {

    public enum TabType{
        BITMAP, TEXT
    }

    public Class<? extends Fragment> fragment;
    public String name;
    public Bitmap defaultBitMap;
    public Bitmap selectedBitMap;


    public Color defaultColor;
    public Color tintColor;
    public TabType tabType;

    public ChTabTopInfo(String name, Bitmap defaultBitMap, Bitmap selectedBitMap){
        this.name = name;
        this.defaultBitMap = defaultBitMap;
        this.selectedBitMap = selectedBitMap;
        this.tabType = TabType.BITMAP;
    }

    public ChTabTopInfo(String name, Color defaultColor, Color tintColor){
        this.name = name;
        this.defaultColor = defaultColor;
        this.tintColor = tintColor;
        this.tabType = TabType.TEXT;
    }



}
