package com.example.fanyangsz.oschina.view.CircleView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.fanyangsz.oschina.Api.HttpSDK;
import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.widgets.ZoomImageView;

public class CircleImageActivity extends Activity {

    Intent intent;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_circle_popupwindow);
        ZoomImageView imageView = (ZoomImageView) findViewById(R.id.tweet_big_image);
        intent = getIntent();
        url = intent.getStringExtra("bigUrl");
        HttpSDK.newInstance().getTweetImage(url,imageView,HttpSDK.IMAGE_TYPE_0);

    }
}
