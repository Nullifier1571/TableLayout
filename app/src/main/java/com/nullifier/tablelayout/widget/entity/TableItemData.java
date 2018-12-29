package com.nullifier.tablelayout.widget.entity;

/**
 * Created by nullifier on 2018/4/25.
 */

public class TableItemData {

    public String tableName;//table名称
    public int iconDefaultResId;//图标资源
    public int iconSelectedResId;//图标选中资源
    public int iconAnimationResId;//图标动画资源
    public int textDefaultResId;//文字资源
    public int textSelectedResId;//文字选中资源
    public int messageCount;//消息个数
    public boolean isShowRedPoint;//是否显示小红点
    public int tableMode = TableMode.MODE_NONE;//table模式
    public int indexInHomeTabs;//在首页Tab的位置游标
    public String key;// tab唯一标识
    public boolean isSelected = false;//是否被选中
}
