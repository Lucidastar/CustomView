package com.mine.customviewtohy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.mine.customviewtohy.R;

/**
 * Created by qiuyouzone on 2017/5/23.
 */

public class CustomImageView extends View {

    private String mTitleText;
    private int mTextColor;
    private int mTextSize;
    private Bitmap mBitmap;
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    // 用来画图片使用
    private Rect mRect;
    // 用来画文字使用
    private Rect mTextBound;
    // 图片缩放形式
    private int mImageScaleType;
    // 缩放形式
    private int IMAGE_SCALE_FITXY = 0;
    private int IMAGE_SCALE_CENTER = 1;

    public CustomImageView(Context context) {
        this(context,null);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomImageView, defStyleAttr, 0);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0 ; i < indexCount;i++){
            int index = typedArray.getIndex(i);
            switch (index){
                case R.styleable.CustomImageView_imageTitleText:
                        mTitleText = typedArray.getString(index);
                    break;
                case R.styleable.CustomImageView_imageTitleTextColor:
                    mTextColor = typedArray.getColor(index, Color.BLACK);
                    break;

                case R.styleable.CustomImageView_imageTitleTextSize:
                    mTextSize = typedArray.getDimensionPixelSize(index, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,16,context.getResources().getDisplayMetrics()));
                    break;

                case R.styleable.CustomImageView_imageScaleType:
                    mImageScaleType = typedArray.getInt(index,0);
                    break;
                case R.styleable.CustomImageView_image:
                    mBitmap = BitmapFactory.decodeResource(context.getResources(),typedArray.getResourceId(index,0));
                    break;
            }
        }
        typedArray.recycle();
        mPaint = new Paint();
        mTextBound = new Rect();//字体的测量
        mRect = new Rect();//图片的路径
        mPaint.setTextSize(mTextSize);
        mPaint.getTextBounds(mTitleText,0,mTitleText.length(),mTextBound);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY){//一般是具体的值，或者是match_parent
                mWidth = specSize;
        }else {
            int mImageWidth = getPaddingLeft()+getPaddingRight()+mBitmap.getWidth();//图片的宽度
            int mTextWidth = getPaddingLeft()+getPaddingRight()+mTextBound.width();//文字的宽度
            int des = Math.max(mImageWidth,mTextWidth);
            mWidth = Math.min(des,specSize);
        }

        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY){
            mHeight = specSize;
        }else {
            int des = getPaddingTop()+getPaddingBottom()+mBitmap.getHeight()+mTextBound.height();
            //如果height是match_parent，那就取最小的
            if (specMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(des, specSize);
            }
        }
            setMeasuredDimension(mWidth,mHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //进行绘制
        //绘制外边的边框
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        canvas.drawRect(0,0,mWidth,mHeight,mPaint);


        //如果字体的宽度大于mWidth，将字体改为xxx
        int w = mTextBound.width();
        mRect.left = getPaddingLeft();
        mRect.right = mWidth - getPaddingRight();
        mRect.top = getPaddingTop();
        mRect.bottom = mHeight - getPaddingBottom();
        //画字体
        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        if (mTextBound.width() > mWidth){
            TextPaint textPaint = new TextPaint(mPaint);
            String msg = TextUtils.ellipsize(mTitleText,textPaint,mWidth - getPaddingLeft()-getPaddingRight(),TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg,getPaddingLeft() / 2, mHeight - getPaddingBottom(), mPaint);
        }else {
            canvas.drawText(mTitleText, mWidth / 2 - mTextBound.width() / 2, mHeight - getPaddingBottom(), mPaint);
        }

        mRect.bottom -= mTextBound.height();
        if (mImageScaleType == IMAGE_SCALE_FITXY){
            canvas.drawBitmap(mBitmap,null,mPaint);
        }else {//居中显示
            mRect.left = mWidth / 2 - mBitmap.getWidth() / 2;
            mRect.right = mWidth / 2 + mBitmap.getWidth()/2;
            mRect.top = (mHeight - mTextBound.height()) / 2 - mBitmap.getHeight() / 2;
            mRect.bottom = (mHeight - mTextBound.height()) / 2 + mBitmap.getHeight() / 2;
            canvas.drawBitmap(mBitmap,null,mRect,mPaint);

        }


    }
}
