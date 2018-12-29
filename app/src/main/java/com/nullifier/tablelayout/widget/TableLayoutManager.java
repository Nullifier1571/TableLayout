package com.nullifier.tablelayout.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.nullifier.tablelayout.R;
import com.nullifier.tablelayout.widget.entity.TableClickedInfo;
import com.nullifier.tablelayout.widget.entity.TableItemData;
import com.nullifier.tablelayout.widget.entity.TableMode;
import com.nullifier.tablelayout.widget.inter.IActivityEventListener;
import com.nullifier.tablelayout.widget.inter.ITableBind;
import com.nullifier.tablelayout.widget.inter.OnTableClickedListener;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by nullifier on 2018/4/25.
 * 用于将table和外部的Fragment绑定起来,index是tableList中的角标
 */

public class TableLayoutManager implements TabItemView.OnTableItemClickedListener {
    private static final String TAG = "TableLayoutManager";
    private View mFragmentLayout;
    private FragmentManager mFragmentManager;
    private int mFragmentId;//fragment的id
    private FragmentActivity mFragmentActivity;
    private TabLayoutView mTabLayout;
    private List<Fragment> mFragmentList;
    private List<OnTableClickedListener> mOnTableClickedListeners = new ArrayList<OnTableClickedListener>();
    private int mCurrentTabIndex = -1;
    private int mLastTabIndex = -1;
    private boolean mEnableAnimation = true;//切换Fragment是否有动画
    private TableClickedInfo mTableClickedInfo = new TableClickedInfo();
    private long mLastClickPersonTime = 0;

    public TableLayoutManager(TabLayoutView tabLayout, FragmentActivity fragmentActivity, int fragmentId) {

        if (tabLayout == null || fragmentActivity == null || fragmentId == 0) {
            throw new IllegalArgumentException("TableLayoutManager 初始化参数有问题");
        }

        this.mTabLayout = tabLayout;
        this.mFragmentList = new ArrayList<Fragment>();
        this.mFragmentActivity = fragmentActivity;
        this.mFragmentId = fragmentId;
        mFragmentManager = fragmentActivity.getSupportFragmentManager();
        mFragmentLayout = fragmentActivity.findViewById(fragmentId);
        mTabLayout.setOnTableItemClickedListener(this);
    }

    public void setTabFragment(ITableBind tableBindFragment) {
        if (mTabLayout != null && tableBindFragment != null && tableBindFragment instanceof Fragment) {
            tableBindFragment.setTableLayoutManager(this);
            mFragmentList.add((Fragment) tableBindFragment);
            if (tableBindFragment.getTableItemData() != null) {
                mTabLayout.setTableItemInfo(tableBindFragment.getTableItemData());
            } else {
                throw new RuntimeException("setTabFragment exception tableBindFragment.mTableItemData==null!");
            }
        } else {
            throw new RuntimeException("setTabFragment exception ! mTabLayout==null or tableBindFragment==null");
        }

    }

    @Override
    public void onTableItemClicked(TableItemData tableItemData, int tableIndex) {
        switchTableByIndex(tableIndex, true);
    }

    /**
     * 针对切换到这个页面非用户点击操作
     *
     * @param index
     */
    public void switchTableByIndex(int index) {
        switchTableByIndex(index, false);
    }

    //为了兼容以前的逻辑
    public void switchTableByIndex(String table) {
        if (TextUtils.isEmpty(table)) {
            return;
        }
        List<TabItemView> listTabItemViews = mTabLayout.getListTabItemViews();
        for (int i = 0; i < listTabItemViews.size(); i++) {
            if (table.equals(listTabItemViews.get(i).getTabItemData().tableName)) {
                switchTableByIndex(i, false);
                break;
            }
        }
    }

    /**
     * 显示当前Manager记录的页面 非用户操作
     */
    public void showCurrentPage() {
        if (mCurrentTabIndex == -1) {
            return;
        }
        switchTableByIndex(mCurrentTabIndex);
    }

    /**
     * @param index
     * @param isFromUser 是否用户点击
     */
    public void switchTableByIndex(int index, boolean isFromUser) {

        mTabLayout.selectTable(index, isFromUser);

        if (mCurrentTabIndex != -1) {
            //如果不是第一次点击则把当前的角标赋值给上一次
            mLastTabIndex = mCurrentTabIndex;
        }
        mCurrentTabIndex = index;
        //判断下如果已经是同一个fragment了
        Fragment currentFragment = mFragmentList.get(index);
        TabItemView currentTabItemView = mTabLayout.getTabItemView(index);
        Fragment lastFragment = null;
        if (mLastTabIndex != -1) {
            lastFragment = mFragmentList.get(mLastTabIndex);
        }

        mTableClickedInfo.currentFragment = currentFragment;
        mTableClickedInfo.lastFragment = lastFragment;
        mTableClickedInfo.currentIndex = mCurrentTabIndex;
        mTableClickedInfo.lastIndex = mLastTabIndex;
        if (isSameFragment()) {
            mTableClickedInfo.isSecondClicked = true;
        } else {
            mTableClickedInfo.isSecondClicked = false;
        }
        mTableClickedInfo.isUserClicked = isFromUser;

        if (!mOnTableClickedListeners.isEmpty()) {
            for (OnTableClickedListener onTableClickedListener : mOnTableClickedListeners) {
                onTableClickedListener.onTableClickedListener(mTableClickedInfo);
            }
        }

        if (currentFragment != null) {
            ((ITableBind) currentFragment).onTableClicked(mTableClickedInfo);
        }

        if (mTableClickedInfo.isSecondClicked) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - mLastClickPersonTime < 500) {
                if (!mOnTableClickedListeners.isEmpty()) {
                    for (OnTableClickedListener onTableClickedListener : mOnTableClickedListeners) {
                        onTableClickedListener.onSameTableFastClickedListener(mTableClickedInfo);
                    }
                }
                if (currentFragment != null) {
                    ((ITableBind) currentFragment).onTableDoubleClicked(mTableClickedInfo);
                }
            }
            mLastClickPersonTime = System.currentTimeMillis();
        }

        if (!isSameFragment()) {
            try {
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                attachAnimation(transaction, mLastTabIndex, mCurrentTabIndex);
                Log.d(TAG, "switchTableByIndex() currentFragment=" + currentFragment);
                transaction.replace(mFragmentId, currentFragment, currentTabItemView.getTabItemData().tableName);
                transaction.commitAllowingStateLoss();
            } catch (Exception e) {
                Log.e("", e.getMessage());
            }

        } else {
            Log.d(TAG, "switchTableByIndex() currentFragment=" + currentFragment);
        }
    }

    private boolean isSameFragment() {
        return mCurrentTabIndex == mLastTabIndex;
    }


    private void attachAnimation(FragmentTransaction transaction, int preTabIndex, int currentTabIndex) {
        if (!mEnableAnimation) {
            return;
        }

        if (preTabIndex == currentTabIndex) {
            return;
        }

        if (preTabIndex != -1) {
            if (currentTabIndex < preTabIndex) {
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
            } else {
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        }
    }


    public void setEnableAnimation(boolean enableAnimation) {
        mEnableAnimation = enableAnimation;
    }

    public void onStart() {

    }

    public void onPause() {

    }

    public void onResume() {

    }

    public void onDestroy() {

    }

    public void setOnTableClickedListener(OnTableClickedListener onTableClickedListener) {
        if (onTableClickedListener != null && !mOnTableClickedListeners.contains(onTableClickedListener)) {
            mOnTableClickedListeners.add(onTableClickedListener);
        }
    }

    public void releaseOnTableClickedListeners() {
        mOnTableClickedListeners.clear();
    }

    public int getLastTableIndex() {
        return mLastTabIndex;
    }

    public int getCurrentTableIndex() {
        return mCurrentTabIndex;
    }

    public Fragment getCurrentFragment() {
        return mFragmentList.get(mCurrentTabIndex);
    }

    public Fragment getFragment(int index) {
        return mFragmentList.get(index);
    }

    public TabItemView getTabItemView(int index) {
        return mTabLayout.getTabItemView(index);
    }

    /**
     * 设置某个table的红点状态
     *
     * @param index  第几个table
     * @param isShow 是否显示
     */
    public void setRedPointState(int index, boolean isShow) {
        if (index > mTabLayout.getListTabItemViews().size()) {
            return;
        }
        TabItemView tabItemView = mTabLayout.getListTabItemViews().get(index);
        TableItemData tabItemData = tabItemView.getTabItemData();
        if (tabItemData.tableMode != TableMode.MODE_RED_POINT) {
            //不是红点模式的table就不要在设置了
            return;
        }
        tabItemData.isShowRedPoint = isShow;
        tabItemView.changeRedPoint(tabItemData);
    }

    public boolean getRedPointState(int index) {
        if (index > mTabLayout.getListTabItemViews().size()) {
            return false;
        }
        TabItemView tabItemView = mTabLayout.getListTabItemViews().get(index);
        TableItemData tabItemData = tabItemView.getTabItemData();
        if (tabItemData.tableMode != TableMode.MODE_RED_POINT) {
            //不是红点模式的table就不要在设置了
            return false;
        }
        return tabItemData.isShowRedPoint;
    }

    /**
     * 设置table上的message个数
     *
     * @param index
     * @param messageNumber
     */
    public void setMessageNumber(int index, int messageNumber) {
        if (index > mTabLayout.getListTabItemViews().size()) {
            return;
        }
        TabItemView tabItemView = mTabLayout.getListTabItemViews().get(index);
        TableItemData tabItemData = tabItemView.getTabItemData();
        if (tabItemData.tableMode != TableMode.MODE_MESSAGE_COUNT) {
            //不是消息个数模式的table就不要在设置了
            return;
        }
        tabItemData.messageCount = messageNumber;
        tabItemView.setData(tabItemData);
    }

    public int getMessageCount(int index) {
        if (index > mTabLayout.getListTabItemViews().size()) {
            return 0;
        }
        TabItemView tabItemView = mTabLayout.getListTabItemViews().get(index);
        TableItemData tabItemData = tabItemView.getTabItemData();
        if (tabItemData.tableMode != TableMode.MODE_MESSAGE_COUNT) {
            //不是红点模式的table就不要在设置了
            return 0;
        }
        return tabItemData.messageCount;
    }

    /**
     * 通过Tab名称获取对应的属性
     *
     * @param name
     * @return
     */
    public TableItemData getItemDataByName(String name) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        List<TabItemView> itemViews = mTabLayout.getListTabItemViews();
        if (itemViews == null || itemViews.isEmpty()) {
            return null;
        }
        for (TabItemView tabItemView : itemViews) {
            if (tabItemView.getTabItemData().tableName.equals(name)) {
                return tabItemView.getTabItemData();
            }
        }
        return null;
    }

    public TableItemData getItemDataByKey(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        List<TabItemView> itemViews = mTabLayout.getListTabItemViews();
        if (itemViews == null || itemViews.isEmpty()) {
            return null;
        }
        for (TabItemView tabItemView : itemViews) {
            if (tabItemView.getTabItemData().key.equals(key)) {
                return tabItemView.getTabItemData();
            }
        }
        return null;
    }

    public void postDataToFragment(int targetIndex, Bundle bundle) {
        if (targetIndex < mFragmentList.size()) {
            Fragment targetFragment = mFragmentList.get(targetIndex);
            if (targetFragment != null) {
                ((ITableBind) targetFragment).onTablePostData(getCurrentTableIndex(), bundle);
            }
        }
    }

    public void postDataToActivity(Bundle bundle) {
        if (mFragmentActivity instanceof IActivityEventListener) {
            ((IActivityEventListener) mFragmentActivity).onReceiveDataFromFragment(mCurrentTabIndex, bundle);
        }
    }
}
