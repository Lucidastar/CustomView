package com.mine.customviewtohy.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Parcelable;
import android.support.annotation.DimenRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.mine.customviewtohy.R;


/****
 * 环形进度控件
 */
public class RoundProgressView extends View {

	private Paint miniPaint;
	private float outProgressStrokeWidth;
	private float mProgressStrokeWidth;
	private int outProgressColor;
	private int mProgressUnFinishColor;
	private int mProgressFinishColor;
	private int mCenterRoundColor ;
	private float mTextSize;
	private int mTextColor;

	private float default_out_progress_stroke_width = 0;
	private float default_progress_stroke_width = 0;
	private int default_out_progress_color = Color.argb(255, 114, 202, 30);
	private int default_progress_unfinish_color = Color.argb(255, 234, 234, 234);
	private int default_progress_finish_color = Color.argb(255, 87, 182,0);
	private int default_center_round_color = Color.rgb(255, 255, 255);
	private int default_center_progress_color = Color.rgb(136,217,63);
	private float default_text_size ;
	private int default_text_color = Color.rgb(51, 51, 51);

	private int[] colors = new int[]{default_progress_finish_color, default_center_progress_color,default_progress_finish_color};
	private SweepGradient sweepGradient;
	private Matrix rotateMatrix;
	Rect rect = new Rect();

	/**小圆点的颜色值*/
	private int smallCircleColor = Color.rgb(151,234,82);
	private float smallCircleRadius = 0;
	private float mWidth,mHeight;
	private float mPaddingX = 0;
	private float mProgress = 0;
	private float mMax = 100f;

	private Paint mPaint = new Paint();
	private RectF oval;
	private Paint mCenterRoundPaint = new Paint();
	private int min_size;
	public RoundProgressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundProgressViewChange, defStyle, 0);
		initByAttributes(attributes);
		attributes.recycle();

	}

	private void initByAttributes(TypedArray attributes) {
		outProgressStrokeWidth = attributes.getDimension(R.styleable.RoundProgressViewChange_round_out_circle_stroke_width,default_out_progress_stroke_width);
		mProgressStrokeWidth = attributes.getDimension(R.styleable.RoundProgressViewChange_round_progress_stroke_width,default_progress_stroke_width);
		outProgressColor = attributes.getColor(R.styleable.RoundProgressViewChange_round_out_circle_color,default_out_progress_color);
		mProgressFinishColor = attributes.getColor(R.styleable.RoundProgressViewChange_round_progress_finish_color,default_progress_finish_color);
		mProgressUnFinishColor = attributes.getColor(R.styleable.RoundProgressViewChange_round_progress_unfinish_color,default_progress_unfinish_color);
		mCenterRoundColor = attributes.getColor(R.styleable.RoundProgressViewChange_round_center_circle_color,default_center_round_color);
		mTextSize = attributes.getDimension(R.styleable.RoundProgressViewChange_round_text_size,default_text_size);
		mTextColor = attributes.getColor(R.styleable.RoundProgressViewChange_round_text_color,default_text_color);

		smallCircleRadius = mProgressStrokeWidth / 2;
	}

	public RoundProgressView(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}

	public RoundProgressView(Context context) {
		this(context,null);

	}

	public void init(){
		//  小圆画笔
		miniPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		miniPaint.setStyle(Style.FILL);
		miniPaint.setStrokeJoin(Paint.Join.ROUND);
		miniPaint.setStrokeCap(Paint.Cap.ROUND);
		miniPaint.setColor(smallCircleColor);

		default_text_size = sp2px(18);
		default_out_progress_stroke_width = dp2px(4);
		default_progress_stroke_width = dp2px(20);
		min_size = (int)dp2px(120);
		rotateMatrix = new Matrix();

		mCenterRoundPaint.setAntiAlias(true);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
	}
	private int measure(int measureSpec) {
		int result;
		int mode = MeasureSpec.getMode(measureSpec);
		int size = MeasureSpec.getSize(measureSpec);
		if (mode == MeasureSpec.EXACTLY) {
			result = size;
		} else {
			result = min_size;
			if (mode == MeasureSpec.AT_MOST) {
				result = Math.min(result, size);
			}
		}
		return result;
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mWidth = getWidth();
		mHeight = getHeight();

		if(mWidth > mHeight){
			mPaddingX = (mWidth-mHeight)/2;
			mWidth = mHeight;
		}

		smallCircleRadius = mProgressStrokeWidth / 2;
		//最外部
		mPaint.setAntiAlias(true); // 消除锯齿
		mPaint.setStyle(Style.STROKE);
		mPaint.setColor(outProgressColor);
		mPaint.setStrokeWidth(outProgressStrokeWidth);
		oval = new RectF(mPaddingX+outProgressStrokeWidth, outProgressStrokeWidth, mWidth+mPaddingX-outProgressStrokeWidth, mHeight-outProgressStrokeWidth);
		canvas.drawArc(oval, 0, 360, true, mPaint);
		//进度条背景
		float halfWidth = mWidth/9;
		mPaint.setStrokeWidth(mProgressStrokeWidth);

		mPaint.setColor(mProgressUnFinishColor);
		mPaint.setStyle(Style.STROKE);
		oval = new RectF(outProgressStrokeWidth+mPaddingX+halfWidth, outProgressStrokeWidth+halfWidth, mWidth+mPaddingX-outProgressStrokeWidth-halfWidth, mHeight-outProgressStrokeWidth-halfWidth);
		canvas.drawArc(oval, 0, 360, false, mPaint);

		//绘制两端小圆点

		//求出圆心坐标
		float cX = mWidth / 2;
		float cY = (mWidth + mPaddingX) / 2;
		float angle = getProgress() / getMax() * 360;//旋转的角度
		float startAngle = 270;//开始角度
		oval = new RectF(outProgressStrokeWidth+mPaddingX+halfWidth, outProgressStrokeWidth+halfWidth, mWidth+mPaddingX-outProgressStrokeWidth-halfWidth, mHeight-outProgressStrokeWidth-halfWidth);
		//进度条的颜色

		//渐变
		sweepGradient = new SweepGradient(cX, cY, colors,null);
//		sweepGradient = new SweepGradient(cX, cY, colors, new float[]{1.0f,0.0f,1.0f});
		rotateMatrix.setRotate(startAngle, cX, angle);
		sweepGradient.setLocalMatrix(rotateMatrix);
		mPaint.setShader(sweepGradient);
//		mPaint.setColor(mProgressFinishColor);
		canvas.drawArc(oval, startAngle, angle, false, mPaint);

		//画第一个小圆点
		if (getMax() != getProgress()){
			canvas.drawCircle(mWidth / 2,outProgressStrokeWidth+halfWidth ,smallCircleRadius,miniPaint);
		}
		canvas.save();
		//让画布进行旋转
		canvas.rotate(angle,cX,cY);
		if (getMax() != getProgress()){
			canvas.drawCircle(mWidth / 2,outProgressStrokeWidth+halfWidth ,smallCircleRadius,miniPaint);
		}

		canvas.restore();

		//绘制中间圆

		mCenterRoundPaint.setStyle(Style.FILL);
		mCenterRoundPaint.setColor(mCenterRoundColor);
		oval = new RectF(outProgressStrokeWidth+mPaddingX+halfWidth+mProgressStrokeWidth / 2, outProgressStrokeWidth+halfWidth+mProgressStrokeWidth / 2, mWidth+mPaddingX-outProgressStrokeWidth-halfWidth-mProgressStrokeWidth/2, mHeight-outProgressStrokeWidth-halfWidth-mProgressStrokeWidth/2);
		canvas.drawArc(oval, 0, 360, true, mCenterRoundPaint);

		//绘制中间的数
		mPaint.reset();
		mPaint.setTextSize(mTextSize);
		mPaint.setColor(mTextColor);
		mPaint.setStyle(Style.FILL);
		mPaint.setTextAlign(Align.CENTER);
		mPaint.setAntiAlias(true);
//		String number = (int)(mProgress*100/mMax)+"";
		String progress = (int)mProgress+"/";
//		canvas.drawText((int)mProgress+"/"+(int)mMax, mWidth/2+mPaddingX, mHeight/2+mTextSize/3, mPaint);
		mPaint.getTextBounds(progress,0,progress.length(),rect);
		int left = rect.centerX();
		canvas.drawText(progress, mWidth/2+mPaddingX-left+12, mHeight/2+mTextSize/3, mPaint);//这个位置是正确的
		mPaint.setTextSize(mTextSize * 0.7f);
		String max = String.valueOf((int) mMax);
		mPaint.getTextBounds(max,0,max.length(),rect);
		canvas.drawText(max,mWidth/2+mPaddingX+rect.centerX()+11, mHeight/2+mTextSize/3, mPaint);

	}
	
	public void setMax(float mMax) {
		this.mMax = mMax;
		invalidate();
	}

	public void setProgress(float mProgress) {
		this.mProgress = mProgress;
		invalidate();
	}

	public float getMax() {
		return mMax;
	}

	public float getProgress() {
		return mProgress;
	}

	public void setTextColor(int color){
		this.mTextColor = color;
		invalidate();
	}

	public int getTextColor(){
		return mTextColor;
	}

	public void setOutProgressColor(int outProgressColor){
		this.outProgressColor = outProgressColor;
		invalidate();
	}

	public int getOutProgressColor(){
		return outProgressColor;
	}

	public float getOutProgressStrokeWidth(){
		return outProgressStrokeWidth;
	}

	public void setOutProgressStrokeWidth(float outProgressStrokeWidth){
		this.outProgressStrokeWidth = outProgressStrokeWidth;
		invalidate();
	}

	public void setProgressStrokeWidth(float progressStrokeWidth){
		this.mProgressStrokeWidth = progressStrokeWidth;
		invalidate();
	}

	public float getProgressStrokeWidth(){
		return mProgressStrokeWidth;
	}

	public void setTextSize(float textSize){
		this.mTextSize = textSize;
		invalidate();
	}

	public float getTextSize(){
		return mTextSize;
	}

	public float dp2px(float dp) {
		final float scale = getResources().getDisplayMetrics().density;
		return dp * scale + 0.5f;
	}

	public float sp2px(float sp) {
		final float scale = getResources().getDisplayMetrics().scaledDensity;
		return sp * scale;
	}


	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		super.onRestoreInstanceState(state);
	}

	@Nullable
	@Override
	protected Parcelable onSaveInstanceState() {
		return super.onSaveInstanceState();
	}
}
