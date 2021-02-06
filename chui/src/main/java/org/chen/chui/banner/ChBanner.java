package org.chen.chui.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import org.chen.chui.R;
import org.chen.chui.banner.core.ChBannerDelegate;
import org.chen.chui.banner.core.ChBannerMo;
import org.chen.chui.banner.indicator.ChIndicator;
import org.chen.chui.banner.core.IBindAdapter;
import org.chen.chui.banner.indicator.IChBanner;

import java.util.List;

public class ChBanner extends FrameLayout implements IChBanner {

    private ChBannerDelegate delegate;


    public ChBanner(@NonNull Context context) {
        this(context,null);
    }

    public ChBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ChBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        delegate = new ChBannerDelegate(context,this);
        initCustomAttrs(context,attrs);
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChBanner);
        boolean autoPlay = typedArray.getBoolean(R.styleable.ChBanner_autoPlay,true);
        boolean loop = typedArray.getBoolean(R.styleable.ChBanner_loop,true);
        int intervalTime = typedArray.getInteger(R.styleable.ChBanner_intervalTime,-1);
        setAutoPlay(autoPlay);
        setLoop(loop);
        setIntervalTime(intervalTime);
        typedArray.recycle();
    }

    @Override
    public void setBannerData(int layoutResId, @NonNull List<? extends ChBannerMo> models) {
        delegate.setBannerData(layoutResId,models);
    }

    @Override
    public void setBannerData(@NonNull List<? extends ChBannerMo> models) {
        delegate.setBannerData(models);
    }

    @Override
    public void setChIndicator(ChIndicator<?> chIndicator) {
        delegate.setChIndicator(chIndicator);
    }

    @Override
    public void setAutoPlay(boolean autoPlay) {
        delegate.setAutoPlay(autoPlay);
    }

    @Override
    public void setLoop(boolean loop) {
        delegate.setLoop(loop);
    }

    @Override
    public void setIntervalTime(int intervalTime) {
        delegate.setIntervalTime(intervalTime);
    }

    @Override
    public void setBindAdapter(IBindAdapter bindAdapter) {
        delegate.setBindAdapter(bindAdapter);
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        delegate.setOnPageChangeListener(onPageChangeListener);
    }

    @Override
    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        delegate.setOnBannerClickListener(onBannerClickListener);
    }

    @Override
    public void setScrollDuration(int duration) {
        delegate.setScrollDuration(duration);
    }
}
