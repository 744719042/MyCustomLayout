package com.example.mycustomwidget.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.mycustomwidget.R;
import com.example.mycustomwidget.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class AbNormalLayout extends ViewGroup {
    private AbNormalAdapter mAdapter;
    private DataSetObserver mObserver;
    private ILayoutManager mLayoutManager;
    private AttributeSet mAttributeSet;
    private int mCellPadding = UIUtils.dp2px(5);
    protected List<View> mDetachedViews = new ArrayList<>();

    public AbNormalLayout(Context context) {
        this(context, null);
    }

    public AbNormalLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbNormalLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mAttributeSet = attrs;
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AbNormalLayout);
            mCellPadding = array.getDimensionPixelSize(R.styleable.AbNormalLayout_cellPadding, mCellPadding);
            array.recycle();
        }
        init();
    }

    private void init() {
        mObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                requestLayout();
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
                requestLayout();
            }
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mLayoutManager = getLayoutManager();
        if (mLayoutManager == null) {
            setMeasuredDimension(0, 0);
            return;
        }

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(width, height);
            return;
        }

        if (widthMode != MeasureSpec.EXACTLY) {
            width = UIUtils.getScreenWidth();
        }

        setMeasuredDimension(width, mLayoutManager.onMeasure(width, getContext(), mAttributeSet));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        detachViews();
        setupViews();
        layoutChildren();
    }

    private void layoutChildren() {
        if (mLayoutManager != null) {
            mLayoutManager.onLayout(this, mCellPadding);
        }
    }

    private void setupViews() {
        AbNormalAdapter adapter = mAdapter;
        int count = adapter.getCount();
        int size = mDetachedViews.size();
        for (int i = 0; i < count; i++) {
            View view = null;
            if (size == count) {
                view = adapter.getView(i, mDetachedViews.get(i), this);
            } else {
                view = adapter.getView(i, null, this);
            }

            mLayoutManager.measureChildView(this, view, mCellPadding);
            addViewInLayout(view, i, view.getLayoutParams());
        }
        mDetachedViews.clear();
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(0, 0);
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        float mWidthRatio = 1.0f;
        float mHeightRatio = 1.0f;
        float mAspect = 0f;

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            if (attrs != null) {
                TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AbNormalLayout_Layout);
                mWidthRatio = array.getFloat(R.styleable.AbNormalLayout_Layout_widthRatio, mWidthRatio);
                mHeightRatio = array.getFloat(R.styleable.AbNormalLayout_Layout_heightRatio, mHeightRatio);
                mAspect = array.getFloat(R.styleable.AbNormalLayout_Layout_aspect, mAspect);
                array.recycle();
            }

            if (mWidthRatio <= 0f) {
                throw new IllegalArgumentException("widthRatio must be positive");
            }

            if (mHeightRatio <= 0f) {
                throw new IllegalArgumentException("heightRatio must be positive");
            }

            if (mAspect < 0f) {
                throw new IllegalArgumentException("aspect must be positive");
            }
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    private void detachViews() {
        int childCount = getChildCount();
        mDetachedViews.clear();
        if (childCount >= 2 && childCount <= 5) {
            if (mAdapter.getCount() == childCount) {
                for (int i = 0; i < childCount; i++) {
                    mDetachedViews.add(getChildAt(i));
                }
                removeAllViewsInLayout();
            }
        }
    }

    public void setAdapter(AbNormalAdapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(mObserver);
            this.mAdapter = null;
        }
        this.mAdapter = adapter;
        this.mAdapter.registerDataSetObserver(mObserver);
        requestLayout();
    }

    private ILayoutManager getLayoutManager() {
        int count = mAdapter.getCount();
        switch (count) {
            case 2: {
                if (mLayoutManager instanceof TwoCellLayoutManager) {
                    return mLayoutManager;
                }
                return new TwoCellLayoutManager();
            }
            case 3:
                if (mLayoutManager instanceof ThreeCellLayoutManager) {
                    return mLayoutManager;
                }
                return new ThreeCellLayoutManager();
            case 4:
                if (mLayoutManager instanceof FourCellLayoutManager) {
                    return mLayoutManager;
                }
                return new FourCellLayoutManager();
            case 5:
                if (mLayoutManager instanceof FiveCellLayoutManager) {
                    return mLayoutManager;
                }
                return new FiveCellLayoutManager();
        }

        return null;
    }
}