package com.mine.customviewtohy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.mine.customviewtohy.R;

/**
 * Created by qiuyouzone on 2017/5/25.
 */

public class CustomVolumControlBar extends View {

    private int mFirstColor;
    private int mSecondColor;
    private int mCircleWidth;
    private Paint mPaint;
    private int mCurrentCount = 6;
    private Bitmap mImage;
    private int mSplitSize;
    private int mCount;
    private Rect mRect;

    public CustomVolumControlBar(Context context) {
        this(context,null);
    }

    public CustomVolumControlBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomVolumControlBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);

    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomVolumControlBar, defStyleAttr, 0);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = typedArray.getIndex(i);
            switch (index){
                case R.styleable.CustomVolumControlBar_vFirstColor:
                    mFirstColor = typedArray.getColor(index, Color.GREEN);
                    break;

                case R.styleable.CustomVolumControlBar_vSecondColor:
                    mSecondColor = typedArray.getColor(index,Color.WHITE);
                    break;
                case R.styleable.CustomVolumControlBar_vCircleWidth:
                    mCircleWidth = typedArray.getDimensionPixelSize(index, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,10,context.getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomVolumControlBar_vDotCount:
                    mCount = typedArray.getInt(index,12);
                    break;
                case R.styleable.CustomVolumControlBar_vSplitSize:
                    mSplitSize = typedArray.getInt(index,20);
                    break;
                case R.styleable.CustomVolumControlBar_vBg:
                    mImage = BitmapFactory.decodeResource(getResources(),typedArray.getResourceId(index,0));
                    break;
            }
        }
        typedArray.recycle();

        mPaint = new Paint();
        mRect = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        int center = getWidth() / 2;
        int radius = center - mCircleWidth / 2;
        drawOval(canvas,center,radius);

/**
 * 计算内切正方形的位置
 */
        int relRadius = radius - mCircleWidth / 2;// 获得内圆的半径

        /**
         * 内切正方形距离左边的距离(或顶部):
         * (内圆半径 -  (更2 / 2) * 内圆半径) + 圆弧的宽度
         */
        mRect.left = (int) (relRadius - Math.sqrt(2) / 2 * relRadius) + mCircleWidth;
        mRect.top = (int) (relRadius - Math.sqrt(2) / 2 * relRadius) + mCircleWidth;
        /**
         * 内切正方形距离左边的距离 + 正方形的边长(Math.sqrt(2) * relRadius)
         */
        mRect.right = (int) (mRect.left + Math.sqrt(2) * relRadius);
        mRect.bottom = (int) (mRect.left + Math.sqrt(2) * relRadius);


        /**
         * 如果图片比较小,那么根据图片的尺寸放置到正中心
         */
        if (mImage.getWidth() < Math.sqrt(2) * relRadius) {
            mRect.left = mCircleWidth + (relRadius - mImage.getWidth() / 2);
            mRect.top = mCircleWidth + (relRadius - mImage.getWidth() / 2);
            mRect.right = mCircleWidth + (relRadius + mImage.getWidth() / 2);
            mRect.bottom = mCircleWidth + (relRadius + mImage.getWidth() / 2);
        }
        canvas.drawBitmap(mImage, null, mRect, mPaint);
        
    }

    //画小块
    private void drawOval(Canvas canvas, int center, int radius) {
        float itemSize = (360 * 1.0f - mCount*mSplitSize) / mCount;

        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);

        /**
         * 画圆环的第一种颜色
         */
        mPaint.setColor(mFirstColor);// 设置圆环的颜色
        for (int i = 0; i < mCount; i++) {
            // 根据进度画圆弧
            canvas.drawArc(oval, (i * (itemSize + mSplitSize)), itemSize, false, mPaint);

        }

        mPaint.setColor(mSecondColor);
        for (int i = 0; i < mCurrentCount; i++) {
            // 根据进度画圆弧
//            canvas.drawArc(oval, (i * (itemSize + mSplitSize)), itemSize, false, mPaint);
            canvas.drawArc(oval, (i * (itemSize + mSplitSize)), itemSize, false, mPaint);
        }

    }

    /**
     * 添加触摸监听
     * 当前数量+1
     */
    public void up() {
        if (mCurrentCount < mCount) {
            mCurrentCount++;
        }
        postInvalidate();
    }

    public void down() {
        if (mCurrentCount > 0) {
            mCurrentCount--;
        }
        postInvalidate();
    }

    private int xDown, xUp;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                xUp = (int) event.getY();
                if (xDown < xUp) {
                    down();
                } else {
                    up();
                }
                break;
        }
        return true;
    }
}
