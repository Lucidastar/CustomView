package com.mine.customviewtohy.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;

import com.mine.customviewtohy.R;
import com.mine.customviewtohy.validation.Density;

import java.util.Arrays;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

/**
 * Created by qiuyouzone on 2017/6/22.
 */

public class CustomPayPsd extends AppCompatEditText {

    private int padStyle = 0;
    private final static int psd_weixin = 0;
    private final static int psd_bottomline = 1;

    private RectF mRectf;//画矩形
    private int deliverLineWidth = Density.dp2px(getContext(),0.5f);//分割线的宽度
    private Paint mDeliverPaint;
    private int mFrameColor = Color.parseColor("#DCDCDC");//边框的颜色
    private int mLineColor = Color.parseColor("#D3D3D3");//线条的颜色
    private int mHeight;
    private int mWidth;
    private int mPsdCount = 6;//密码的个数


    private Paint framePaint;
    private int frameStrokeWidth = 2;

    private OnCompleteListener mOnCompleteListener;

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        mOnCompleteListener = onCompleteListener;
    }

    public CustomPayPsd(Context context) {
        this(context,null);
    }

    public CustomPayPsd(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomPayPsd(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomPayPsd, defStyleAttr, 0);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = typedArray.getIndex(i);
            switch (index){
                case R.styleable.CustomPayPsd_psdType:
                    padStyle = typedArray.getInt(R.styleable.CustomPayPsd_psdType,psd_weixin);
                    break;
            }
        }

        typedArray.recycle();
        initPaint();
        this.setBackgroundColor(Color.WHITE);
        this.setCursorVisible(false);
        this.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mPsdCount)});
        mRectf = new RectF();
    }

    private void initPaint() {
        framePaint = new Paint(ANTI_ALIAS_FLAG);
        framePaint.setStrokeWidth(0);
        framePaint.setColor(mFrameColor);
        framePaint.setStyle(Paint.Style.STROKE);


        mDeliverPaint = new Paint(ANTI_ALIAS_FLAG);
        mDeliverPaint.setStrokeWidth(deliverLineWidth);
        mDeliverPaint.setColor(mLineColor);
        mDeliverPaint.setStyle(Paint.Style.STROKE);

        mCirclePaint = new Paint(ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.FILL);

        setFocusableInTouchMode(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBorder(canvas);
        drawPsdCircle(canvas);
    }

    //画圆点
    private Paint mCirclePaint;
    private int mCircleColor = Color.BLACK;
    private int mCircleRadius = Density.dp2px(getContext(),3);

    private float mCircleCenterX;
    private float mCircleCenterY;

    private int mInputSum = 0;
    private void drawPsdCircle(Canvas canvas) {
        if (mInputSum > 0){
            for (int i = 0; i < mInputSum; i++) {
                canvas.drawCircle(mCircleCenterX +mDeliverStartX * i,mCircleCenterY,mCircleRadius,mCirclePaint);
            }
        }
    }

    private int mDeliverStartX;
    //画线
    private void drawBorder(Canvas canvas) {
        canvas.drawRoundRect(mRectf,3,3,framePaint);
        int startX = mDeliverStartX;
        for (int i = 1; i <= mPsdCount; i++) {
            canvas.drawLine(startX,0,startX, mHeight,mDeliverPaint);
            startX = i * mDeliverStartX;
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        mInputSum = text.length();
        if (mInputSum == mPsdCount && mOnCompleteListener != null){
            Toast.makeText(getContext(),"输入了"+getPsdString(),Toast.LENGTH_LONG).show();
            mOnCompleteListener.inputString(getPsdString());
        }
        Log.d("custom", "onTextChanged: "+start);
        invalidate();
    }

    public String getPsdString() {
        return getText().toString().trim();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mDeliverStartX = w / mPsdCount;
        mCircleCenterX = w / mPsdCount / 2;
        mCircleCenterY = mHeight / 2;
        mRectf.set(0,0,mWidth, mHeight);
    }

    public interface OnCompleteListener{
        void inputString(String content);
    }
}
