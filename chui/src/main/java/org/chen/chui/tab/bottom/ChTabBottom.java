package org.chen.chui.tab.bottom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import org.chen.chui.R;
import org.chen.chui.tab.common.IChTab;

public class ChTabBottom extends RelativeLayout implements IChTab<ChTabBottomInfo<?>> {

    private ChTabBottomInfo<?> tabInfo;
    private ImageView tabImageView;
    private TextView tabIconView;
    private TextView tabNameView;

    public ChTabBottom(Context context) {
        this(context, null);
    }

    public ChTabBottom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChTabBottom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.ch_tab_bottom, this);
        tabImageView = findViewById(R.id.iv_image);
        tabIconView = findViewById(R.id.tv_icon);
        tabNameView = findViewById(R.id.tv_name);
    }


    @Override
    public void setChTabInfo(@NonNull ChTabBottomInfo data) {
        this.tabInfo = data;
        inflateInfo(false, true);
    }

    private void inflateInfo(boolean selected, boolean init) {

        if (tabInfo.tabType == ChTabBottomInfo.TabType.ICON) {
            if (init) {
                tabImageView.setVisibility(GONE);
                tabIconView.setVisibility(VISIBLE);
                Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), tabInfo.iconFont);
                tabIconView.setTypeface(typeface);
                if (!TextUtils.isEmpty(tabInfo.name)) {
                    tabNameView.setText(tabInfo.name);
                }
            }
            if (selected) {
                Log.d("chen", "inflateInfo: ");
                tabIconView.setText(TextUtils.isEmpty(tabInfo.selectedIconName) ? tabInfo.defaultIconName : tabInfo.selectedIconName);
                tabIconView.setTextColor(getTextColor(tabInfo.tintColor));
                tabNameView.setTextColor(getTextColor(tabInfo.tintColor));
            } else {
                tabIconView.setText(tabInfo.defaultIconName);
                tabIconView.setTextColor(getTextColor(tabInfo.defaultColor));
                tabNameView.setTextColor(getTextColor(tabInfo.defaultColor));
            }
        } else if(tabInfo.tabType == ChTabBottomInfo.TabType.BITMAP){
            if(init){
                tabImageView.setVisibility(VISIBLE);
                tabIconView.setVisibility(GONE);
                if(!TextUtils.isEmpty(tabInfo.name)){
                    tabNameView.setText(tabInfo.name);
                }
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
    public void onTabSelectedChange(int index,  ChTabBottomInfo<?> prevInfo, @NonNull ChTabBottomInfo<?> nextInfo) {
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

    public ChTabBottomInfo<?> getTabInfo() {
        return tabInfo;
    }

    public ImageView getTabImageView() {
        return tabImageView;
    }

    public TextView getTabIconView() {
        return tabIconView;
    }

    public TextView getTabNameView() {
        return tabNameView;
    }

}
