package com.example.mycustomwidget.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class UIUtils {
    private static Context sContext;
    private static int width;
    private static int height;

    public static void init(Context context) {
        sContext = context;
    }
    public static int getScreenWidth() {
        if (sContext != null && width == 0) {
            DisplayMetrics displayMetrics = sContext.getResources().getDisplayMetrics();
            width = displayMetrics.widthPixels;
        }

        return width;
    }

    public static int getScreenHeight() {
        if (sContext != null && height == 0) {
            DisplayMetrics displayMetrics = sContext.getResources().getDisplayMetrics();
            height = displayMetrics.heightPixels;
        }
        return height;
    }

    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }
}
