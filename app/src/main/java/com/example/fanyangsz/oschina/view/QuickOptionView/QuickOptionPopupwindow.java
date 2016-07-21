package com.example.fanyangsz.oschina.view.QuickOptionView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.fanyangsz.oschina.R;

/**
 * Created by fanyang.sz on 2016/1/28.
 */

public class QuickOptionPopupwindow extends PopupWindow{

    ImageView imgText, imgAlbum, imgPhoto, imgVoice, imgScan, imgNote;
    Activity activity;

    public QuickOptionPopupwindow(Activity context){
        super(context);
        activity =context;
        View view = View.inflate(context,R.layout.layout_quick_option,null);
        initView(view);
        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.quick_popwindow);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0ffffff);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    private void initView(View view){
        imgText = (ImageView) view.findViewById(R.id.quick_option_text);
        imgText.setOnClickListener(onClickListener);
        imgAlbum = (ImageView) view.findViewById(R.id.quick_option_album);
        imgAlbum.setOnClickListener(onClickListener);
        imgPhoto = (ImageView) view.findViewById(R.id.quick_option_photo);
        imgPhoto.setOnClickListener(onClickListener);
        imgVoice = (ImageView) view.findViewById(R.id.quick_option_voice);
        imgVoice.setOnClickListener(onClickListener);
        imgScan = (ImageView) view.findViewById(R.id.quick_option_scan);
        imgScan.setOnClickListener(onClickListener);
        imgNote = (ImageView) view.findViewById(R.id.quick_option_note);
        imgNote.setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.quick_option_text:
                    activity.startActivity(new Intent(activity,PostTweetActivity.class));
                    dismiss();
                    break;
                case R.id.quick_option_album:
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
                    activity.startActivityForResult(i, 2);
                    break;
                case R.id.quick_option_photo:
                    activity.startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), 2);
                    break;
                case R.id.quick_option_voice:
                    break;
                case R.id.quick_option_scan:
                    break;
                case R.id.quick_option_note:
                    break;
                default:
                    break;
            }
        }
    };

}
