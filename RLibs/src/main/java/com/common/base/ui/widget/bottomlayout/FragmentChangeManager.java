package com.common.base.ui.widget.bottomlayout;

//import android.support.annotation.IdRes;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

/**
 * Created by HSK° on 2018/9/21.
 * --function: bottombarlayout 与fragment 联动的管理类
 */
public class FragmentChangeManager {

    private FragmentManager fm;

    private List<Fragment> fragments;

    private @IdRes
    int containerId;
    /**
     * 当前选中的Tab索引
     */
    private int mCurrentTab;


    public FragmentChangeManager(FragmentManager fm, List<Fragment> fragments, int containerId) {
        this.fm = fm;
        this.fragments = fragments;
        this.containerId = containerId;

        initFragments();
    }


    /**
     * 初始化fragments
     */
    private void initFragments() {
        for (Fragment fragment : fragments) {
            fm.beginTransaction().add(containerId, fragment).hide(fragment).commit();
        }

        setFragments(0);
    }

    /**
     * 界面切换控制
     */
    public void setFragments(int index) {
        for (int i = 0; i < fragments.size(); i++) {
            FragmentTransaction ft = fm.beginTransaction();
            Fragment fragment = fragments.get(i);
            if (i == index) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commit();
        }
        mCurrentTab = index;
    }

    public int getCurrentTabIndex() {
        return mCurrentTab;
    }

    public Fragment getCurrentFragment() {
        return fragments.get(mCurrentTab);
    }


}
