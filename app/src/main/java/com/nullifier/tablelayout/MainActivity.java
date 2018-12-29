package com.nullifier.tablelayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.nullifier.tablelayout.widget.TabLayoutView;
import com.nullifier.tablelayout.widget.TableLayoutManager;
import com.nullifier.tablelayout.widget.entity.TableClickedInfo;
import com.nullifier.tablelayout.widget.inter.IActivityEventListener;
import com.nullifier.tablelayout.widget.inter.OnTableClickedListener;

public class MainActivity extends AppCompatActivity implements OnTableClickedListener, IActivityEventListener {
    private TabLayoutView mTabLayout;
    private TableLayoutManager mTableLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取table组件
        mTabLayout = findViewById(R.id.tab_layout_view);
        //初始化管理类
        mTableLayoutManager = new TableLayoutManager(mTabLayout, this, R.id.realtabcontent);
        //是否开启每个item动画
        mTableLayoutManager.setEnableAnimation(false);

        //设置点击事件
        mTableLayoutManager.setOnTableClickedListener(this);

        //初始化fragment并绑定
        initFragments();

        mTableLayoutManager.switchTableByIndex(0);
    }


    private void initFragments() {
        if (mTableLayoutManager == null) {
            throw new RuntimeException("initFragments mast invoke after mTableLayoutManager init");
        }
        mTableLayoutManager.setTabFragment(new FragmentTest1());
        mTableLayoutManager.setTabFragment(new FragmentTest2());
        mTableLayoutManager.setTabFragment(new FragmentTest3());
        mTableLayoutManager.setTabFragment(new FragmentTest4());
        mTableLayoutManager.setTabFragment(new FragmentTest5());
    }

    @Override
    public void onTableClickedListener(TableClickedInfo tableClickedInfo) {
//被点击了
    }

    @Override
    public void onSameTableFastClickedListener(TableClickedInfo tableClickedInfo) {
//一个item被快速点击了
    }

    @Override
    public void onReceiveDataFromFragment(int fromIndex, Bundle bundle) {
        //fragment向activity传递数据 可选项
    }
}
