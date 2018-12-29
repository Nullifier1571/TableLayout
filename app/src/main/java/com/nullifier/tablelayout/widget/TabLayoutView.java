package com.nullifier.tablelayout.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;


import com.nullifier.tablelayout.widget.entity.TableItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nullifier on 2018/4/25.
 */

public class TabLayoutView extends LinearLayout implements TabItemView.OnTableItemClickedListener {

    private Context mContext;
    private TabItemView.OnTableItemClickedListener mOnTableItemClickedListener;
    private List<TabItemView> mListTabItemViews = new ArrayList<TabItemView>();

    public TabLayoutView(Context context) {
        super(context);
        initView(context);
    }

    public TabLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TabLayoutView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        setOrientation(HORIZONTAL);
    }

    public void addTableItemView(TableItemData tableItemData) {
        TabItemView tabItemView = new TabItemView(mContext);
        tabItemView.setData(tableItemData);
        tabItemView.setTabIndex(getChildCount());
        tabItemView.setOnTableItemClickedListener(this);
        addView(tabItemView);
        mListTabItemViews.add(tabItemView);
    }

    /*public void addTableItemViews(List<TableItemData> listTableItemData) {
        if (listTableItemData != null && listTableItemData.size() > 0) {
            for (int i = 0; i < listTableItemData.size(); i++) {
                TableItemData tableItemData = listTableItemData.get(i);
                addTableItemView(tableItemData);
            }
        }
    }*/


    public void clearAllTables() {
        removeAllViews();
        mListTabItemViews.clear();
    }

    @Override
    public void onTableItemClicked(TableItemData tableItemData, int tableIndex) {
        if (mOnTableItemClickedListener != null) {
            mOnTableItemClickedListener.onTableItemClicked(tableItemData, tableIndex);
        }
    }


    public void setOnTableItemClickedListener(TabItemView.OnTableItemClickedListener onTableItemClickedListener) {
        this.mOnTableItemClickedListener = onTableItemClickedListener;
    }

    public List<TabItemView> getListTabItemViews() {
        return mListTabItemViews;
    }

    public TabItemView getTabItemView(int index) {
        return mListTabItemViews.get(index);
    }


    public void selectTable(int index, boolean isUserClicked) {
        for (int i = 0; i < mListTabItemViews.size(); i++) {
            TabItemView tabItemView = mListTabItemViews.get(i);
            if (i == index) {
                tabItemView.setTableSelectedState(true);
            } else {
                tabItemView.setTableSelectedState(false);
            }
        }
    }

    public void setTableItemInfo(TableItemData tableItemData) {
        addTableItemView(tableItemData);
    }
}
