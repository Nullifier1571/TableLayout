package com.nullifier.tablelayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nullifier.tablelayout.widget.TabLayoutView;
import com.nullifier.tablelayout.widget.TableLayoutManager;
import com.nullifier.tablelayout.widget.entity.TableClickedInfo;
import com.nullifier.tablelayout.widget.entity.TableItemData;
import com.nullifier.tablelayout.widget.entity.TableMode;
import com.nullifier.tablelayout.widget.inter.ITableBind;

public class FragmentTest1 extends Fragment implements ITableBind {
    private TabLayoutView mTabLayout;
    private TableLayoutManager mTableLayoutManager;
    private TableItemData mFirstTableItemData;

    @Override
    public void initTableInfo() {
        if (mFirstTableItemData == null) {
            mFirstTableItemData = new TableItemData();
        }
        mFirstTableItemData.tableName = "首页";//table名称
        mFirstTableItemData.iconSelectedResId = R.mipmap.home_selected;
        mFirstTableItemData.iconDefaultResId = R.mipmap.home_unselected;
        mFirstTableItemData.messageCount = 0;//消息个数
        mFirstTableItemData.isShowRedPoint = false;//消息个数
        mFirstTableItemData.tableMode = TableMode.MODE_NONE;//table模式
        mFirstTableItemData.indexInHomeTabs = 0;
        mFirstTableItemData.key = "main";
    }

    @Override
    public void setTableLayoutManager(TableLayoutManager tableLayoutManager) {
        this.mTableLayoutManager = tableLayoutManager;
    }

    @Override
    public TableItemData getTableItemData() {
        if (mFirstTableItemData == null) {
            initTableInfo();
        }
        return mFirstTableItemData;
    }

    @Override
    public void onTableClicked(TableClickedInfo tableClickedInfo) {
        if (tableClickedInfo.currentFragment instanceof FragmentTest1 && tableClickedInfo.isSecondClicked) {
        }
    }

    @Override
    public void onTableDoubleClicked(TableClickedInfo tableClickedInfo) {

    }

    @Override
    public void onTablePostData(int fromIndex, Bundle bundle) {

    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mTableLayoutManager.postDataToActivity(new Bundle());
        return inflater.inflate(R.layout.fragment_test_one, null);
    }
}
