package org.chen.chui.tab.top;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import org.chen.chui.R;
import org.chen.chui.tab.common.IChTab;

public class ChTabTop extends RelativeLayout implements IChTab<ChTabTopInfo<?>> {

    private ChTabTopInfo<?> tabInfo;
    private ImageView tabImageView;
    private TextView tabNameView;
    private View indicator;

    public ChTabTop(Context context) {
        this(context, null);
    }

    public ChTabTop(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChTabTop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.ch_tab_top, this);
        tabImageView = findViewById(R.id.iv_image);
        tabNameView = findViewById(R.id.tv_name);
        indicator = findViewById(R.id.tab_top_indicator);
    }


    @Override
    public void setChTabInfo(@NonNull ChTabTopInfo data) {
        this.tabInfo = data;
        inflateInfo(false, true);
    }

    private void inflateInfo(boolean selected, boolean init) {

        if (tabInfo.tabType == ChTabTopInfo.TabType.TEXT) {
            if (init) {
                tabImageView.setVisibility(GONE);
                tabNameView.setVisibility(VISIBLE);
                if (!TextUtils.isEmpty(tabInfo.name)) {
                    tabNameView.setText(tabInfo.name);
                }
            }
            if (selected) {
                Log.d("chen", "inflateInfo: ");
                indicator.setVisibility(VISIBLE);
                tabNameView.setTextColor(getTextColor(tabInfo.tintColor));
            } else {
                tabNameView.setTextColor(getTextColor(tabInfo.defaultColor));
                indicator.setVisibility(INVISIBLE);
            }
        } else if(tabInfo.tabType == ChTabTopInfo.TabType.BITMAP){
            if(init){
                tabImageView.setVisibility(VISIBLE);
                tabNameView.setVisibility(GONE);
            }
            if(selected){
                tabImageView.setImageBitmap(tabInfo.selectedBitMap);
            }else {
                tabImageView.setImageBitmap(tabInfo.defaultBitMap);
            }
        }
    }

    @Override
    public void resetHeight(int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = height;
        setLayoutParams(layoutParams);
        getTabNameView().setVisibility(GONE);
    }

    @Override
    public void onTabSelectedChange(int index, ChTabTopInfo<?> prevInfo, @NonNull ChTabTopInfo<?> nextInfo) {
        //重复选额
        if(prevInfo == null && tabInfo == nextInfo){
            inflateInfo(true,false);
        }

        if(prevInfo != tabInfo && nextInfo!= tabInfo || prevInfo == nextInfo){
            return;
        }
        Log.d("chen", "onTabSelectedChange: ");
        if(prevInfo == tabInfo){
            inflateInfo(false,false);
        }else {
            inflateInfo(true,false);
        }
    }

    @ColorInt
    private int getTextColor(Object color) {
        if (color instanceof String) {
            return Color.parseColor((String) color);
        } else {
            return (int) color;
        }
    }

    public ChTabTopInfo<?> getTabInfo() {
        return tabInfo;
    }

    public ImageView getTabImageView() {
        return tabImageView;
    }

    public TextView getTabNameView() {
        return tabNameView;
    }

}
