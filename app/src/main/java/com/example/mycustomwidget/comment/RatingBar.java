package com.example.mycustomwidget.comment;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.mycustomwidget.R;
import com.example.mycustomwidget.utils.NumUtils;
import com.example.mycustomwidget.utils.UIUtils;

public class RatingBar extends View {
    private static final String TAG = "RatingBar";
    private Bitmap mEmptyBitmap;
    private Bitmap mFullBitmap;
    private static final int DEFAULT_MAX_VALUE = 10;
    private static final int DEFAULT_MIN_HEIGHT = UIUtils.dp2px(20);
    private static final int DEFAULT_CELL_PADDING = UIUtils.dp2px(5);
    private static final float PADDING_PERCENT = 0.05F;
    private int mMaxValue = DEFAULT_MAX_VALUE;
    private float mValue;
    private Rect mRect;
    private Rect mLeftRect;
    private Rect mRightRect;

    private Rect mTmpRect;
    private int mStarPadding = DEFAULT_CELL_PADDING;

    private Paint mPaint;

    public RatingBar(Context context) {
        this(context, null);
    }

    public RatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
            Drawable emptyDrawable = array.getDrawable(R.styleable.RatingBar_emptyImage);
            if (emptyDrawable instanceof BitmapDrawable) {
                mEmptyBitmap = ((BitmapDrawable) emptyDrawable).getBitmap();
            }
            Drawable fullDrawable = array.getDrawable(R.styleable.RatingBar_fullImage);
            if (fullDrawable instanceof BitmapDrawable) {
                mFullBitmap = ((BitmapDrawable) fullDrawable).getBitmap();
            }
            mStarPadding = array.getDimensionPixelSize(R.styleable.RatingBar_starPadding, mStarPadding);
            mValue = array.getFloat(R.styleable.RatingBar_value, mValue);
            mMaxValue = array.getInt(R.styleable.RatingBar_maxValue, mMaxValue);
            array.recycle();
        }
        init();
    }

    private void init() {
        if (mEmptyBitmap == null) {
            mEmptyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
        }
        if (mFullBitmap == null) {
            mFullBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.full);
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        mRect = new Rect();
        mLeftRect = new Rect();
        mRightRect = new Rect();
        mTmpRect = new Rect();
    }

    public void setMaxValue(int maxValue) {
        if (maxValue <= 0) {
            mMaxValue = DEFAULT_MAX_VALUE;
        } else {
            mMaxValue = maxValue;
        }
        setValue(0);
    }

    public void setValue(float value) {
        if (value < 0) {
            mValue = 0;
        }

        if (value > mMaxValue) {
            mValue = mMaxValue;
        }
        mValue = value;
        Log.e(TAG, "value = " + mValue);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
            if (height < DEFAULT_MIN_HEIGHT) {
                height = DEFAULT_MIN_HEIGHT;
            }
        } else {
            height = DEFAULT_MIN_HEIGHT;
        }

        int width = height * 5 + 4 * mStarPadding;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getMeasuredHeight();
        int drawableHeight = (int) (height * (1 - 2 * PADDING_PERCENT));
        int drawableWidth = drawableHeight;
        int top = (int) (PADDING_PERCENT * height);
        int left = 0;

        if (NumUtils.equals(mValue, 0)) {
            for (int i = 0; i < 5; i++) {
                left =  i * (drawableWidth + mStarPadding);
                mRect.set(left, top, left + drawableWidth, top + drawableHeight);
                canvas.drawBitmap(mEmptyBitmap, null, mRect, mPaint);
            }
        } else if (NumUtils.equals(mValue, mMaxValue)) {
            for (int i = 0; i < 5; i++) {
                left = i * (drawableWidth + mStarPadding);
                mRect.set(left, top, left + drawableWidth, top + drawableHeight);
                canvas.drawBitmap(mFullBitmap, null, mRect, mPaint);
            }
        } else {
            float value = mValue / mMaxValue * 5.0f;
            int full = (int) value;
            int empty = full + 1;

            for (int i = 0; i < full; i++) {
                left =  i * (drawableWidth + mStarPadding);
                mRect.set(left, top, left + drawableWidth, top + drawableHeight);
                canvas.drawBitmap(mFullBitmap, null, mRect, mPaint);
            }

            float fullPart = value - full;
            left = full * (drawableWidth + mStarPadding);
            mRect.set(left, top, (int) (left + drawableWidth * fullPart), top + drawableHeight);
            int realWidth = mFullBitmap.getWidth(), realHeight = mFullBitmap.getHeight();
            mLeftRect.set(0, 0, (int) (realWidth * fullPart), realHeight);
            canvas.drawBitmap(mFullBitmap, mLeftRect, mRect, mPaint);

            float emptyPart = 1.0f - fullPart;
            left = (int) (left + drawableWidth * fullPart);
            mRect.set(left, top, (int) (left + drawableWidth * emptyPart), top + drawableHeight);
            mRightRect.set((int) (realWidth * fullPart), 0, realWidth, realHeight);
            canvas.drawBitmap(mEmptyBitmap, mRightRect, mRect, mPaint);

            for (int i = empty; i < 5; i++) {
                left = i * (drawableWidth + mStarPadding);
                mRect.set(left, top, left + drawableWidth, top + drawableHeight);
                canvas.drawBitmap(mEmptyBitmap, null, mRect, mPaint);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        Log.e(TAG, "x = " + x + ", y = " + y);
        changeValue(x, y);
        return super.dispatchTouchEvent(event);
    }

    private void changeValue(int x, int y) {
        mTmpRect.set(0, 0, getWidth(), getHeight());
        int height = getMeasuredHeight();
        int drawableHeight = (int) (height * (1 - 2 * PADDING_PERCENT));
        int drawableWidth = drawableHeight;
        int left = 0;
        Log.e(TAG, "total rect = " + mTmpRect.flattenToString());
        if (mTmpRect.contains(x, y)) {
            for (int i = 0; i < 5; i++) {
                left = (drawableWidth + mStarPadding) * i;
                mTmpRect.set(left, 0, left + drawableWidth, height);
                Log.e(TAG, "current rect = " + mTmpRect.flattenToString());
                if (mTmpRect.contains(x, y)) {
                    float point = 1.0f * (x - left) / drawableWidth;
                    Log.e(TAG, "point = " + point);
                    float value = (i + point) / 5 * mMaxValue;
                    Log.e(TAG, "value = " + value);
                    setValue(value);
                    break;
                }
            }
        }
    }
}
