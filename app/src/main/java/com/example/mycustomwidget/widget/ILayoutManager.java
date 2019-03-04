package com.example.mycustomwidget.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public interface ILayoutManager {
    int onMeasure(int width, Context context, AttributeSet attrs);
    void onLayout(AbNormalLayout layout, int cellPadding);
    void measureChildView(AbNormalLayout parent, View view, int cellPadding);
}
