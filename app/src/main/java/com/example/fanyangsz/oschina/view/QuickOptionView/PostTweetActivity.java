package com.example.fanyangsz.oschina.view.QuickOptionView;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.fanyangsz.oschina.Api.HttpSDK;
import com.example.fanyangsz.oschina.Beans.TweetBean;
import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.view.LoginView.LoginFragment;

public class PostTweetActivity extends ActionBarActivity implements Response.Listener,Response.ErrorListener {

    private EditText editText;
    private ImageView userPicture;
    private ImageView clearUserPicture;
    private TextView textCount;
    private ImageButton selectPicture;
    private ImageButton selectMention;
    private ImageButton selectTrendSofeware;
    private ImageButton selectEmoji;

    private final int writeMaxCount = 160;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_tweet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    private void initView(){
        editText = (EditText) findViewById(R.id.et_content);
        editText.addTextChangedListener(onTextWatch);

        userPicture = (ImageView)findViewById(R.id.iv_img);

        clearUserPicture = (ImageView)findViewById(R.id.iv_clear_img);
        clearUserPicture.setOnClickListener(onClickListener);

        textCount = (TextView)findViewById(R.id.tv_clear);
        textCount.setText(writeMaxCount + "");

        selectPicture = (ImageButton)findViewById(R.id.ib_picture);
        selectPicture.setOnClickListener(onClickListener);

        selectMention = (ImageButton)findViewById(R.id.ib_mention);
        selectMention.setOnClickListener(onClickListener);

        selectTrendSofeware = (ImageButton)findViewById(R.id.ib_trend_software);
        selectTrendSofeware.setOnClickListener(onClickListener);

        selectEmoji = (ImageButton)findViewById(R.id.ib_emoji_keyboard);
        selectEmoji.setOnClickListener(onClickListener);

    }

    private TextWatcher onTextWatch = new TextWatcher() {

        private CharSequence temp;
        private int selectionStart;
        private int selectionEnd;
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s;
            if((writeMaxCount - s.length()) <= 0) {
                textCount.setText(0 + "");
            }else{
                textCount.setText(writeMaxCount - s.length() + "");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            selectionStart = editText.getSelectionStart();
            selectionEnd = editText.getSelectionEnd();
            if (temp.length() > writeMaxCount) {
                s.delete(selectionStart - 1, selectionEnd);
                editText.setText(s);
                editText.setSelection(s.length());//设置光标在最后
            }
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){

            }
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tweet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_publish:
                TweetBean bean = new TweetBean();
                if(!TextUtils.isEmpty(editText.getText())){
                    bean.setBody(editText.getText().toString());
                    bean.setAuthorid(LoginFragment.getLoginUser(this).getUser().getId());
                    HttpSDK.newInstance().pubTweet(bean,this,this);
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {

    }

    @Override
    public void onResponse(Object o) {
        PostTweetActivity.this.finish();
    }
}
