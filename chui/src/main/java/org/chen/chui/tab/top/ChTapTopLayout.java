package org.chen.chui.tab.top;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import org.chen.chui.tab.bottom.ChTabBottom;
import org.chen.chui.tab.bottom.ChTabBottomInfo;
import org.chen.chui.tab.common.IChTabLayout;
import org.chen.cibrary.util.ChDisplayUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChTapTopLayout extends HorizontalScrollView implements IChTabLayout<ChTabTop, ChTabTopInfo<?>> {

    private List<OnTabSelectedListener<ChTabTopInfo<?>>> tabSelectedChangeListeners = new ArrayList<>();
    private ChTabTopInfo<?> selectedInfo;
    private List<ChTabTopInfo<?>> infoList;
    private int tabWith;


    public ChTapTopLayout(Context context) {
        this(context, null);
    }

    public ChTapTopLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChTapTopLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setVerticalScrollBarEnabled(false);
    }

    @Override
    public ChTabTop findTab(@NonNull ChTabTopInfo info) {
        ViewGroup ll = getRootLayout(false);
        for (int i = 0; i < ll.getChildCount(); i++) {
            View child = ll.getChildAt(i);
            if (child instanceof ChTabTop) {
                ChTabTop tab = (ChTabTop) child;
                if (tab.getTabInfo() == info) {
                    return tab;
                }
            }
        }
        return null;
    }

    @Override
    public void defaultSelected(@NonNull ChTabTopInfo defaultInfo) {
        onSelected(defaultInfo);
    }

    @Override
    public void addTabSelectedChangeListener(OnTabSelectedListener<ChTabTopInfo<?>> listener) {
        tabSelectedChangeListeners.add(listener);
    }

    @Override
    public void inflateInfo(@NonNull List<ChTabTopInfo<?>> infoList) {
        if (infoList.isEmpty()) {
            return;
        }
        this.infoList = infoList;
        //防止重复添加，移除之前添加的View;
        LinearLayout linearLayout = getRootLayout(true);
        selectedInfo = null;

        Iterator<OnTabSelectedListener<ChTabTopInfo<?>>> iterator = tabSelectedChangeListeners.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof ChTabTop) {
                iterator.remove();
            }
        }

        for (int i = 0; i < infoList.size(); i++) {
            ChTabTopInfo<?> info = infoList.get(i);
            ChTabTop tab = new ChTabTop(getContext());
            tabSelectedChangeListeners.add(tab);
            tab.setChTabInfo(info);
            linearLayout.addView(tab);
            tab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelected(info);
                }
            });
        }
    }

    private void onSelected(@NonNull ChTabTopInfo<?> nextInfo) {
        for (OnTabSelectedListener<ChTabTopInfo<?>> listener : tabSelectedChangeListeners) {
            Log.d("chen", "onSelected: ");
            listener.onTabSelectedChange(infoList.indexOf(nextInfo), selectedInfo, nextInfo);
        }
        this.selectedInfo = nextInfo;
        autoScroll(nextInfo);
    }

    private void autoScroll(ChTabTopInfo<?> nextInfo) {
        ChTabTop tabTop = findTab(nextInfo);
        if (tabTop == null) return;
        int index = infoList.indexOf(nextInfo);
        int scrollWidth;
        if (tabWith == 0) {
            tabWith = tabTop.getWidth();
        }
        int[] loc = new int[2];
        tabTop.getLocationInWindow(loc);
        if ((loc[0] + tabWith / 2) > ChDisplayUtil.getDisplayWidthInPx(getContext()) / 2) {
            scrollWidth = rangeScrollWidth(index, 2);
        } else {
            scrollWidth = rangeScrollWidth(index, -2);
        }
        scrollTo(getScrollX() + scrollWidth , 0);
    }

    private int rangeScrollWidth(int index, int range) {
        int scrollWidth = 0;
        for (int i = 0; i <=  Math.abs(range); i++) {
            int next;
            if (range < 0) {
                next = range + i + index;
            } else {
                next = range - i + index;
            }
            if (next >= 0 && next < infoList.size()) {
                if (range < 0) {
                    scrollWidth -= scrollWidth(next,false);
                } else {
                    scrollWidth += scrollWidth(next,true);
                }
            }
        }
        return scrollWidth;
    }

    private int scrollWidth(int index, boolean toRight) {
        ChTabTop target = findTab(infoList.get(index));
        if (target == null) {
            return 0;
        }
        Rect rect = new Rect();
        target.getLocalVisibleRect(rect);
        if (toRight) {//右滑
            if (rect.right > tabWith) {//right坐标大于控件的宽度时，说明完全没有显示
                return tabWith;
            } else {//显示了部分
                return tabWith - rect.right;
            }
        } else {
            if(rect.left <= -tabWith) {//完全没有显示
                return tabWith;
            } else if (rect.left > 0) {
                return rect.left;
            }
            return 0;
        }
    }

    private LinearLayout getRootLayout(boolean clear) {
        LinearLayout rootView = (LinearLayout) getChildAt(0);
        if (rootView == null) {
            rootView = new LinearLayout(getContext());
            rootView.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            addView(rootView, params);
        } else if (clear) {
            rootView.removeAllViews();
        }
        return rootView;
    }
}
