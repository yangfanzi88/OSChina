package com.example.fanyangsz.oschina.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fanyangsz.oschina.Api.HttpSDK;
import com.example.fanyangsz.oschina.Beans.TweetBean;
import com.example.fanyangsz.oschina.Beans.TweetBeans;
import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.view.CircleView.CircleImageActivity;

/**
 * Created by fanyang.sz on 2016/2/23.
 */
public class TweetAdapter extends BaseAdapter {

    private TweetBeans.TweetList datas;
    private Context context;

    public TweetAdapter(TweetBeans.TweetList datas,Context context) {
        this.datas = datas;
        this.context = context;
    }

    private static class ViewHolder{
        TextView author;
        TextView time;
        TextView content;
        TextView commentcount;
        TextView platform;
        ImageView face;
        ImageView image;
        ImageView tvLikeState;
        TextView del;
        TextView likeUsers;
    }

    @Override
    public int getCount() {
        return datas.getTweet().size();
    }

    @Override
    public Object getItem(int position) {
        return datas.getTweet().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = View.inflate(context, R.layout.list_item_tweet, null);
            holder.author = (TextView) convertView.findViewById(R.id.tv_tweet_name);
            holder.face = (ImageView) convertView.findViewById(R.id.iv_tweet_face);
            holder.content = (TextView) convertView.findViewById(R.id.tweet_item);
            holder.image = (ImageView) convertView.findViewById(R.id.iv_tweet_image);
//            convertView.findViewById(R.id.iv_tweet_image).setVisibility(View.VISIBLE);
            holder.likeUsers = (TextView) convertView.findViewById(R.id.tv_likeusers);
            holder.time = (TextView) convertView.findViewById(R.id.tv_tweet_time);
            holder.platform = (TextView) convertView.findViewById(R.id.tv_tweet_platform);
            holder.del = (TextView) convertView.findViewById(R.id.tv_del);
            holder.tvLikeState = (ImageView) convertView.findViewById(R.id.tv_like_state);
            holder.commentcount = (TextView) convertView.findViewById(R.id.tv_tweet_comment_count);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }


        TweetBean bean = datas.getTweet().get(position);

        holder.author.setText(bean.getAuthor());
        new HttpSDK().getAvatarImage(context,bean.getPortrait(),holder.face);
        holder.content.setText(bean.getBody());
        if(!bean.getImgSmall().equals("")){
            holder.image.setVisibility(View.VISIBLE);
            new HttpSDK().getTweetImage(context,bean.getImgSmall(),holder.image);
            final String url = bean.getImgBig();
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    new CirclePopupWindow(context, url).showAtLocation(View.inflate(context,R.layout.layout_bottom_bar,null), Gravity.CENTER,0,0);
                    Intent intent = new Intent(context, CircleImageActivity.class);
                    intent.putExtra("bigUrl", url);
                    context.startActivity(intent);
                }
            });
        }else {
            holder.image.setVisibility(View.GONE);
        }
//        holder.likeUsers.setText(bean.getLikeCount()+"");
        likeUserShow(holder.likeUsers, bean.getLikeCount(), position);
        holder.time.setText(bean.getPubDate());
        holder.platform.setText(bean.getAppclient()+"");

        holder.commentcount.setText(bean.getCommentCount());
        return convertView;
    }

    private void likeUserShow(TextView textView, int count, int position){
        if(count == 0){
            textView.setVisibility(View.GONE);
        }
        else{
            textView.setVisibility(View.VISIBLE);
            String s = datas.getTweet().get(position).getLikeUser().get(0).getName()
                    + " 等" + count +"人觉得很赞";

            SpannableStringBuilder builder = new SpannableStringBuilder(s);
            ForegroundColorSpan blueSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.tab_background));
            builder.setSpan(blueSpan, 0, datas.getTweet().get(position).getLikeUser().get(0).getName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(builder);
        }
    }

    public TweetBeans.TweetList getDatas() {
        return datas;
    }
}
