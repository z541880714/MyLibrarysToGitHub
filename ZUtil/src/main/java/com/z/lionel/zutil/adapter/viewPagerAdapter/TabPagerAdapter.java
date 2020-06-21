package com.z.lionel.zutil.adapter.viewPagerAdapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by solo on 2018/1/8.
 */

public class TabPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private FragmentManager fm;

    public TabPagerAdapter (FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fm = fm;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem (int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount () {
        return fragmentList.size();
    }
}
