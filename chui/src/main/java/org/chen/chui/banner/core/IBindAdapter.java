package org.chen.chui.banner.core;

/**
 * ChBanner的数据绑定接口
 * */
public interface IBindAdapter {

    void onBind(ChBannerAdapter.ChBannerViewHolder viewHolder, ChBannerMo mo, int position);

}
