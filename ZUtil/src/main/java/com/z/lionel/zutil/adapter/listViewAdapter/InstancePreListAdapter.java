package com.z.lionel.zutil.adapter.listViewAdapter;

import android.view.View;


import java.util.ArrayList;

public abstract class InstancePreListAdapter<T> extends
        BaseListAdapterPreference<T, InstancePreListAdapter.ViewHolder> {

    public InstancePreListAdapter(ArrayList<T> datas) {
        super(datas);
    }

    public ArrayList<T> getDatas() {
        return mDatas;
    }


    /**
     * 给viewHolder 创建一个实例, 给他一个标记,标记当前的位置 .
     * 并且给作为tag 设置给子组件们, 目的是 ,通过 holderview 或者子组件, 都能获取到当前组件所在的位置
     * 当前组件所在的位置.
     *
     * @param view
     */
    public ViewHolder getVHolder(View view) {
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        holder.contetViews = initComponent(view);
        for (View v : holder.contetViews) {
            v.setTag(holder);
        }
        return holder;
    }

    /**
     * item 中, 某个组件的子插件 获取当前的位置....
     *
     * @param childView itemView 及其子组件....
     * @return
     */
    public int getPosition(View childView) {
        Mark mark = (Mark) childView.getTag();
        return mark.getMark();
    }

    @Override
    ArrayList<? extends View> getComponents(Object viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.position = position;
        return holder.contetViews;
    }

    public abstract ArrayList<View> initComponent(View view);

    @Override
    public abstract View getItemView();

    @Override
    public void dataChangedNotify() {
    }

    @Override
    public abstract void doSomeThing(int position, ArrayList<? extends View> views);

    /**
     * 每个组件都会与viewHolder  绑定... 不会改变的.
     * 如果通过组件获取到viewHolder , 就能获取当 data 的实时 数据.. 就可以做相应的更改了...
     */
    class ViewHolder implements Mark {
        //每个 viewHolder 实时都会 含有一个 dataList  的某一个位置的 数据...
        int position;
        View view;
        ArrayList<? extends View> contetViews;

        ViewHolder(View view) {
            this.view = view;
        }

        @Override
        public int getMark() {
            return position;
        }
    }


    /**
     * 让 ViewHolder 实现 这个mark 接口.. 让子列表可以通过 mark  与 组件绑定..
     * 然后可以通过获取Mark 来获取mark 的内容
     * <p>
     * 例:
     * val mark = linearLayout.tag as Mark
     * Log.i("zc", "initComponent:position ${mark.mark as Int}")
     */
    public interface Mark {
        int getMark();
    }
}

