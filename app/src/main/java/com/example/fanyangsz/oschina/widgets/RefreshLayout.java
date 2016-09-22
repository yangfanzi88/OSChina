package com.example.fanyangsz.oschina.widgets;

/**
 * Created by fanyang.sz on 2016/9/12.
 */

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fanyangsz.oschina.R;

/**
 * 上拉加载更多的swiperefreshlayout
 */
public class RefreshLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener {

    private Context mContext;

    private ListView mListView;
    private OnLoadListener mOnLoadListener; //上拉加载监听
    private View mListViewFooter; //底部加载时的布局
    private LinearLayout layLoading;
    private TextView noMore;
    private boolean isLoading; //是否正在加载


    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mListViewFooter = LayoutInflater.from(context).inflate(R.layout.refresh_listview_footer, null, false);
        layLoading = (LinearLayout) mListViewFooter.findViewById(R.id.layLoading);
        noMore = (TextView) mListViewFooter.findViewById(R.id.noMore);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //初始化listview对象
        if (mListView == null) {
            int childs = getChildCount();
            if (childs > 0) {
                View childView = getChildAt(0);
                if (childView instanceof ListView) {
                    mListView = (ListView) childView;
                    mListView.setOnScrollListener(this);
                    mListView.addFooterView(mListViewFooter);
                    mListViewFooter.setVisibility(VISIBLE);
                }
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState == SCROLL_STATE_FLING) {
        } else if (scrollState == SCROLL_STATE_IDLE) {
            for (int i = 0; i < mListView.getFooterViewsCount(); i++) {
                if (mListView.getChildAt(mListView.getChildCount() - i - 1) == mListViewFooter) {
                    if (mListViewFooter != null )
                        loadData();
                    break;
                }
            }
        }

//        View netContainer = findViewById(R.id.setting_net_container);
//        if (netContainer != null && netContainer.getVisibility() == View.VISIBLE) {
//            netContainer.setVisibility(View.GONE);
//        }
    }

    //加载操作
    private void loadData() {
        if (mOnLoadListener != null) {
            setLoading(true);
            mOnLoadListener.onLoad();
        }
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        if (isLoading) {
//            mListViewFooter.setVisibility(VISIBLE);
            layLoading.setVisibility(VISIBLE);
            noMore.setVisibility(GONE);
            noMore.setText("");
        } else {
//            mListViewFooter.setVisibility(GONE);
            layLoading.setVisibility(GONE);
            noMore.setVisibility(VISIBLE);
            noMore.setText(getResources().getString(R.string.refresh_listview_footer_nomore));
        }
    }

    public void setOnLoadListener(OnLoadListener loadListener) {
        mOnLoadListener = loadListener;
    }

    @Override
    public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

    }

    //加载更多的监听器
    public static interface OnLoadListener {
        public void onLoad();
    }
}
