package org.chen.chui.banner.indicator;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import org.chen.chui.banner.core.ChBannerAdapter;
import org.chen.chui.banner.core.ChBannerMo;
import org.chen.chui.banner.core.IBindAdapter;

import java.util.List;

public interface IChBanner {

    void setBannerData(@LayoutRes int layoutResId, @NonNull List<? extends ChBannerMo> models);

    void setBannerData(@NonNull List<? extends ChBannerMo> models);

    void setChIndicator(ChIndicator<?> chIndicator);

    void setAutoPlay(boolean autoPlay);

    void setLoop(boolean loop);

    void setIntervalTime(int intervalTime);

    void setBindAdapter(IBindAdapter bindAdapter);

    void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener);

    void setOnBannerClickListener(OnBannerClickListener onBannerClickListener);

    void setScrollDuration(int duration);

    interface OnBannerClickListener {
        void onBannerClick(@NonNull ChBannerAdapter.ChBannerViewHolder viewHolder, @NonNull ChBannerMo bannerMo, int position);
    }

}
