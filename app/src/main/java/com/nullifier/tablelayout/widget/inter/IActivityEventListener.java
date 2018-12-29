package com.nullifier.tablelayout.widget.inter;

import android.os.Bundle;

/**
 * 用于向activity回调数据非必须选项
 */
public interface IActivityEventListener {
    void onReceiveDataFromFragment(int fromIndex, Bundle bundle);
}
