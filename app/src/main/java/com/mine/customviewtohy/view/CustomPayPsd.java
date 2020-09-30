package com.mine.customviewtohy.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatEditText;

import com.mine.customviewtohy.R;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

/**
 * Created by qiuyouzone on 2017/6/22.
 */

public class CustomPayPsd extends AppCompatEditText {

    private int padStyle = 0;
    private final static int psd_weixin = 0;
    private final static int psd_bottomline = 1;

    private RectF mRectf;//画矩形
    private int deliverLineWidth = 2;//分割线的宽度
    private int frameColor = Color.BLACK;//边框的颜色
    private int lineColor;//线条的颜色
    private int height;
    private int width;
    private int psdCount;//密码的个数


    private Paint framePaint;
    private int frameStrokeWidth = 2;
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
        this.setBackgroundColor(Color.TRANSPARENT);
        this.setCursorVisible(false);
        mRectf = new RectF();
    }

    private void initPaint() {
        framePaint = new Paint(ANTI_ALIAS_FLAG);
        framePaint.setStrokeWidth(frameStrokeWidth);
        framePaint.setColor(frameColor);
        framePaint.setStyle(Paint.Style.STROKE);

        framePaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        canvas.drawRect(mRectf,framePaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i("mine", "onSizeChanged: ");
        width = w;
        height = h;

        mRectf.set(0,0,w,height);
    }
}
