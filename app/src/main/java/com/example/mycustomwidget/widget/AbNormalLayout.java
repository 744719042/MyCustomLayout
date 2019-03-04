package com.example.mycustomwidget.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class AbNormalLayout extends ViewGroup {
    public AbNormalLayout(Context context) {
        this(context, null);
    }

    public AbNormalLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbNormalLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
