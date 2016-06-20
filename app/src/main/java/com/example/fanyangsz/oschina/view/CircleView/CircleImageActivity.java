package com.example.fanyangsz.oschina.view.CircleView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.fanyangsz.oschina.Api.HttpSDK;
import com.example.fanyangsz.oschina.R;

public class CircleImageActivity extends Activity {

    Intent intent;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_circle_popupwindow);
        ImageView imageView = (ImageView) findViewById(R.id.tweet_big_image);
        intent = getIntent();
        url = intent.getStringExtra("bigUrl");
        new HttpSDK().getTweetImage(this,url,imageView);

    }
}
