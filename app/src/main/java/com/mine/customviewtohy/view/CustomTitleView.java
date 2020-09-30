package com.mine.customviewtohy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.mine.customviewtohy.R;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * Created by qiuyouzone on 2017/5/22.
 */

public class CustomTitleView extends View {

    private String titleText;
    private int titleTextColor;
    private int titleTextSize;
    private Paint mPaint;
    private Rect rect;
    private Random random = new Random();

    public CustomTitleView(Context context) {
        this(context, null);
    }

    public CustomTitleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttributeSet(context, attrs, defStyleAttr);
    }

    private void initAttributeSet(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTitleView, defStyleAttr, 0);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = typedArray.getIndex(i);
            switch (index) {
                case R.styleable.CustomTitleView_titleText:
                    titleText = typedArray.getString(index);
                    break;
                case R.styleable.CustomTitleView_titleTextColor:
                    titleTextColor = typedArray.getColor(index, Color.BLACK);
                    break;
                case R.styleable.CustomTitleView_titleTextSize:
                    titleTextSize = typedArray.getDimensionPixelSize(index, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,16,getResources().getDisplayMetrics()));
                    break;
            }
        }
        typedArray.recycle();
        initData();
    }

    private void initData() {
        mPaint = new Paint();
        rect = new Rect();
        mPaint.setTextSize(titleTextSize);
        mPaint.setColor(titleTextColor);
        mPaint.getTextBounds(titleText,0,titleText.length(),rect);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                titleText = getRandom();
                invalidate();
            }
        });
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        int height = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY){//给的具体值,或者是match_parent属性
            width = widthSize + getPaddingLeft() + getPaddingRight();
        }else if (widthMode == MeasureSpec.AT_MOST){//一般是wrap_content
            float v = mPaint.measureText(titleText);
            width = (int)(v+getPaddingLeft()+getPaddingRight());
        }else if (widthMode == MeasureSpec.UNSPECIFIED){
            float v = mPaint.measureText(titleText);
            width = (int)(v+getPaddingLeft()+getPaddingRight());
        }


        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY){//给的具体值,或者是match_parent属性
            height = heightSize + getPaddingTop() + getPaddingBottom();
        }else if (heightMode == MeasureSpec.AT_MOST){//一般是wrap_content
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            float textHeight = Math.abs((fontMetrics.bottom - fontMetrics.top));
            height = (int)(textHeight+getPaddingTop()+getPaddingBottom());
        }else if (heightMode == MeasureSpec.UNSPECIFIED){
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            float textHeight = Math.abs((fontMetrics.bottom - fontMetrics.top));
            height = (int)(textHeight+getPaddingTop()+getPaddingBottom());
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLUE);

        mPaint.setAntiAlias(true);
        mPaint.setColor(titleTextColor);
        mPaint.setTextSize(titleTextSize);
        canvas.drawText(titleText,getWidth()/ 2 - rect.width() / 2- rect.left,getHeight() / 2 + rect.height() / 2,mPaint);
//        canvas.drawText(titleText,getWidth()/ 2,getHeight() / 2 ,mPaint);
//        canvas.drawText(titleText,getMeasuredWidth() / 2,getMeasuredHeight() / 2,mPaint);
        Log.e("---->", "getWidth():" + getWidth());
        Log.e("---->", "rect.width():" + rect.width());
        Log.e("---->", "rect.left():" + rect.left);
        Log.e("---->", "rect.Right():" + rect.right);

        Log.e("---->", "getMeasuredWidth():" + getMeasuredWidth());
        Log.e("---->", "mRect.width():" + rect.width());
        Log.e("---->", "paddingLeft:" + getPaddingLeft());
        Log.e("---->", "PaddingRight():" + getPaddingRight());

    }


    private String getRandom(){

        HashSet<Integer> hashSet = new HashSet<>();
        while (hashSet.size() < 4){
            int ran = random.nextInt(10);
            hashSet.add(ran);
        }
        StringBuilder stringBuilder = new StringBuilder();
       if (!hashSet.isEmpty()){
           Iterator<Integer> iterator = hashSet.iterator();
           while (iterator.hasNext()){
               stringBuilder.append(iterator.next());
           }
       }
        return stringBuilder.toString();
    }
}
