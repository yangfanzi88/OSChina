package com.example.fanyangsz.oschina.view.MainActivity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.example.fanyangsz.oschina.Api.HttpSDK;
import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.Support.util.OSChinaDB;
import com.example.fanyangsz.oschina.view.BottomBar.BottomBarFragment;
import com.example.fanyangsz.oschina.view.CircleView.CircleViewpagerFragment;
import com.example.fanyangsz.oschina.view.DrawerFragment.NavigationDrawerFragment;
import com.example.fanyangsz.oschina.view.LoginView.LoginFragment;
import com.example.fanyangsz.oschina.view.NewsView.NewsContentOneFragment;
import com.example.fanyangsz.oschina.view.NewsView.NewsContentTwoFragment;
import com.example.fanyangsz.oschina.view.NewsView.NewsViewpagerFragment;
import com.example.fanyangsz.oschina.view.QuickOptionView.QuickOptionPopupwindow;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,BottomBarFragment.BottomBarCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private BottomBarFragment mBottomBarFragment;
    private NewsContentOneFragment mContentOneFragment;
    private NewsContentTwoFragment mNewsContentTwoFragment;
    private CircleViewpagerFragment mCircleViewpagerFragment;
    private NewsViewpagerFragment mNewsViewpagerFragment;
    private LoginFragment mLoginFragment;


    private View root;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root = findViewById(R.id.container_content);
        //侧边栏
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mTitle = getString(R.string.app_name);
        //底部功能栏
        mBottomBarFragment = new BottomBarFragment();
        mBottomBarFragment.setCallback(this);
         fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container_bar, mBottomBarFragment);
        transaction.commit();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //初始化httpSDK
        HttpSDK.newInstance(getApplicationContext());
//        //初始化DB表
//        OSChinaDB.setInitDB(getApplicationContext());
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_content, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    @Override
    public void onBottomBarItemSelected(int position) {
//        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch(position){
            case R.id.bottom_bar_item_1:

                    mNewsViewpagerFragment = new NewsViewpagerFragment();

                    transaction.replace(R.id.container_content, mNewsViewpagerFragment);
                    transaction.commit();


                break;
            case R.id.bottom_bar_item_2:
                mCircleViewpagerFragment = new CircleViewpagerFragment();
//                FragmentManager fragmentManager2 = getFragmentManager();
//                FragmentTransaction transaction2 = fragmentManager2.beginTransaction();
                transaction.replace(R.id.container_content, mCircleViewpagerFragment);
                transaction.commit();
                break;
            case R.id.bottom_bar_item_3:
//                contentListView.setAdapter(new ArrayAdapter<String>(
//                        MainActivity.this,
//                        android.R.layout.simple_list_item_activated_1,
//                        android.R.id.text1,
//                        new String[]{
//                                "1", "2", "3", "a", "b", "c", "d", "e", "f", "g", "h", "i"
//                        }));
                ObjectAnimator.ofFloat(mBottomBarFragment.view, "translationY", -mBottomBarFragment.view.getMeasuredHeight()).start();
                ValueAnimator colorAnim = ObjectAnimator.ofInt(mBottomBarFragment.view, "backgroundColor", 0xFFFF8080, 0xFF8080FF);
                colorAnim.setDuration(3000);
                colorAnim.setEvaluator( new ArgbEvaluator());
                colorAnim.setRepeatCount(ValueAnimator.INFINITE);
                colorAnim.setRepeatMode(ValueAnimator.REVERSE);
                colorAnim.start();
                break;
            case R.id.bottom_bar_item_4:
//                contentListView.setAdapter(new ArrayAdapter<String>(
//                        MainActivity.this,
//                        android.R.layout.simple_list_item_activated_1,
//                        android.R.id.text1,
//                        new String[]{
//                                "4", "5", "6", "a", "b", "c", "d", "e", "f", "g", "h", "i"
//                        }));

//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, LoginActivity.class);
//                MainActivity.this.startActivity(intent);
                mLoginFragment = new LoginFragment();
                transaction.replace(R.id.container_content, mLoginFragment);
                transaction.commit();
                break;
            case R.id.bottom_bar_item_quick:
               /* //第一种方法：用activity实现
                Intent intent = new Intent(this, QuickOptionActivity.class);
                startActivity(intent);*/

                //第二种方法，用dialog实现
                /*CustomerDialog myDialog = new CustomerDialog(MainActivity.this, R.style.Theme_Light_Dialog);
                myDialog.show();*/

                //第三种方法，用popupwindow实现

                QuickOptionPopupwindow myPopupWindow = new QuickOptionPopupwindow(this);

                myPopupWindow.showAtLocation(MainActivity.this.findViewById(R.id.bottom_bar_quick_option), Gravity.BOTTOM, 0, getNavigationBarHeight(this));//显示(idol3手机上的虚拟按键)

                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 0.7f;
                getWindow().setAttributes(lp);
                myPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
//                        myPopupWindow.dismiss();
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1f;
                        getWindow().setAttributes(lp);

                    }
                });

                break;
        }
    }

    public static int getNavigationBarHeight(Context context) {
        int height = 0;
        try {
            Resources resources = context.getResources();
//            int resourceId = resources.getIdentifier("navigation_bar_height_landscape", "dimen", "android");
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            height = resources.getDimensionPixelSize(resourceId);
            Log.v("dbw", "Navi height:" + height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return height;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onSectionAttached(int number) {
        /*switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
        }*/
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
