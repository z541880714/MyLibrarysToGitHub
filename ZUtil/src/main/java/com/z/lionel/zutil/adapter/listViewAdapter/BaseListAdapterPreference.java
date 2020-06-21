package com.z.lionel.zutil.adapter.listViewAdapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import java.util.ArrayList;

abstract class BaseListAdapterPreference<T, E> extends BaseAdapter {
    ArrayList<T> mDatas = null;

    BaseListAdapterPreference(ArrayList<T> datas) {
        mDatas = datas;
    }

    public synchronized void setmDatasAndNotify(ArrayList<T> mDatas) {
        this.mDatas.clear();
        this.mDatas.addAll(mDatas);
        dataChangedNotify();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("zc", "BaseListAdapterPreference getView: 111");
        E vHolder = null;
        if (convertView == null) {
            convertView = getItemView();
            vHolder = getVHolder(convertView);
            convertView.setTag(vHolder);
        } else {
            vHolder = (E) convertView.getTag();
        }
        ArrayList<? extends View> views = getComponents(vHolder, position);
        doSomeThing(position, views);
        return convertView;
    }

    abstract void doSomeThing(int position, ArrayList<? extends View> views);

    abstract ArrayList<? extends View> getComponents(Object viewHolder, int position);

    abstract View getItemView();

    abstract E getVHolder(View convertView);


    /**
     * 数据已进行更新, 通知 实现类, 做对应的操作....
     */
    abstract void dataChangedNotify();


}
