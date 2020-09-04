package com.common.base.ui.widget.bottomlayout;

/**
 * Created by HSK° on 2018/9/21.
 * --function: bottombar 点击回调
 */
public interface OnBottomBarSelectListener {
    void onTabSelect(int position,BottomTab bottomTab);
    void onTabReselect(int position, BottomTab bottomTab);
}
