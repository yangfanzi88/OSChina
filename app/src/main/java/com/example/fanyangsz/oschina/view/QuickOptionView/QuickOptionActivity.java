package com.example.fanyangsz.oschina.view.QuickOptionView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;

import com.example.fanyangsz.oschina.R;

/**
 * Created by fanyang.sz on 2016/1/28.
 */
public class QuickOptionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_quick_option);

        this.getWindow().setGravity(Gravity.BOTTOM);
        this.getWindow().setWindowAnimations(R.style.quick_popwindow);


       /* WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);*/
    }

}
