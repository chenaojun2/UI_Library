package org.chen.chui.banner.indicator;

import android.view.View;

public interface ChIndicator<T extends View> {

    T get();

    /**
     * 初始化Indicator
     *
     * @param count 幻灯片数量
     * */
    void onInflate(int count);


    /**
     *幻灯片切换回调
     * @param current 切换到幻灯片位置
     * @param count 幻灯片数量
     **/
    void onPointChange(int current, int count);

}
