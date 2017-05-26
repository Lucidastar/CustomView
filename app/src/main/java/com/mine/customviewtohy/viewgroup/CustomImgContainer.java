package com.mine.customviewtohy.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by qiuyouzone on 2017/5/25.
 */

public class CustomImgContainer extends ViewGroup {



    public CustomImgContainer(Context context) {
        super(context);
    }

    public CustomImgContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImgContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        //ViewGroup的宽和高
        int width = 0;
        int height = 0;

        //child的宽和高
        int cWidth = 0;
        int cHeight = 0;

        measureChildren(widthMeasureSpec,heightMeasureSpec);

        MarginLayoutParams cParam = null;
        int childCount = getChildCount();
        //左边两个孩子的高度和
        int lHeight = 0;
        //右边两个孩子的高度和  然后取他们的最大值
        int rHeight = 0;

        //上边两个孩子的宽度和
        int tWidth = 0;
        //下边两个孩子的宽和  然后取他们的最大值
        int bWidth = 0;

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParam = (MarginLayoutParams) childView.getLayoutParams();

            if (i == 0 || i == 1){
                tWidth += cWidth + cParam.leftMargin + cParam.rightMargin;
            }
            if (i == 0 || i == 2){
                lHeight += cHeight + cParam.topMargin + cParam.bottomMargin;
            }

            if (i == 1 || i == 3){
                rHeight += cHeight + cParam.topMargin + cParam.bottomMargin;
            }

            if (i == 2 || i == 3){
                bWidth += cWidth + cParam.leftMargin + cParam.rightMargin;
            }

        }

        width = Math.max(tWidth,bWidth);
        height = Math.max(lHeight,rHeight);
        /**
         * 如果是wrap_content设置为我们计算的值
         * 否则：直接设置为父容器计算的值
         */
        // 是精确的类型就直接取得到的值; 不是则用计算的值
        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width, modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int childWidth = 0;
        int childHeight = 0;
        MarginLayoutParams childParams = null;

        /**
         * 遍历所有childview根据其宽和高,以及margin进行布局
         */
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childWidth = childView.getMeasuredWidth();
            childHeight = childView.getMeasuredHeight();
            childParams = (MarginLayoutParams) childView.getLayoutParams();

            int cl = 0, ct = 0, cr = 0, cb = 0;
            switch (i) {
                case 0:
                    cl = childParams.leftMargin;
                    ct = childParams.topMargin;
                    break;
                case 1:
//                    cl = getMeasuredWidth() - childWidth - childParams.rightMargin;
                    // getMeasuredWidth()也是可以的,得到的是在ViewGroup里的宽
                    cl = getWidth() - childWidth - childParams.rightMargin;
                    ct = childParams.topMargin;
                    break;
                case 2:
                    cl = childParams.leftMargin;
//                    ct = getMeasuredHeight() - childHeight - childParams.bottomMargin;
                    ct = getHeight() - childHeight - childParams.bottomMargin;
                    break;
                case 3:
                    cl = getWidth() - childWidth - childParams.rightMargin;
                    ct = getHeight() - childHeight - childParams.bottomMargin;
                    break;
            }
            cr = childWidth + cl;
            cb = childHeight + ct;
            childView.layout(cl, ct, cr, cb);
        }
    }
}
