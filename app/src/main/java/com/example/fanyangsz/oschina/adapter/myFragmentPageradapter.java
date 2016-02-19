package com.example.fanyangsz.oschina.adapter;

import android.app.Fragment;
import android.app.FragmentManager;

import java.util.ArrayList;

/**
 * Created by fanyang.sz on 2016/1/8.
 */
public class myFragmentPageradapter extends FragmentPageradapter {

    ArrayList<Fragment> mFragment;
    public myFragmentPageradapter(FragmentManager fm,ArrayList<Fragment> mFragment){
        super(fm);
        this.mFragment = mFragment;
    }
    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }


}
