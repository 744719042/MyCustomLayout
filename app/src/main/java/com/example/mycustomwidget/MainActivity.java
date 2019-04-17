package com.example.mycustomwidget;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.example.mycustomwidget.comment.RatingBar;
import com.example.mycustomwidget.utils.UIUtils;

public class MainActivity extends AppCompatActivity {

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private RatingBar ratingBar;
    private boolean isShow = false;
    private int delta = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(getApplicationContext())) { //启动Activity让用户授权
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
    }

    public void showRatingBar(View view) {
        if (isShow) {
            return;
        }
        if (windowManager == null) {
            windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            ratingBar = new RatingBar(getApplicationContext());
            layoutParams = new WindowManager.LayoutParams();
            ratingBar.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            layoutParams.x = UIUtils.dp2px(150);
            layoutParams.y = UIUtils.dp2px(350);
            layoutParams.width = ratingBar.getMeasuredWidth();
            layoutParams.height = ratingBar.getMeasuredHeight();
            layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
            layoutParams.format = PixelFormat.RGBA_8888;
            if (Build.VERSION.SDK_INT>=26) {//8.0新特性
                layoutParams.type= WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            }else{
                layoutParams.type= WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        }

        try {
            windowManager.addView(ratingBar, layoutParams);
            isShow = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideRatingBar(View view) {
        if (windowManager == null || !isShow) {
            return;
        }

        try {
            windowManager.removeView(ratingBar);
            isShow = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void moveRatingBar(View view) {
        if (windowManager == null || !isShow) {
            return;
        }

        layoutParams.x += delta;
        layoutParams.y += delta;
        delta += Math.random() >= 0.5 ? 10 : -10;
        layoutParams.width = ratingBar.getMeasuredWidth();
        layoutParams.height = ratingBar.getMeasuredHeight();
        windowManager.updateViewLayout(ratingBar, layoutParams);
    }
}
