/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.z.lionel.zutil.adapter.viewPagerAdapter;

import android.content.Context;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import java.util.List;


public class MyPagerAdapter extends APagerAdapter {
    private List<View> list;

    public MyPagerAdapter(Context context, List<View> dataList) {
        super(context, dataList);
        this.list = dataList;
    }

    /**
     * 重新设置viewList
     */
    public void resetDatas(List<View> dataList) {
        list = dataList;
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public View getView(int position) {
        return list.get(position);
    }


    /**
     * 用法:
     * *MyPagerAdapter(context, viewPagerViewList).apply {
     * *                 setChangeListener(id_view_pager_config_devices_operator) {
     * *                    //监听 pageView 的变化...
     * *                     when (it) {
     * *                          0 -> id_popup_device_operation_title.text =
     * *                              context.resources.getString(R.string.config_lte_device_operation)
     * *                          // 1,2 目前 还没有的 ,留着呗..
     * *                          1 -> id_popup_device_operation_title.text =
     * *                              context.resources.getString(R.string.config_gsm_device_operation)
     * *                          2 -> id_popup_device_operation_title.text =
     * *                              context.resources.getString(R.string.config_cdma_device_operation)
     * *                      }
     * *                  }
     * *              }
     *
     * @param vp
     * @param pageChangeListener
     */
    public void setChangeListener(ViewPager vp, final PageChangeListener pageChangeListener) {
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currentPosition = 0;

            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                if (currentPosition != i) {
                    pageChangeListener.onPageScrollStateChanged(i);
                    currentPosition = i;
                }
               /* pageChangeListener.onPageScrollStateChanged(i);
                currentPosition = i;*/
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    public void setChangeListener(ViewPager vp, final PageChangeListener2 pageChangeListener2) {
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currentPosition = 0;

            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                if (currentPosition != i) {
                    pageChangeListener2.onPageScrollStateChanged(currentPosition, i);
                    currentPosition = i;
                }
               /* pageChangeListener2.onPageScrollStateChanged(currentPosition, i);
                currentPosition = i;*/
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    public interface PageChangeListener {
        void onPageScrollStateChanged(int i);
    }

    public interface PageChangeListener2 {
        void onPageScrollStateChanged(int o, int n);
    }
}

