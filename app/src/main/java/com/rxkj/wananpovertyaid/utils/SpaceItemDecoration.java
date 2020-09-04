package com.rxkj.wananpovertyaid.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Chiachi on 2019/7/5
 * Code'n'learn,ç'est la vie
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpanCount; //列数
    private int mSpacing; //间隔
    private boolean misIncludeEdge; //是否包含边缘

    public SpaceItemDecoration(int spanCount, int mSpacing, boolean misIncludeEdge) {
        this.mSpanCount = spanCount;
        this.mSpacing = mSpacing;
        this.misIncludeEdge = misIncludeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        //这里是关键，需要根据你有几列来判断
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % mSpanCount; // item column

        if (misIncludeEdge) {
            outRect.left = mSpacing - column * mSpacing / mSpanCount; // mSpacing - column * ((1f / mSpanCount) * mSpacing)
            outRect.right = (column + 1) * mSpacing / mSpanCount; // (column + 1) * ((1f / mSpanCount) * mSpacing)

            if (position < mSpanCount) { // top edge
                outRect.top = mSpacing;
            }
            outRect.bottom = mSpacing; // item bottom
        } else {
            outRect.left = column * mSpacing / mSpanCount; // column * ((1f / mSpanCount) * mSpacing)
            outRect.right = mSpacing - (column + 1) * mSpacing / mSpanCount; // mSpacing - (column + 1) * ((1f /    mSpanCount) * mSpacing)
            if (position >= mSpanCount) {
                outRect.top = mSpacing;
            }
        }
    }
}
