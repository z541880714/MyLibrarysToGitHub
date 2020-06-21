package com.z.lionel.zutil.adapter.listViewAdapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

abstract class BaseListAdapterPreference1<T> extends BaseAdapter {
    ArrayList<T> mDatas = null;

    public BaseListAdapterPreference1 (ArrayList<T> datas) {
        mDatas = datas;
    }

    @Override
    public int getCount () {
        return mDatas.size();
    }

    @Override
    public Object getItem (int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        ViewHolder vHolder = null;
        if (convertView == null) {
            convertView = getItemView();
            vHolder = new ViewHolder(convertView);
            convertView.setTag(vHolder);
        } else {
            vHolder = (ViewHolder) convertView.getTag();
        }

        doSomeThing(position, vHolder.contetViews);
        return convertView;
    }

    public abstract void doSomeThing (int position, ArrayList<? extends View> contetViews);

    public abstract View getItemView ();

    public abstract ArrayList<View> initComponent (View view);

    class ViewHolder {
        View view;
        ArrayList<? extends View> contetViews;

        ViewHolder (View view) {
            this.view = view;
            contetViews = initComponent(view);
        }
    }


}
