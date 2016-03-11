package com.example.fanyangsz.oschina.view.NewsView;

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

import java.util.ArrayList;

/**
 * Created by fanyang.sz on 2016/1/8.
 */
public class NewsViewpagerFragment extends Fragment{

    private ArrayList<Fragment> mFragment;
    private View view;
    ViewPager viewPager;
    TextView title1;
    TextView title2;
    TextView title3;
    TextView title4;
    ImageView imageView;

    NewsContentOneFragment mNewsContentOneFragment = new NewsContentOneFragment();
    NewsContentTwoFragment mNewsContentTwoFragment = new NewsContentTwoFragment();
    NewsContentThreeFragment mNewsContentThreeFragment = new NewsContentThreeFragment();
    NewsContentFourFragment mNewsContentFourFragment = new NewsContentFourFragment();

    private int currIndex = 1;// 当前页卡编号
    private int ivCursorWidth;// 动画图片宽度
    private int tabWidth;// 每个tab头的宽度
    private int offsetX;// tab头的宽度减去动画图片的宽度再除以2（保证动画图片相对tab头居中）

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_news_viewpager, container, false);
        InitViewPager();
        InitTextView();
        InitImageView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        viewPager.removeAllViews();
        if(!getActivity().isDestroyed()){
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.remove(mNewsContentOneFragment);
            transaction.remove(mNewsContentTwoFragment);
            transaction.remove(mNewsContentThreeFragment);
            transaction.remove(mNewsContentFourFragment);
            transaction.commit();
        }
    }

    private void InitViewPager(){
        viewPager = (ViewPager)view.findViewById(R.id.news_viewpager);
        mFragment = new ArrayList<Fragment>();
        mFragment.add(mNewsContentOneFragment);
        mFragment.add(mNewsContentTwoFragment);
        mFragment.add(mNewsContentThreeFragment);
        mFragment.add(mNewsContentFourFragment);
        viewPager.setAdapter(new myFragmentPageradapter(getFragmentManager(), mFragment));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    private void InitTextView(){
        title1 = (TextView)view.findViewById(R.id.news_viewpager_title_1);
        title2 = (TextView)view.findViewById(R.id.news_viewpager_title_2);
        title3 = (TextView)view.findViewById(R.id.news_viewpager_title_3);
        title4 = (TextView)view.findViewById(R.id.news_viewpager_title_4);

        title1.setTextColor(Color.GREEN);

        title1.setOnClickListener(new MyOnClickListener(0));
        title2.setOnClickListener(new MyOnClickListener(1));
        title3.setOnClickListener(new MyOnClickListener(2));
        title4.setOnClickListener(new MyOnClickListener(3));
    }

    private void InitImageView(){
        imageView = (ImageView) view.findViewById(R.id.news_viewpager_cursor);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenW = dm.widthPixels;
        ivCursorWidth = BitmapFactory.decodeResource(getResources(),
                R.drawable.viewpager_cursor).getWidth();// 获取图片宽度
        tabWidth = screenW / 4;
        if (ivCursorWidth > tabWidth) {
            imageView.getLayoutParams().width = tabWidth;
            ivCursorWidth = tabWidth;
        }
        offsetX = (tabWidth - ivCursorWidth) / 2;
    }



    public class MyOnClickListener implements View.OnClickListener{
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;

        }

        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(index);

        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageSelected(int position) {
            Animation animation = new TranslateAnimation(tabWidth * currIndex
                    + offsetX, tabWidth * position + offsetX, 0, 0);// 显然这个比较简洁，只有一行代码。
            currIndex = position;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(350);
            imageView.startAnimation(animation);

            switch(position){
                case 0:
                    title1.setTextColor(Color.GREEN);
                    title2.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
                    title3.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
                    title4.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
                    break;
                case 1:
                    title1.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
                    title2.setTextColor(Color.GREEN);
                    title3.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
                    title4.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
                    break;
                case 2:
                    title1.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
                    title2.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
                    title3.setTextColor(Color.GREEN);
                    title4.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
                    break;
                case 3:
                    title1.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
                    title2.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
                    title3.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
                    title4.setTextColor(Color.GREEN);
                    break;
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
