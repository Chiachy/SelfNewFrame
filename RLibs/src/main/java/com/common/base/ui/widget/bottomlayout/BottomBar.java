package com.common.base.ui.widget.bottomlayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;

import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.common.base.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HSK° on 2018/9/21.
 * --function: 底部导航栏
 */
public class BottomBar extends FrameLayout {

    private LinearLayout tabsContainer;
    private Context context;

    private List<BottomTab> bottomTabArrayList = new ArrayList<>();
    private OnBottomBarSelectListener mListener;
    private int currentIndex;
    private FragmentChangeManager fragmentChangeManager;


    private int bottomTabCount;
//    private int badgeViewColor;
    private int textSelectColor;
    private int textUnSelectColor;


    private float textSize;
    private float iconWidth;
    private float iconHeight;
    private float iconMargin;


    public BottomBar(@NonNull Context context) {
        this(context, null);
    }

    public BottomBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setWillNotDraw(false);
        this.context = context;
        tabsContainer = new LinearLayout(context);
        addView(tabsContainer);

        initAttrbusets(context, attrs);


    }

    /**
     * 初始化自定义属性
     */
    private void initAttrbusets(Context context, AttributeSet attrs) {

        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.BottomBar);
//        badgeViewColor = t.getColor(R.styleable.BottomBarLayout_bar_badgeview_background, Color.parseColor("#FF0006"));//默认红色
        textSelectColor = t.getColor(R.styleable.BottomBar_bar_text_select_color, Color.parseColor("#53575a"));
        textUnSelectColor = t.getColor(R.styleable.BottomBar_bar_text_unselect_color, Color.parseColor("#555555"));
        textSize = t.getDimension(R.styleable.BottomBar_bar_textsize, sp2px(14f));
        iconHeight = t.getDimension(R.styleable.BottomBar_bar_icon_height, dp2px(24));
        iconWidth = t.getDimension(R.styleable.BottomBar_bar_icon_width, dp2px(24));
        iconMargin = t.getDimension(R.styleable.BottomBar_bar_icon_margin, dp2px(1));

        t.recycle();
    }


    /**
     * 设置数据
     * 默认选中第一项
     */

    public void setBottomTabs(List<BottomTab> bottomTabs) {

        if (bottomTabs == null || bottomTabs.size() == 0)
            throw new IllegalArgumentException("bottombar can't br null or empty!");
        bottomTabArrayList.clear();
        bottomTabArrayList.addAll(bottomTabs);

        notifyDataSetChange();
        setCurrentSelect(0);

    }

    /**
     * 关联数据支持同时切换fragments
     */
    public void setBottomTabs(List<BottomTab> tabEntitys, FragmentActivity fa, int containerViewId, List<Fragment> fragments) {
        fragmentChangeManager = new FragmentChangeManager(fa.getSupportFragmentManager(), fragments, containerViewId);
        setBottomTabs(tabEntitys);
    }

    /**
     * 设置 导航栏 点击事件
     */
    public void setOnBottomBarSelectListener(OnBottomBarSelectListener listener) {
        mListener = listener;
    }


    /**
     * 显示未读消息
     *
     * @param position 显示tab位置
     * @param num      num小于等于0显示红点,num大于0显示数字
     */
    @SuppressLint("SetTextI18n")
    public void setUnRead(int position, int num) {
        if (position >= bottomTabCount) {
            position = bottomTabCount - 1;
        }

        View tabView = tabsContainer.getChildAt(position);
        AppCompatTextView tipView = tabView.findViewById(R.id.bt_tv_tips);
        if (tipView != null && num >= 0) {
            tipView.setVisibility(VISIBLE);

            if (num > 0 && num < 10) {
                tipView.setBackgroundResource(R.drawable.bt_badgeview_dot);
                tipView.setText(String.valueOf(num));
            } else if (num >= 10 && num < 99) {
                tipView.setBackgroundResource(R.drawable.bt_badgeview_dotbig);
                tipView.setText(String.valueOf(num));
            } else {
                tipView.setBackgroundResource(R.drawable.bt_badgeview_dotbig);
                tipView.setText("99+");
            }


        }
    }

    /**
     * 隐藏 未读消息
     *
     * @param position
     */
    public void hideUnRead(int position) {
        if (position >= bottomTabCount) {
            position = bottomTabCount - 1;
        }

        View tabView = tabsContainer.getChildAt(position);
        AppCompatTextView tipView = tabView.findViewById(R.id.bt_tv_tips);
        if (tipView != null) {
            tipView.setVisibility(View.GONE);
        }
    }


    /**
     * 更新数据源 与视图
     */
    private void notifyDataSetChange() {
        tabsContainer.removeAllViews();
        bottomTabCount = bottomTabArrayList.size();
        View tabView;

        for (int i = 0; i < bottomTabArrayList.size(); i++) {
            tabView = LayoutInflater.from(context).inflate(R.layout.bottombar_layout, null);
            addBottomTab(i, tabView);
        }
    }

    /**
     * 创建并添加tab
     */
    private void addBottomTab(int position, View tabView) {
        RelativeLayout rl_main = tabView.findViewById(R.id.bt_rl_main);
        AppCompatImageView iv_icon = tabView.findViewById(R.id.bt_iv_icon);
        AppCompatTextView tv_title = tabView.findViewById(R.id.bt_tv_title);
        AppCompatTextView tv_tips = tabView.findViewById(R.id.bt_tv_tips);

        rl_main.setTag(position);
        tv_title.setText(bottomTabArrayList.get(position).getTitle());
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tv_tips.setText("");

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) iconWidth, (int) iconHeight);
        layoutParams.bottomMargin = (int) iconMargin;
        iv_icon.setLayoutParams(layoutParams);
        iv_icon.setImageResource(bottomTabArrayList.get(position).getUnSelectIcon());


        rl_main.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int) view.getTag();
                if (currentIndex != position) {
                    setCurrentSelect(position);
                    if (mListener != null)
                        mListener.onTabSelect(position, bottomTabArrayList.get(position));
                } else {
                    mListener.onTabReselect(position, bottomTabArrayList.get(position));
                }
            }
        });

        /** 每一个Tab的布局参数 */
        LinearLayout.LayoutParams lp_tab = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
        tabsContainer.addView(tabView, position, lp_tab);


    }

    /**
     * 设置当前选中
     */
    public void setCurrentSelect(int position) {
        this.currentIndex = position;
        updateTabSelection(position);
        if (fragmentChangeManager != null) {
            fragmentChangeManager.setFragments(position);
        }
        invalidate();
    }


    /**
     * 更新选中样式
     */
    private void updateTabSelection(int position) {
        for (int i = 0; i < bottomTabCount; ++i) {
            View tabView = tabsContainer.getChildAt(i);
            final boolean isSelect = i == position;
            AppCompatTextView tab_title = tabView.findViewById(R.id.bt_tv_title);
            tab_title.setTextColor(isSelect ? textSelectColor : textUnSelectColor);
            AppCompatImageView iv_tab_icon = tabView.findViewById(R.id.bt_iv_icon);
            BottomTab tabEntity = bottomTabArrayList.get(i);
            iv_tab_icon.setImageResource(isSelect ? tabEntity.getSelectIcon() : tabEntity.getUnSelectIcon());
//                if (mTextBold == TEXT_BOLD_WHEN_SELECT) {
//                    tab_title.getPaint().setFakeBoldText(isSelect);
//                }
        }
    }

    public List<BottomTab> getBottomTabArrayList() {
        return bottomTabArrayList;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("mCurrentTab", currentIndex);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            currentIndex = bundle.getInt("mCurrentTab");
            state = bundle.getParcelable("instanceState");
            if (currentIndex != 0 && tabsContainer.getChildCount() > 0) {
                updateTabSelection(currentIndex);
            }
        }
        super.onRestoreInstanceState(state);
    }

    protected int dp2px(float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected int sp2px(float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

}
