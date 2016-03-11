package com.example.fanyangsz.oschina.view.CircleView;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.adapter.myFragmentPageradapter;
import com.example.fanyangsz.oschina.view.NewsView.NewsContentFourFragment;
import com.example.fanyangsz.oschina.view.NewsView.NewsContentThreeFragment;
import com.example.fanyangsz.oschina.view.NewsView.NewsContentTwoFragment;

import java.util.ArrayList;

/**
 * Created by fanyang.sz on 2016/1/7.
 */
public class CircleViewpagerFragment extends Fragment  {

    private ArrayList<Fragment> mFragment;
    View view;
    ViewPager viewPager;
    TextView title1;
    TextView title2;
    TextView title3;
    ImageView imageView;

    CircleContentOneFragment mCircleContentOneFragment = new CircleContentOneFragment();
    NewsContentTwoFragment mNewsContentTwoFragment = new NewsContentTwoFragment();
    NewsContentThreeFragment mNewsContentThreeFragment = new NewsContentThreeFragment();
    NewsContentFourFragment mNewsContentFourFragment = new NewsContentFourFragment();

    private int currIndex = 1;// 当前页卡编号
    private int ivCursorWidth;// 动画图片宽度
    private int tabWidth;// 每个tab头的宽度
    private int offsetX;// tab头的宽度减去动画图片的宽度再除以2（保证动画图片相对tab头居中）

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_circle_viewpager, container, false);
        InitViewPager();
        InitTextView();
        InitImageView();
        return view;
    }
    private void InitViewPager() {
        viewPager = (ViewPager) view.findViewById(R.id.circle_viewpager);
        mFragment = new ArrayList<Fragment>();
        mFragment.add(mCircleContentOneFragment);
        mFragment.add(mNewsContentTwoFragment);
        mFragment.add(mNewsContentThreeFragment);
        viewPager.setAdapter(new myFragmentPageradapter(getFragmentManager(),mFragment));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }


    private void InitTextView() {
        title1 = (TextView) view.findViewById(R.id.circle_viewpager_title_1);
        title2 = (TextView) view.findViewById(R.id.circle_viewpager_title_2);
        title3 = (TextView) view.findViewById(R.id.circle_viewpager_title_3);

        title1.setTextColor(Color.GREEN);

        title1.setOnClickListener(new MyOnClickListener(0));
        title2.setOnClickListener(new MyOnClickListener(1));
        title3.setOnClickListener(new MyOnClickListener(2));
    }

    private void InitImageView() {
        imageView = (ImageView) view.findViewById(R.id.circle_viewpager_cursor);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenW = dm.widthPixels;// 获取分辨率宽度
        ivCursorWidth = BitmapFactory.decodeResource(getResources(),
                R.drawable.viewpager_cursor).getWidth();// 获取图片宽度

        tabWidth = screenW / 3;
        if (ivCursorWidth > tabWidth) {
            imageView.getLayoutParams().width = tabWidth;
            ivCursorWidth = tabWidth;
        }
        offsetX = (tabWidth - ivCursorWidth) / 2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(!getActivity().isDestroyed()) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.remove(mCircleContentOneFragment);
            transaction.remove(mNewsContentTwoFragment);
            transaction.remove(mNewsContentThreeFragment);
            transaction.commit();
        }
    }


    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;

        }

        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(index);

        }
    }

    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = new TranslateAnimation(tabWidth * currIndex
                    + offsetX, tabWidth * arg0 + offsetX, 0, 0);// 显然这个比较简洁，只有一行代码。
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(350);
            imageView.startAnimation(animation);

            switch(arg0){
                case 0:
                    title1.setTextColor(Color.GREEN);
                    title2.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
                    title3.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
                    break;
                case 1:
                    title1.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
                    title2.setTextColor(Color.GREEN);
                    title3.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
                    break;
                case 2:
                    title1.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
                    title2.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
                    title3.setTextColor(Color.GREEN);
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    }
}
