package org.chen.chui.tab.bottom;

import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

public class ChTabBottomInfo<Color> {

    public enum TabType{
        BITMAP,ICON
    }

    public Class<? extends Fragment> fragment;
    public String name;
    public Bitmap defaultBitMap;
    public Bitmap selectedBitMap;
    public String iconFont;

    public String defaultIconName;
    public String selectedIconName;
    public Color defaultColor;
    public Color tintColor;
    public TabType tabType;

    public ChTabBottomInfo(String name,Bitmap defaultBitMap,Bitmap selectedBitMap){
        this.name = name;
        this.defaultBitMap = defaultBitMap;
        this.selectedBitMap = selectedBitMap;
        this.tabType = TabType.BITMAP;
    }

    public ChTabBottomInfo(String name,String iconFont,String defaultIconName,String selectedIconName,Color defaultColor,Color tintColor){
        this.name = name;
        this.iconFont = iconFont;
        this.defaultIconName = defaultIconName;
        this.selectedIconName = selectedIconName;
        this.defaultColor = defaultColor;
        this.tintColor = tintColor;
        this.tabType = TabType.ICON;
    }



}
