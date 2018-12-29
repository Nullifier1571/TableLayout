package com.nullifier.tablelayout.widget.inter;


import android.os.Bundle;

import com.nullifier.tablelayout.widget.TableLayoutManager;
import com.nullifier.tablelayout.widget.entity.TableClickedInfo;
import com.nullifier.tablelayout.widget.entity.TableItemData;


/**
 * Created by nullifier on 2018/4/26.
 * 如果Fragment和一个TableViewItem绑定，那么就实现这个fragment，以获得TableLayoutManager
 * TableLayoutManager 用来操作table上的红点或者消息个数逻辑
 */

public interface ITableBind {
    public void initTableInfo();

    public void setTableLayoutManager(TableLayoutManager tableLayoutManager);

    public TableItemData getTableItemData();

    public void onTableClicked(TableClickedInfo tableClickedInfo);

    public void onTableDoubleClicked(TableClickedInfo tableClickedInfo);

    public void onTablePostData(int fromIndex, Bundle bundle);
}
