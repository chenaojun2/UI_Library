package org.chen.chui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ChRefreshLayout extends FrameLayout implements ChRefresh {

    private ChOverView.ChRefreshState mState;
    private GestureDetector mGestureDetector;
    private ChRefreshListener mChRefreshListener;
    protected ChOverView mChOverView;
    private int mLastY;
    private boolean disableRefreshScroll;
    private AutoScroller mScroller;

    public ChRefreshLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public ChRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mState = ChOverView.ChRefreshState.STATE_INIT;
        mGestureDetector = new GestureDetector(getContext(), chGestureDetector);
        mScroller = new AutoScroller();
    }

    @Override
    public void setDisableRefreshScroll(boolean disableRefreshScroll) {
        this.disableRefreshScroll = disableRefreshScroll;
    }

    @Override
    public void refreshFinished() {
        View head = getChildAt(0);
        mChOverView.onFinish();
        mChOverView.setState(ChOverView.ChRefreshState.STATE_INIT);
        int bottom = head.getBottom();
        if (bottom > 0) {
            recover(bottom);
        }
        mState = ChOverView.ChRefreshState.STATE_INIT;
    }

    @Override
    public void setRefreshListener(ChRefreshListener chRefreshListener) {
        this.mChRefreshListener = chRefreshListener;
    }

    @Override
    public void setRefreshOverView(ChOverView chOverView) {
        if (this.mChOverView != null) {
            removeView(mChOverView);
        }
        this.mChOverView = chOverView;
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mChOverView, 0, params);
    }

    ChGestureDetector chGestureDetector = new ChGestureDetector() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceX) > Math.abs(distanceY) || mChRefreshListener != null && !mChRefreshListener.enableRefresh()) {
                //横向滑动，刷新被禁止
                return false;
            }
            if (disableRefreshScroll && mState == ChOverView.ChRefreshState.STATE_REFRESH) {
                //刷新是否禁止滑动
                return true;
            }
            View head = getChildAt(0);
            View child = ChScrollerUtil.findScrollableChild(ChRefreshLayout.this);
            if (ChScrollerUtil.childScrolled(child)) {
                //列表发生了滑动不处理
                return false;
            }
            //没有刷新或者没有达到刷新的距离，且头部已经划出或者下拉
            if ((mState != ChOverView.ChRefreshState.STATE_REFRESH || head.getBottom() <= mChOverView.mPullRefreshHeight) && (head.getBottom() > 0 || distanceY <= 0.0f)) {
                //还在滑动中
                if (mState != ChOverView.ChRefreshState.STATE_OVER_RELEASE) {
                    int seed;
                    //滑动速度计算
                    if (child.getTop() < mChOverView.mPullRefreshHeight) {
                        seed = (int) (mLastY / mChOverView.minDamp);
                    } else {
                        seed = (int) (mLastY / mChOverView.maxDamp);
                    }
                    //如果正在刷新，则不允许滑动的时候改变状态
                    boolean bool = moveDown(seed, true);
                    mLastY = (int) -distanceY;
                    return bool;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    };

    /**
     * 根据偏移量移动header与child
     */
    private boolean moveDown(int offsetY, boolean nonAuto) {
        View head = getChildAt(0);
        View child = getChildAt(1);
        int childTop = child.getTop() + offsetY;
        if (childTop <= 0) {
            //异常情况
            offsetY = -child.getTop();
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
            if (mState != ChOverView.ChRefreshState.STATE_REFRESH) {
                mState = ChOverView.ChRefreshState.STATE_INIT;
            }
        } else if (mState == ChOverView.ChRefreshState.STATE_REFRESH && childTop > mChOverView.mPullRefreshHeight) {
            //如果正在下拉刷新，禁止继续下拉
            return false;
        } else if (childTop <= mChOverView.mPullRefreshHeight) {
            //还没有超出设定的刷新距离
            if (mChOverView.getState() != ChOverView.ChRefreshState.STATE_VISIBLE && nonAuto) {
                //头部开始显示
                mChOverView.onVisible();
                mChOverView.setState(ChOverView.ChRefreshState.STATE_VISIBLE);
                mState = ChOverView.ChRefreshState.STATE_VISIBLE;
            }
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
            if (childTop == mChOverView.mPullRefreshHeight && mState == ChOverView.ChRefreshState.STATE_OVER_RELEASE) {
                //TODO 下拉刷新完成
                refresh();
            }
        } else {
            if (mChOverView.getState() != ChOverView.ChRefreshState.STATE_OVER && nonAuto) {
                //超出刷新位置
                mChOverView.onOver();
                mChOverView.setState(ChOverView.ChRefreshState.STATE_OVER);
            }
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
        }
        if (mChOverView != null) {
            mChOverView.onScroll(head.getBottom(), mChOverView.mPullRefreshHeight);
        }
        return true;
    }

    private void refresh() {
        if (mChRefreshListener != null) {
            mState = ChOverView.ChRefreshState.STATE_REFRESH;
            mChOverView.onRefresh();
            mChOverView.setState(ChOverView.ChRefreshState.STATE_REFRESH);
            mChRefreshListener.onRefresh();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View head = getChildAt(0);
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL
                || ev.getAction() == MotionEvent.ACTION_POINTER_INDEX_MASK) {
            //松开手
            if (head.getBottom() > 0) {
                if (mState != ChOverView.ChRefreshState.STATE_REFRESH) {
                    //非刷新
                    recover(head.getBottom());
                    return false;
                }
            }
            mLastY = 0;
        }
        boolean consumed = mGestureDetector.onTouchEvent(ev);
        if ((consumed || (mState != ChOverView.ChRefreshState.STATE_INIT && mState != ChOverView.ChRefreshState.STATE_REFRESH))
                && head.getBottom() != 0) {
            ev.setAction(MotionEvent.ACTION_CANCEL);
            return super.dispatchTouchEvent(ev);
        }
        if (consumed) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    private void recover(int dis) {
        if (mChRefreshListener != null && dis > mChOverView.mPullRefreshHeight) {
            //滚动到指定位置
            mScroller.recover(dis - mChOverView.mPullRefreshHeight);
            mState = ChOverView.ChRefreshState.STATE_OVER_RELEASE;
        } else {
            mScroller.recover(dis);
        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //定义排列位置
        View head = getChildAt(0);
        View child = getChildAt(1);
        if (head != null && child != null) {
            int childTop = child.getTop();
            if (mState == ChOverView.ChRefreshState.STATE_REFRESH) {
                head.layout(0, mChOverView.mPullRefreshHeight - head.getMeasuredHeight(), right, mChOverView.mPullRefreshHeight);
                child.layout(0, mChOverView.mPullRefreshHeight, right, mChOverView.mPullRefreshHeight + head.getMeasuredHeight());
            } else {
                head.layout(0, childTop - head.getMeasuredHeight() - head.getMeasuredHeight(), right, childTop);
                child.layout(0, childTop, right, childTop + head.getMeasuredHeight());
            }
            View other;
            for (int i = 2; i < getChildCount(); i++) {
                other = getChildAt(i);
                other.layout(0, top, right, bottom);
            }
        }
    }

    private class AutoScroller implements Runnable {

        private Scroller mScroller;

        private int mLastY;

        private boolean mIsFinished;

        public AutoScroller() {
            mScroller = new Scroller(getContext(), new LinearInterpolator());
            mIsFinished = true;
        }

        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {//还未滚动完成
                moveDown(mLastY - mScroller.getCurrY(), false);
                mLastY = mScroller.getCurrY();
                post(this);
            } else {
                removeCallbacks(this);
                mIsFinished = true;
            }
        }

        void recover(int dis) {
            if (dis <= 0) {
                return;
            }
            removeCallbacks(this);
            mLastY = 0;
            mIsFinished = false;
            mScroller.startScroll(0, 0, 0, dis, 300);
            post(this);
        }

        boolean isIsFinished() {
            return mIsFinished;
        }
    }
}
