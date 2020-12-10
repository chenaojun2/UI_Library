package org.chen.chui.tab.bottom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.chen.chui.R;
import org.chen.chui.tab.common.IChTabLayout;
import org.chen.cibrary.util.ChDisplayUtil;
import org.chen.cibrary.util.ChViewUtil;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChTabBottomLayout extends FrameLayout implements IChTabLayout<ChTabBottom, ChTabBottomInfo<?>> {
    private static final String TAG_TAB_BOTTOM = "TAG_TAB_BOTTOM";

    private List<OnTabSelectedListener<ChTabBottomInfo<?>>> tabSelectedListeners = new ArrayList<>();
    private ChTabBottomInfo<?> selectedInfo;
    private float bottomAlpha = 1f;
    private float tabBottomHeight = 50;
    private float bottomLineHeight = 0.5f;
    private String bottomLineColor = "#dfe0e1";
    private List<ChTabBottomInfo<?>> infoList;

    public ChTabBottomLayout(@NonNull Context context) {
        super(context);
    }

    public ChTabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChTabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public ChTabBottom findTab(@NonNull ChTabBottomInfo<?> info) {
        ViewGroup ll = findViewWithTag(TAG_TAB_BOTTOM);
        for(int i = 0 ; i < ll.getChildCount() ; i++){
            View child = ll.getChildAt(i);
            if(child instanceof ChTabBottom){
                ChTabBottom tab = (ChTabBottom) child;
                if(tab.getTabInfo() == info)
                    return tab;
            }
        }
        return null;
    }

    @Override
    public void addTabSelectedChangeListener(OnTabSelectedListener<ChTabBottomInfo<?>> listener) {
        tabSelectedListeners.add(listener);
    }

    @Override
    public void defaultSelected(@NonNull ChTabBottomInfo<?> defaultInfo) {
        onSelected(defaultInfo);
    }

    @Override
    public void inflateInfo(@NonNull List<ChTabBottomInfo<?>> infoList) {
        if (infoList.isEmpty()) {
            return;
        }
        this.infoList = infoList;
        //防止重复添加，移除之前添加的View;
        for (int i = getChildCount() - 1; i > 0; i++) {
            removeViewAt(i);
        }
        addBackground();

        Iterator<OnTabSelectedListener<ChTabBottomInfo<?>>> iterator = tabSelectedListeners.iterator();
        while(iterator.hasNext()){
            if(iterator.next() instanceof ChTabBottom){
                iterator.remove();
            }
        }

        int height = ChDisplayUtil.dp2px(tabBottomHeight,getResources());
        FrameLayout ll = new FrameLayout(getContext());
        ll.setTag(TAG_TAB_BOTTOM);
        int width = ChDisplayUtil.getDisplayWidthInPx(getContext())/infoList.size();
        for(int i = 0; i < infoList.size(); i++){
            ChTabBottomInfo<?> info = infoList.get(i);
            //为何不用线性布局，动态改变child大小后Gravity失效
            LayoutParams params = new LayoutParams(width,height);
            params.gravity = Gravity.BOTTOM;
            params.leftMargin = i * width;
            ChTabBottom tabBottom = new ChTabBottom(getContext());
            tabSelectedListeners.add(tabBottom);
            tabBottom.setChTabInfo(info);
            ll.addView(tabBottom,params);
            tabBottom.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("chen", "onClick: ");
                    onSelected(info);
                }
            });
        }
        LayoutParams flParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        flParams.gravity = Gravity.BOTTOM;

        addBottomLine();
        addView(ll,flParams);
        fixContentView();
    }

    private void addBottomLine(){
          View bottomLine = new View(getContext());
          bottomLine.setBackgroundColor(Color.parseColor(bottomLineColor));

          LayoutParams bottomLineParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ChDisplayUtil.dp2px(bottomLineHeight,getResources()));
          bottomLineParams.gravity = Gravity.BOTTOM;
          bottomLineParams.bottomMargin = ChDisplayUtil.dp2px(tabBottomHeight - bottomLineHeight,getResources());
          addView(bottomLine,bottomLineParams);
          bottomLine.setAlpha(bottomAlpha);
    }

    private void onSelected(@NonNull ChTabBottomInfo<?> nextInfo){
        for(OnTabSelectedListener<ChTabBottomInfo<?>> listener : tabSelectedListeners){
            Log.d("chen", "onSelected: ");
            listener.onTabSelectedChange(infoList.indexOf(nextInfo),selectedInfo,nextInfo);
        }
        this.selectedInfo = nextInfo;
    }

    private void addBackground() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.ch_bottom_layout_bg,null);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ChDisplayUtil.dp2px(tabBottomHeight,getResources()));
        params.gravity = Gravity.BOTTOM;
        addView(view,params);
        view.setAlpha(bottomAlpha);

    }

    /**
     * 修复底部padding
     * */
    private void fixContentView(){

        if(!((getChildAt(0))instanceof ViewGroup)){
            return;
        }
        ViewGroup rootView = (ViewGroup) getChildAt(0);
        ViewGroup targetView = ChViewUtil.findTypeView(rootView, RecyclerView.class);
        if(targetView == null){
            targetView = ChViewUtil.findTypeView(rootView, ScrollView.class);
        }
        if(targetView == null){
            targetView = ChViewUtil.findTypeView(rootView, AbsListView.class);
        }
        if(targetView != null){
            targetView.setPadding(0,0,0,ChDisplayUtil.dp2px(tabBottomHeight,getResources()));
            targetView.setClipToPadding(false);
        }

    }

    public void setTabAlpha(float alpha) {
        this.bottomAlpha = alpha;
    }

    public void setTabHeight(float tabHeight) {
        this.tabBottomHeight = tabHeight;
    }

    public void setBottomLineHeight(float bottomLineHeight) {
        this.bottomLineHeight = bottomLineHeight;
    }

    public void setBottomLineColor(String bottomLineColor) {
        this.bottomLineColor = bottomLineColor;
    }
}
