package org.chen.chui.banner.core;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import org.chen.chui.R;
import org.chen.chui.banner.ChBanner;
import org.chen.chui.banner.indicator.ChCircleIndicator;
import org.chen.chui.banner.indicator.ChIndicator;
import org.chen.chui.banner.indicator.IChBanner;

import java.util.List;

/**
 * Banner 控制器
 * 辅助完成各种功能的控制
 * 将Banner的一些逻辑内聚在这，保证暴露给使用者的Banner干净整洁
 */
public class ChBannerDelegate implements IChBanner, ViewPager.OnPageChangeListener {

    private Context mContext;
    private ChBanner mChBanner;
    private ChBannerAdapter mAdapter;
    private ChIndicator<?> mChIndicator;
    private boolean mAutoPlay;
    private boolean mLoop;
    private List<? extends ChBannerMo> mChBannerMos;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private int mIntervalTime = 5000;
    private int mScrollDuration = -1;
    private IChBanner.OnBannerClickListener mOnBannerClickListener;
    private ChViewPager mChViewPager;

    public ChBannerDelegate(Context context, ChBanner chBanner) {
        mContext = context;
        mChBanner = chBanner;
    }

    @Override
    public void setBannerData(int layoutResId, @NonNull List<? extends ChBannerMo> models) {
        mChBannerMos = models;
        init(layoutResId);
    }

    @Override
    public void setBannerData(@NonNull List<? extends ChBannerMo> models) {
        setBannerData(R.layout.ch_banner_item_image, models);
    }

    @Override
    public void setChIndicator(ChIndicator<?> chIndicator) {
        this.mChIndicator = chIndicator;
    }

    @Override
    public void setAutoPlay(boolean autoPlay) {
        this.mAutoPlay = autoPlay;
        if (mAdapter != null) {
            mAdapter.setAutoPlay(autoPlay);
        }
        if (mChViewPager != null) {
            mChViewPager.setAutoPlay(autoPlay);
        }
    }

    @Override
    public void setLoop(boolean loop) {
        this.mLoop = loop;
    }

    @Override
    public void setIntervalTime(int intervalTime) {
        if (intervalTime > 0) {
            this.mIntervalTime = intervalTime;
        }
    }

    @Override
    public void setBindAdapter(IBindAdapter bindAdapter) {
        mAdapter.setBindAdapter(bindAdapter);
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener;
    }

    @Override
    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        this.mOnBannerClickListener = onBannerClickListener;
    }

    @Override
    public void setScrollDuration(int duration) {
        this.mScrollDuration = duration;
        if(mChViewPager != null && duration > 0){
            mChViewPager.setScrollerDuration(duration);
        }
    }

    private void init(int layoutResId) {
        if (mAdapter == null) {
            mAdapter = new ChBannerAdapter(mContext);
        }

        if (mChIndicator == null) {
            mChIndicator = new ChCircleIndicator(mContext);
        }

        mChIndicator.onInflate(mChBannerMos.size());
        mAdapter.setLayoutResId(layoutResId);
        mAdapter.setBannerData(mChBannerMos);
        mAdapter.setAutoPlay(mAutoPlay);
        mAdapter.setLoop(mLoop);
        mAdapter.setBannerClickListener(mOnBannerClickListener);

        mChViewPager = new ChViewPager(mContext);
        mChViewPager.setIntervalTime(mIntervalTime);
        mChViewPager.addOnPageChangeListener(this);
        mChViewPager.setAutoPlay(mAutoPlay);
        mChViewPager.setAdapter(mAdapter);
        if(mScrollDuration > 0) {
            mChViewPager.setScrollerDuration(mScrollDuration);
        }

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        if ((mLoop || mAutoPlay) && mAdapter.getRealCount() != 0) {
            //无限轮播的关键点、使第一张能反向滑动到最后一张，已经达到无限轮动的效果
            int firstItem = mAdapter.getFirstItem();
            mChViewPager.setCurrentItem(firstItem, false);
        }
        //清除缓存View
        mChBanner.removeAllViews();
        mChBanner.addView(mChViewPager, layoutParams);
        mChBanner.addView(mChIndicator.get(), layoutParams);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (null != mOnPageChangeListener && mAdapter.getRealCount() != 0) {
            mOnPageChangeListener.onPageScrolled(position % mAdapter.getRealCount(), positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mAdapter.getRealCount() == 0) {
            return;
        }
        position = position % mAdapter.getRealCount();
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(position);
        }
        if (mChIndicator != null) {
            mChIndicator.onPointChange(position, mAdapter.getRealCount());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
    }
}
