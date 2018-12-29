package com.nullifier.tablelayout.widget.entity;

import android.support.v4.app.Fragment;

/**
 * Created by nullifier on 2018/4/25.
 */

public class TableClickedInfo {
    public int currentIndex;
    public int lastIndex;
    public Fragment currentFragment;
    public Fragment lastFragment;
    public boolean isSecondClicked;//如果目前Fragment是A，此时点击A所对应table或直接调用相关方法非用户点击，这种情况返回ture
    public boolean isUserClicked;//是否是用户点击

    @Override
    public String toString() {
        return "TableClickedInfo{" +
                "currentIndex=" + currentIndex +
                ", lastIndex=" + lastIndex +
                ", currentFragment=" + currentFragment +
                ", lastFragment=" + lastFragment +
                ", isSecondClicked=" + isSecondClicked +
                ", isUserClicked=" + isUserClicked +
                '}';
    }
}
