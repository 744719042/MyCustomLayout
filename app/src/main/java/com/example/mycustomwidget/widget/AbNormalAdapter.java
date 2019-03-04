package com.example.mycustomwidget.widget;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbNormalAdapter<T> extends BaseAdapter {
    private Context mContext;
    private List<T> mData = new ArrayList<>();

    public AbNormalAdapter(Context context, List<T> data) {
        this.mContext = context;
        this.mData.addAll(data);
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public void replace(List<T> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }
}
