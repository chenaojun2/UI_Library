package org.chen.chui.banner.core;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import org.chen.chui.banner.indicator.IChBanner;

import java.util.List;

public class ChBannerAdapter extends PagerAdapter {

    private Context mContext;
    private SparseArray<ChBannerViewHolder> mCachedViews = new SparseArray<>();
    private IChBanner.OnBannerClickListener mBannerClickListener;
    private IBindAdapter mBindAdapter;
    private List<? extends ChBannerMo> models;

    /**
     * 是否开启自动轮播
     */
    private boolean mAutoPlay = true;
    /**
     * 非自动轮播下是否可以循环切换
     */
    private boolean mLoop = false;
    private int mLayoutResId = -1;

    public ChBannerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setBannerData(List<? extends ChBannerMo> models) {
        this.models = models;
        //初始化数据
        initCachedView();
        notifyDataSetChanged();
    }

    public void setBannerClickListener(IChBanner.OnBannerClickListener bannerClickListener) {
        this.mBannerClickListener = bannerClickListener;
    }

    public void setBindAdapter(IBindAdapter bindAdapter) {
        this.mBindAdapter = bindAdapter;
    }

    public void setAutoPlay(boolean autoPlay) {
        this.mAutoPlay = autoPlay;
    }

    public void setLoop(boolean isLoop) {
        this.mLoop = isLoop;
    }

    public void setLayoutResId(int layoutResId) {
        this.mLayoutResId = layoutResId;
    }

    @Override
    public int getCount() {
        //无限轮播关键点
        return mAutoPlay ? Integer.MAX_VALUE : (mLoop ? Integer.MAX_VALUE : getRealCount());
    }

    public int getRealCount() {
        return models == null ? 0 : models.size();
    }

    /**
     * 获取初次展示item的位置
     */
    public int getFirstItem() {
        return Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % getRealCount();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //真正展示的ViewHolder
        int realPosition = position;
        if (getRealCount() > 0) {
            realPosition = position % getRealCount();
        }
        ChBannerViewHolder viewHolder = mCachedViews.get(realPosition);
        //防止重复添加
        if (container.equals(viewHolder.rootView.getParent())) {
            container.removeView(viewHolder.rootView);
        }
        //数据绑定
        onBind(viewHolder, models.get(realPosition), realPosition);
        if (viewHolder.rootView.getParent() != null) {
            ((ViewGroup) viewHolder.rootView.getParent()).removeView(viewHolder.rootView);
        }
        container.addView(viewHolder.rootView);
        return viewHolder.rootView;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        //让item每次都刷新
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //不调用父类方法避免删除数据
    }

    protected void onBind(@NonNull final ChBannerAdapter.ChBannerViewHolder viewHolder, @NonNull final ChBannerMo bannerMo, final int position) {
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBannerClickListener != null) {
                    mBannerClickListener.onBannerClick(viewHolder, bannerMo, position);
                }
            }
        });

        //业务层数据绑定
        if (mBindAdapter != null) {
            mBindAdapter.onBind(viewHolder, bannerMo, position);
        }
    }

    private void initCachedView() {
        mCachedViews = new SparseArray<>();
        for (int i = 0; i < models.size(); i++) {
            ChBannerViewHolder viewHolder = new ChBannerViewHolder(createView(LayoutInflater.from(mContext), null));
            mCachedViews.put(i, viewHolder);
        }
    }

    private View createView(LayoutInflater layoutInflater, ViewGroup parent) {
        if (mLayoutResId == -1) {
            throw new IllegalArgumentException("you must be set setLayoutResId first");
        }
        return layoutInflater.inflate(mLayoutResId, parent, false);
    }

    public static class ChBannerViewHolder {
        private SparseArray<View> viewSparseArray;

        View rootView;

        public ChBannerViewHolder(View rootView) {
            this.rootView = rootView;
        }

        public View getRootView() {
            return rootView;
        }

        public <V extends View> V findViewById(int id) {
            if (!(rootView instanceof ViewGroup)) {
                return (V) rootView;
            }
            if (this.viewSparseArray == null) {
                this.viewSparseArray = new SparseArray<>(1);
            }
            V childView = (V) viewSparseArray.get(id);
            if (childView == null) {
                childView = rootView.findViewById(id);
                this.viewSparseArray.put(id, childView);
            }
            return childView;
        }

    }
}
