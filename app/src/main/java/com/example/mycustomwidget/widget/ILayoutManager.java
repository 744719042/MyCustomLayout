package com.example.mycustomwidget.widget;

public interface ILayoutManager {
    void onMeasure(AbNormalLayout layout, int widthMeasure, int heightMeasure);
    void onLayout(AbNormalLayout layout, boolean changed, int left, int top, int right, int bottom);
}
