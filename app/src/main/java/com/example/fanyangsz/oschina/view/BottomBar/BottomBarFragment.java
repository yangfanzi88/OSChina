package com.example.fanyangsz.oschina.view.BottomBar;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.fanyangsz.oschina.R;

/**
 * Created by fanyang.sz on 2015/12/30.
 */
public class BottomBarFragment extends Fragment {
    private static final String STATE_SELECTED_POSITION = "selected_bottom_bar_position";
    private int mCurrentSelectedPosition = R.id.bottom_bar_item_1;
    private BottomBarCallbacks mCallbacks;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
        }
        selectItem(mCurrentSelectedPosition, false);
    }

    private void selectItem(int position, boolean isClick) {
        if (position != mCurrentSelectedPosition || !isClick) {

            if (position != R.id.bottom_bar_item_quick) {
                mCurrentSelectedPosition = position;
                if (getView() != null) {
                    getView().findViewById(R.id.bottom_bar_image_1).setSelected(position == R.id.bottom_bar_item_1);
                    getView().findViewById(R.id.bottom_bar_image_2).setSelected(position == R.id.bottom_bar_item_2);
                    getView().findViewById(R.id.bottom_bar_image_3).setSelected(position == R.id.bottom_bar_item_3);
                    getView().findViewById(R.id.bottom_bar_image_4).setSelected(position == R.id.bottom_bar_item_4);
                }
           /* getView().findViewById(R.id.bottom_bar_text_1).setSelected(position == R.id.bottom_bar_item_1);
            getView().findViewById(R.id.bottom_bar_text_2).setSelected(position == R.id.bottom_bar_item_2);
            getView().findViewById(R.id.bottom_bar_text_3).setSelected(position == R.id.bottom_bar_item_3);
            getView().findViewById(R.id.bottom_bar_text_4).setSelected(position == R.id.bottom_bar_item_4);*/
//            getView().findViewById(R.id.bottom_bar_quick_option).setSelected(position == R.id.bottom_bar_item_quick);
            }
            if (mCallbacks != null) {
                mCallbacks.onBottomBarItemSelected(position);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.layout_bottom_bar, container, false);
        LinearLayout linearLayout1 = (LinearLayout) view.findViewById(R.id.bottom_bar_item_1);
        LinearLayout linearLayout2 = (LinearLayout) view.findViewById(R.id.bottom_bar_item_2);
        LinearLayout linearLayout3 = (LinearLayout) view.findViewById(R.id.bottom_bar_item_3);
        LinearLayout linearLayout4 = (LinearLayout) view.findViewById(R.id.bottom_bar_item_4);
        LinearLayout linearLayoutquick = (LinearLayout) view.findViewById(R.id.bottom_bar_item_quick);

        linearLayout1.setOnClickListener(onFliterClickListener);
        linearLayout2.setOnClickListener(onFliterClickListener);
        linearLayout3.setOnClickListener(onFliterClickListener);
        linearLayout4.setOnClickListener(onFliterClickListener);
        linearLayoutquick.setOnClickListener(onFliterClickListener);
        view.findViewById(R.id.bottom_bar_image_1).setSelected(true);
        return view;
    }

    View.OnClickListener onFliterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectItem(v.getId(), true);
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    public void setCallback(BottomBarCallbacks callback) {
        mCallbacks = callback;
    }

    public interface BottomBarCallbacks {
        void onBottomBarItemSelected(int position);
    }
}
