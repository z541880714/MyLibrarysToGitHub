package com.z.lionel.zutil.adapter.viewPagerAdapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public abstract class APagerAdapter<T> extends PagerAdapter {

    private SparseArray<View> mViewSparseArray;

    APagerAdapter (Context context, List<T> dataList) {
        mViewSparseArray = new SparseArray<>(dataList.size());
    }

    void resetViews (List<T> datas) {
        mViewSparseArray = new SparseArray<>(datas.size());
    }

    @Override
    public abstract int getCount ();

    @Override
    public boolean isViewFromObject ( View view,  Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem ( ViewGroup container, int position) {
        View view = mViewSparseArray.get(position);
        if (view == null) {
            view = getView(position);
            mViewSparseArray.put(position, view);
        } else if (view.getParent() != null) {
            ViewGroup vg = (ViewGroup) view.getParent();
            vg.removeView(view);
        }
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem ( ViewGroup container, int position,  Object object) {
        container.removeView(mViewSparseArray.get(position));

    }

    public abstract View getView (int position);


}


