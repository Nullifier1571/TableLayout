package com.nullifier.tablelayout.widget.inter;


import com.nullifier.tablelayout.widget.entity.TableClickedInfo;

public interface OnTableClickedListener {
    void onTableClickedListener(TableClickedInfo tableClickedInfo);

    void onSameTableFastClickedListener(TableClickedInfo tableClickedInfo);
}
