package com.nullifier.tablelayout.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nullifier.tablelayout.R;
import com.nullifier.tablelayout.widget.entity.TableItemData;
import com.nullifier.tablelayout.widget.entity.TableMode;

/**
 * Created by nullifier on 2018/4/25.
 */

public class TabItemView extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "TabItemView";

    private ImageView mTabIcon;

    private ImageView mTabRedImage;

    private TextView mTabTitle;

    private TextView mTabMsgCount;

    private TableItemData mTableItemData;

    private OnTableItemClickedListener mOnTableItemClickedListener;
    private ImageView mPublishIcon;
    private RelativeLayout mNormalRelativeLayout;
    private int mIndex;
    private Context mContext;

    public TabItemView(Context context) {
        super(context);
        initView(context);
    }

    public TabItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TabItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public void initView(Context context) {
        this.mContext = context;
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
        View rootView = View.inflate(context, R.layout.home_tab_view, this);
        mTabIcon = (ImageView) rootView.findViewById(R.id.tab_icon);
        mPublishIcon = (ImageView) rootView.findViewById(R.id.icon_publish);
        mNormalRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.rl_normal);
        mTabRedImage = (ImageView) rootView.findViewById(R.id.tab_red_img);
        mTabTitle = (TextView) rootView.findViewById(R.id.tab_title);
        mTabMsgCount = (TextView) rootView.findViewById(R.id.tab_msg_count);
        setOnClickListener(this);

    }

    //设置这个Table是第几个
    public void setTabIndex(int index) {
        this.mIndex = index;
    }

    public void setData(TableItemData tableItemData) {
        this.mTableItemData = tableItemData;

        if (tableItemData != null) {

            if (!TextUtils.isEmpty(tableItemData.tableName)) {
                mTabTitle.setText(tableItemData.tableName);
            }

            setTableSelectedState(tableItemData.isSelected);

            switch (tableItemData.tableMode) {
                case TableMode.MODE_NONE:
                    mNormalRelativeLayout.setVisibility(VISIBLE);
                    mPublishIcon.setVisibility(GONE);
                    mTabRedImage.setVisibility(GONE);
                    mTabMsgCount.setVisibility(GONE);
                    break;
                case TableMode.MODE_RED_POINT:
                    mNormalRelativeLayout.setVisibility(VISIBLE);
                    mPublishIcon.setVisibility(GONE);
                    mTabMsgCount.setVisibility(GONE);
                    if (tableItemData.isShowRedPoint) {
                        mTabRedImage.setVisibility(VISIBLE);
                    } else {
                        mTabRedImage.setVisibility(GONE);
                    }
                    break;
                case TableMode.MODE_MESSAGE_COUNT:
                    mNormalRelativeLayout.setVisibility(VISIBLE);
                    mPublishIcon.setVisibility(GONE);
                    mTabRedImage.setVisibility(GONE);
                    if (tableItemData.messageCount > 0) {
                        mTabMsgCount.setVisibility(VISIBLE);
                        mTabMsgCount.setText(tableItemData.messageCount + "");
                    } else {
                        mTabMsgCount.setVisibility(GONE);
                    }
                    break;
                case TableMode.MODE_PUBLISH:
                    mNormalRelativeLayout.setVisibility(GONE);
                    mPublishIcon.setVisibility(VISIBLE);
                    mTabRedImage.setVisibility(GONE);
                    if (tableItemData.messageCount > 0) {
                        mTabMsgCount.setVisibility(VISIBLE);
                        mTabMsgCount.setText(tableItemData.messageCount + "");
                    } else {
                        mTabMsgCount.setVisibility(GONE);
                    }
                    break;
            }
        } else {
            Log.e(TAG, "initData data==null");
        }
    }

    public TableItemData getTabItemData() {
        return mTableItemData;
    }

    public void setTableSelectedState(boolean selected) {

        if (mTableItemData == null) {
            return;
        }
        if (selected) {
            //如果有动画并且是没选中状态则执行动画
            if (mTableItemData.iconAnimationResId != 0 && !mTableItemData.isSelected) {
                mTabIcon.setImageDrawable(getResources().getDrawable(mTableItemData.iconAnimationResId));
                Drawable drawable = mTabIcon.getDrawable();
                if (!(drawable instanceof AnimationDrawable)) {
                    return;
                }
                AnimationDrawable animationDrawable = (AnimationDrawable) drawable;
                animationDrawable.setOneShot(true);
                animationDrawable.start();
            } else {
                mTabIcon.setImageResource(mTableItemData.iconSelectedResId);
            }
            int colorDefault = R.color.tab_selected_text_color;
            if (mTableItemData.textSelectedResId != 0) {
                colorDefault = mTableItemData.textSelectedResId;
            }
            mTabTitle.setTextColor(getResources().getColor(colorDefault));
        } else {
            mTabIcon.setImageResource(mTableItemData.iconDefaultResId);
            int colorDefault = R.color.tab_unselected_text_color;
            if (mTableItemData.textDefaultResId != 0) {
                colorDefault = mTableItemData.textDefaultResId;
            }
            mTabTitle.setTextColor(getResources().getColor(colorDefault));
        }

    }

    @Override
    public void onClick(View v) {
        if (mOnTableItemClickedListener != null) {
            mOnTableItemClickedListener.onTableItemClicked(mTableItemData, mIndex);
        }
    }

    public interface OnTableItemClickedListener {
        void onTableItemClicked(TableItemData tableItemData, int tableIndex);
    }

    public void setOnTableItemClickedListener(OnTableItemClickedListener onTableItemClickedListener) {
        this.mOnTableItemClickedListener = onTableItemClickedListener;
    }

    public boolean getTableIsSelected() {
        return mTableItemData.isSelected;
    }

    public void changeRedPoint(TableItemData tableItemData){
        if (tableItemData.isShowRedPoint) {
            mTabRedImage.setVisibility(VISIBLE);
        } else {
            mTabRedImage.setVisibility(GONE);
        }
    }

}
