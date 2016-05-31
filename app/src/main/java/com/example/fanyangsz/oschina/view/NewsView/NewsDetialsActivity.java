package com.example.fanyangsz.oschina.view.NewsView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.Support.util.Utils;

public class NewsDetialsActivity extends ActionBarActivity {

    WebView mWebView;
    WebViewClient mWebViewClient;
    Intent intent;
    View loading,fail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("品牌！！！！", android.os.Build.BOARD);

        intent = getIntent();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true); // 决定左上角图标的右侧是否有向左的小箭头, true有小箭头，并且图标可以点击
        actionBar.setDisplayShowHomeEnabled(true);// 使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题，否则，显示应用程序图标，对应id为android.R.id.home，对应ActionBar.DISPLAY_SHOW_HOME
        actionBar.setTitle(intent.getStringExtra("newsTitle"));

        setContentView(R.layout.layout_news_detials);
        mWebView = (WebView)findViewById(R.id.webview);
        loading = findViewById(R.id.loading);
        fail = findViewById(R.id.fail);


        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(true);
        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        settings.setLoadWithOverviewMode(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        initWebView();
    }

    private void initWebView(){



        mWebView.loadUrl(intent.getStringExtra("urlWebView"));

        mWebViewClient = new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loading.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.GONE);
                fail.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);
                if(Boolean.parseBoolean(intent.getStringExtra("hideHead"))){
                    mWebView.setPaddingRelative(mWebView.getPaddingStart(), Utils.dip2px(getBaseContext(), -20),
                            mWebView.getPaddingEnd(), mWebView.getPaddingBottom());
                }
                loading.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
                fail.setVisibility(View.GONE);

            }


            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                loading.setVisibility(View.GONE);
                mWebView.setVisibility(View.GONE);
                fail.setVisibility(View.VISIBLE);
            }
        };

        mWebView.setWebViewClient(mWebViewClient);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_detials, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_refresh:
                initWebView();
                return true;
            case R.id.action_textSize:
                textSizeDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void textSizeDialog(){
        final String items[] = {"特大字号","大字号","中字号","小字号"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(items, 3, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        Toast.makeText(getBaseContext(),"特大字号",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getBaseContext(),"大字号",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getBaseContext(),"中字号",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getBaseContext(),"小字号",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }
}
