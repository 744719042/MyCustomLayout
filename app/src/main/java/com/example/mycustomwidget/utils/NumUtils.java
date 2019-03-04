package com.example.mycustomwidget.utils;

public class NumUtils {
    public static boolean equals(float x, float y) {
        return Math.abs(x - y) < 0.000001f;
    }

    public static boolean equals(double x, double y) {
        return Math.abs(x - y) < 0.000001;
    }
}
