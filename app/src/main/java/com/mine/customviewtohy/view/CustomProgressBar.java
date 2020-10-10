package com.mine.customviewtohy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.mine.customviewtohy.R;

/**
 * Created by qiuyouzone on 2017/5/24.
 */

public class CustomProgressBar extends View {

    private int mFirstColor;
    private int mSecondColor;
    private int mProgressStrokeWidth;
    private int mSpeed;
    private boolean isContinue;
    private Paint mPaint;
    private RectF mRectF;
    private int mCurrentProgress;

    private boolean isNext = false;

    public CustomProgressBar(Context context) {
        this(context,null);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, defStyleAttr, 0);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = typedArray.getIndex(i);
            switch (index){
                case R.styleable.CustomProgressBar_circleWidth:
                    mProgressStrokeWidth = typedArray.getDimensionPixelSize(index, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomProgressBar_firstColor:
                    mFirstColor = typedArray.getColor(index, Color.BLUE);
                    break;
                case R.styleable.CustomProgressBar_secondColor:
                    mSecondColor = typedArray.getColor(index,Color.RED);
                    break;
                case R.styleable.CustomProgressBar_speed:
                    mSpeed = typedArray.getInt(index,20);
                    break;
            }
        }
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setStrokeWidth(mProgressStrokeWidth);
        mPaint.setAntiAlias(true);
        mRectF = new RectF();
        isContinue = true;
        new Thread(){
            @Override
            public void run() {
                while (isContinue){
                    mCurrentProgress++;
                    if (mCurrentProgress == 360){
                        mCurrentProgress = 0;
                        isNext = !isNext;
                    }
                    try {
                        Thread.sleep(100 / mSpeed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    postInvalidate();
                }
            }
        }.start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Log.i("---->", "onDraw: ");
        int center = getWidth() / 2;
        int radius = center - mProgressStrokeWidth / 2;
        mRectF.left = center - radius;
        mRectF.top = center - radius;
        mRectF.right = center + radius;
        mRectF.bottom = center + radius;

        mPaint.setStyle(Paint.Style.STROKE);
        if (!isNext){
            mPaint.setColor(mFirstColor);
            canvas.drawCircle(center,center,radius,mPaint);
            mPaint.setColor(mSecondColor);
            canvas.drawArc(mRectF,-90,mCurrentProgress,false,mPaint);
        }else {
            mPaint.setColor(mSecondColor);
            canvas.drawCircle(center,center,radius,mPaint);
            mPaint.setColor(mFirstColor);
            canvas.drawArc(mRectF,-90,mCurrentProgress,false,mPaint);
        }

    }
    private int width;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);

        if (modeWidth == MeasureSpec.EXACTLY) {
            width = sizeWidth;
        } else {//默认宽度
            width = (int) getContext().getResources().getDimension(R.dimen.width);
        }

//        Log.i("---->", "onMeasure: ");
        setMeasuredDimension(width, width);
    }
}
