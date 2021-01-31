package org.chen.chui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.chen.cibrary.util.ChDisplayUtil;

/**
 * 下拉刷新视图
 */
public abstract class ChOverView extends FrameLayout {

    public enum ChRefreshState {
        /**
         * 初始
         */
        STATE_INIT,

        /**
         * Header展示
         */
        STATE_VISIBLE,

        /**
         * 超出可刷新状态
         */
        STATE_REFRESH,

        /**
         * 超出刷新位置松开手的状态
         */
        STATE_OVER_RELEASE,
        /**
         * 超出刷新距离
         */
        STATE_OVER
    }

    protected ChRefreshState mState = ChRefreshState.STATE_INIT;

    /**
     * 下拉刷新最小高度
     */
    public int mPullRefreshHeight;

    /**
     * 最小阻尼
     */
    public float minDamp = 1.6f;

    public float maxDamp = 2.2f;


    public ChOverView(@NonNull Context context) {
        super(context);
        preInit();
    }

    public ChOverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        preInit();
    }

    public ChOverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        preInit();
    }

    /**
     * 预初始化
     */
    protected void preInit() {
        mPullRefreshHeight = ChDisplayUtil.dp2px(66, getResources());
        init();
    }

    /**
     * 初始化
     */
    public abstract void init();

    protected abstract void onScroll(int scrollY, int pullRefreshHeight);

    /**
     * 显示Overlay
     */
    protected abstract void onVisible();

    /**
     * 超过Overlay，释放就会加载
     */
    protected abstract void onOver();

    /**
     * 开始刷新
     */
    public abstract void onRefresh();

    /**
     * 加载完成
     */
    public abstract void onFinish();

    public void setState(ChRefreshState state) {
        this.mState = state;
    }

    public ChRefreshState getState() {
        return mState;
    }

}
