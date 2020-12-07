package org.chen.chui.tab.common;

import androidx.annotation.NonNull;
import androidx.annotation.Px;

public interface IChTab<D> extends IChTabLayout.OnTabSelectedListener<D> {

    void setChTabInfo(@NonNull D data);

    void resetHeight(@Px int height);

}
