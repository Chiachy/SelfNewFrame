package com.common.base.ui.widget.bottomlayout;

import androidx.annotation.DrawableRes;

/**
 * Created by HSK° on 2018/9/21.
 * --function: 导航栏 实体
 */
public class BottomTab {

    private String title;

    /**
     * 选中的图标
     */
    private @DrawableRes int selectIcon;
    /**
     * 未选中的图标
     */
    private @DrawableRes
    int unSelectIcon;


    public BottomTab(String title, int selectIcon, int unSelectIcon) {
        this.title = title;
        this.selectIcon = selectIcon;
        this.unSelectIcon = unSelectIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSelectIcon() {
        return selectIcon;
    }

    public void setSelectIcon(int selectIcon) {
        this.selectIcon = selectIcon;
    }

    public int getUnSelectIcon() {
        return unSelectIcon;
    }

    public void setUnSelectIcon(int unSelectIcon) {
        this.unSelectIcon = unSelectIcon;
    }
}
