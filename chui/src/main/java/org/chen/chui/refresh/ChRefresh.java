package org.chen.chui.refresh;

/**
 * @author caj
 */
public interface ChRefresh {

    /**
     * 刷新时是否禁止滚动
     *
     * @param disableRefreshScroll 是否滚动
     * */
    void setDisableRefreshScroll(boolean disableRefreshScroll);

    /**
     * 刷新完成
     * */
    void refreshFinished();

    void setRefreshListener(ChRefreshListener chRefreshListener);

    void setRefreshOverView(ChOverView chOverView);


    interface ChRefreshListener{

        void onRefresh();

        boolean enableRefresh();

    }
}
