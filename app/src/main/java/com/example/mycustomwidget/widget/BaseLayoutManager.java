package com.example.mycustomwidget.widget;

import android.view.View;
import android.view.ViewGroup;

import com.example.mycustomwidget.utils.NumUtils;

public abstract class BaseLayoutManager implements ILayoutManager {
    protected float mAspect = 0.5f;

    protected void measure(View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (!(layoutParams instanceof AbNormalLayout.LayoutParams)) {
            throw new IllegalArgumentException("child layoutParam is not AbNormalLayout.LayoutParams");
        }

        AbNormalLayout.LayoutParams params = (AbNormalLayout.LayoutParams) layoutParams;
        params.width = (int) (width * params.mWidthRatio);
        if (!NumUtils.equals(params.mAspect, 0f)) { // 如果设置了aspect
            params.height = (int) (params.width * params.mAspect);
        } else {
            params.height = (int) (height * params.mHeightRatio);
        }

        view.measure(View.MeasureSpec.makeMeasureSpec(params.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(params.height, View.MeasureSpec.EXACTLY));
    }
}
